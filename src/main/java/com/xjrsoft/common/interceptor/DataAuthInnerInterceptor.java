package com.xjrsoft.common.interceptor;

import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.xjrsoft.common.handler.DataAuthHandler;
import com.xjrsoft.common.utils.DataAuthUtil;
import com.xjrsoft.core.launch.constant.TokenConstant;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.core.tool.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserTreeConstants;
import net.sf.jsqlparser.parser.Node;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;

//@AllArgsConstructor
@Slf4j
public class DataAuthInnerInterceptor extends JsqlParserSupport implements InnerInterceptor {

    private DataAuthHandler dataAuthHandler;

    public DataAuthInnerInterceptor(DataAuthHandler dataAuthHandler){
        this.dataAuthHandler = dataAuthHandler;
    }

    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.SELECT) {
            if (SqlParserHelper.getSqlParserInfo(ms)) {
                return;
            }

            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
            mpBs.sql(this.parserMulti(mpBs.sql(), (Object)null));
        }

    }

    @Override
    protected void processSelect(Select select, int index, Object obj) {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null || StringUtil.isEmpty(request.getHeader(TokenConstant.KEY_OF_HEADER_MODULE_ID))) {
            return;
        }
        this.processSelectBody(select.getSelectBody());
        List<WithItem> withItemsList = select.getWithItemsList();
        if (!CollectionUtils.isEmpty(withItemsList)) {
            withItemsList.forEach(this::processSelectBody);
        }

    }

    protected void processSelectBody(SelectBody selectBody) {
        if (selectBody != null) {
            if (selectBody instanceof PlainSelect) {
                this.processPlainSelect((PlainSelect)selectBody);
            } else if (selectBody instanceof WithItem) {
                WithItem withItem = (WithItem)selectBody;
                this.processSelectBody(withItem.getSelectBody());
            } else {
                SetOperationList operationList = (SetOperationList)selectBody;
                if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                    operationList.getSelects().forEach(this::processSelectBody);
                }
            }

        }
    }

    protected void processPlainSelect(PlainSelect plainSelect) {
        FromItem fromItem = plainSelect.getFromItem();
        Expression where = plainSelect.getWhere();
        this.processWhereSubSelect(where);
        if (fromItem instanceof Table) {
            Table fromTable = (Table)fromItem;
            if (!this.dataAuthHandler.ignoreTable(fromTable.getName()) && !this.isSkipAuth(plainSelect)) {
                plainSelect.setWhere(this.builderExpression(where, fromTable));
            }
        } else {
            this.processFromItem(fromItem);
        }

//        List<Join> joins = plainSelect.getJoins();
//        if (joins != null && joins.size() > 0) {
//            joins.forEach((j) -> {
//                this.processJoin(j);
//                this.processFromItem(j.getRightItem());
//            });
//        }

    }

    protected void processWhereSubSelect(Expression where) {
        if (where != null) {
            if (where instanceof FromItem) {
                this.processFromItem((FromItem)where);
            } else {
                if (where.toString().indexOf("SELECT") > 0) {
                    if (where instanceof BinaryExpression) {
                        BinaryExpression expression = (BinaryExpression)where;
                        this.processWhereSubSelect(expression.getLeftExpression());
                        this.processWhereSubSelect(expression.getRightExpression());
                    } else if (where instanceof InExpression) {
                        InExpression expression = (InExpression)where;
                        ItemsList itemsList = expression.getRightItemsList();
                        if (itemsList instanceof SubSelect) {
                            this.processSelectBody(((SubSelect)itemsList).getSelectBody());
                        }
                    } else if (where instanceof ExistsExpression) {
                        ExistsExpression expression = (ExistsExpression)where;
                        this.processWhereSubSelect(expression.getRightExpression());
                    } else if (where instanceof NotExpression) {
                        NotExpression expression = (NotExpression)where;
                        this.processWhereSubSelect(expression.getExpression());
                    } else if (where instanceof Parenthesis) {
                        Parenthesis expression = (Parenthesis)where;
                        this.processWhereSubSelect(expression.getExpression());
                    }
                }

            }
        }
    }

    protected void processFromItem(FromItem fromItem) {
//        if (fromItem instanceof SubJoin) {
//            SubJoin subJoin = (SubJoin)fromItem;
//            if (subJoin.getJoinList() != null) {
//                subJoin.getJoinList().forEach(this::processJoin);
//            }
//
//            if (subJoin.getLeft() != null) {
//                this.processFromItem(subJoin.getLeft());
//            }
//        } else
        if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect)fromItem;
            if (subSelect.getSelectBody() != null) {
                this.processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            this.logger.debug("Perform a subquery, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect)fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    this.processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

//    protected void processJoin(Join join) {
//        if (join.getRightItem() instanceof Table) {
//            Table fromTable = (Table)join.getRightItem();
//            if (this.dataAuthHandler.ignoreTable(fromTable.getName())) {
//                return;
//            }
//
//            join.setOnExpression(this.builderExpression(join.getOnExpression(), fromTable));
//        }
//
//    }

    protected Expression builderExpression(Expression currentExpression, Table table) {
        Expression expression = null;
        ExpressionList expressionList = this.dataAuthHandler.getExpressionList();
        if (expressionList == null || CollectionUtil.isEmpty(expressionList.getExpressions())) {
            return currentExpression;
        }
        List<Expression> expressions = expressionList.getExpressions();
        if (expressions.size() == 1) {
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(this.getAliasColumn(table));
            equalsTo.setRightExpression(expressionList.getExpressions().get(0));
            expression = equalsTo;
        } else {
            InExpression inExpression = new InExpression();
            inExpression.setLeftExpression(this.getAliasColumn(table));
            inExpression.setRightItemsList(expressionList);
            expression = inExpression;
        }
        log.info("添加权限过滤条件：" + expression.toString());
        if (currentExpression == null) {
            return expression;
        } else {
            return currentExpression instanceof OrExpression ? new AndExpression(new Parenthesis(currentExpression), expression) : new AndExpression(currentExpression, expression);
        }
    }
    protected Column getAliasColumn(Table table) {
        StringBuilder column = new StringBuilder();
        if (table.getAlias() != null) {
            column.append(table.getAlias().getName()).append(".");
        }

        column.append(this.dataAuthHandler.getDataAuthColumn());
        return new Column(column.toString());
    }

    protected boolean isSkipAuth(PlainSelect plainSelect) {
        Node node = plainSelect.getASTNode().jjtGetParent();
        Node parentNode = node.jjtGetParent();
        // 非最外层sql，不需要加限制F_CreateUserId
        if (node.getId() != CCJSqlParserTreeConstants.JJTSETOPERATIONLIST
                || parentNode.getId() != CCJSqlParserTreeConstants.JJTSTATEMENTS) {
            return true;
        }
        // 查询是否包含F_CreateUserId字段
        return !isContainsDataAuthColumn(plainSelect);
    }

    protected boolean isContainsDataAuthColumn(PlainSelect plainSelect) {
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            SelectBody selectBody = subSelect.getSelectBody();
            if ((selectBody instanceof PlainSelect) && !isContainsDataAuthColumn((PlainSelect) selectBody)) {
                return true;
            }
            return false;
        }
        Alias tableAlias = fromItem.getAlias();
        String dataAuthColumn = this.dataAuthHandler.getDataAuthColumn();
        for (SelectItem selectItem : selectItems) {
            if (selectItem instanceof SelectExpressionItem) {
                SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
                Expression expression = selectExpressionItem.getExpression();
                if (expression instanceof Column) {
                    Column column = (Column) expression;
                    if (tableAlias != null && !StringUtil.equals(column.getTable().getName(), tableAlias.getName())) {
                        continue;
                    } else if (StringUtil.equals(dataAuthColumn, column.getColumnName())) {
                        return true;
                    }
                }
            } else if (selectItem instanceof AllTableColumns) {
                AllTableColumns allTableColumns = (AllTableColumns) selectItem;
                if (!StringUtil.equals(allTableColumns.getTable().getName(), tableAlias.getName())) {
                    continue;
                }
                Table table = (Table) fromItem;
                if (DataAuthUtil.isContainsField(table.getName(), dataAuthColumn)) {
                    return true;
                }
            } else if (selectItem instanceof AllColumns) {
                Table table = (Table) fromItem;
                if (DataAuthUtil.isContainsField(table.getName(), dataAuthColumn)) {
                    return true;
                }
            }
        }
        return false;
    }
}
