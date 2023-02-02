package com.xjrsoft.module.buildCode.grant;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.SqlServerTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.core.tool.utils.SpringUtil;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.buildCode.config.InjectionConfig;
import com.xjrsoft.module.buildCode.dto.*;
import com.xjrsoft.module.buildCode.utils.BuildCodeUtil;
import com.xjrsoft.module.form.constant.FormConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码生成器配置类
 *
 * @author Chill
 */
@Data
@Slf4j
public class XjrCodeGenerator {

	private Boolean isWebModule = true;

	/**
	 * 代码模块名称
	 */
	private String codeName;
	/**
	 * 代码所在服务名
	 */
	private String serviceName = "xjr-service";
	/**
	 * 代码生成的包名
	 */
	private String packageName = "com.xjrsoft.test";
	/**
	 * 代码后端生成的地址
	 */
	private String packageDir;
	/**
	 * 代码前端生成的地址
	 */
	private String packageWebDir;
	/**
	 * 需要去掉的表前缀
	 */
	private String[] tablePrefix = {""};
	/**
	 * 是否包含基础业务字段
	 */
	private Boolean hasSuperEntity = Boolean.FALSE;
	/**
	 * 是否包含包装器
	 */
	private Boolean hasWrapper = Boolean.FALSE;
	/**
	 * 基础业务字段
	 */
	private String[] superEntityColumns = {"create_time", "create_user", "create_dept", "update_time", "update_user", "status", "is_deleted"};
//	/**
//	 * 租户字段
//	 */
//	private String tenantColumn = "tenant_id";
	/**
	 * 是否启用swagger
	 */
	private Boolean isSwagger2 = Boolean.TRUE;
	/**
	 * 数据库驱动名
	 */
	private String driverName;
	/**
	 * 数据库链接地址
	 */
	private String url;
	/**
	 * 数据库用户名
	 */
	private String username;
	/**
	 * 数据库密码
	 */
	private String password;

	/**
	 * 数据库类型
	 */
	private DbType dbType;

	private Map<String, Object> params;

	private Map<String, String> resultCodeMap = new HashMap<>(16);

	/**
	 *
	 * @param codeSchemaDto
	 * @param isWriteCode 是否写入文件
	 */
	public void run(CodeSchemaDto codeSchemaDto, boolean isWriteCode, boolean isDynamicData) {
		BaseInfoDto baseInfoDto = codeSchemaDto.getBaseInfoDto();

		DbTableDto parentTableDto = codeSchemaDto.getDbTableDtoList().stream().filter(table -> StringUtil.isBlank(table.getRelationName())).findFirst().get();
		if (isWebModule) {
			String listCode = genListViewCode(codeSchemaDto.getDbLinkId(), baseInfoDto, codeSchemaDto.getQueryDataDtoList(), codeSchemaDto.getColDataDto(), parentTableDto, codeSchemaDto.getTotalDataDto(), codeSchemaDto.getFormDataDtoList());
			if (!isWriteCode) {
				resultCodeMap.put("listHTML", listCode);
			}
		}
		if (baseInfoDto.getIsOnlyFont()) {
			return;
		}
		List<String> includeTableList = codeSchemaDto.getDbTableDtoList().stream().map(table -> table.getName()).collect(Collectors.toList());
		AutoGenerator mpg = new AutoGenerator();
		GlobalConfig gc = new GlobalConfig();
		String outputDir = getOutputDir();
		gc.setOutputDir(outputDir);
		gc.setAuthor(codeSchemaDto.getBaseInfoDto().getCreateUser());
		gc.setFileOverride(true);
		gc.setOpen(false);
		gc.setActiveRecord(false);
		gc.setEnableCache(false);
		gc.setBaseResultMap(true);
		gc.setBaseColumnList(true);
		gc.setMapperName("%sMapper");
		gc.setXmlName("%sMapper");
		gc.setServiceName("I%sService");
		gc.setServiceImplName("%sServiceImpl");
		gc.setControllerName("%sController");
		gc.setSwagger2(isSwagger2);
		mpg.setGlobalConfig(gc);
		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		// strategy.setCapitalMode(true);// 全局大写命名
		// strategy.setDbColumnUnderline(true);//全局下划线命名
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		strategy.setTablePrefix(tablePrefix);
		if (includeTableList.size() > 0) {
			strategy.setInclude(includeTableList.toArray(new String[]{}));
		}
		if (hasSuperEntity) {
			strategy.setSuperEntityClass("com.xjrsoft.core.mp.base.BaseEntity");
			strategy.setSuperEntityColumns(superEntityColumns);
			strategy.setSuperServiceClass("com.xjrsoft.core.mp.base.BaseService");
			strategy.setSuperServiceImplClass("com.xjrsoft.core.mp.base.BaseServiceImpl");
		} else {
			strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
			strategy.setSuperServiceImplClass("com.xjrsoft.core.mp.base.BaseService");
		}
		strategy.setEntityBuilderModel(false);
		strategy.setEntityLombokModel(true);
		strategy.setControllerMappingHyphenStyle(true);
		strategy.setEntityTableFieldAnnotationEnable(true);
//		strategy.setNameConvert(new XjrNameConvert());
		mpg.setStrategy(strategy);

		if (isDynamicData) {
			mpg.setTableInfoList(BuildCodeUtil.buildTableInfoList(codeSchemaDto.getTableInfoDtoList(), dbType, strategy, gc));
		} else {
			DataSourceConfig dsc = new DataSourceConfig();
			DynamicDataSourceProperties dataSourceProperties = SpringUtil.getBean(DynamicDataSourceProperties.class);
			DataSourceProperty dataSourceProperty = dataSourceProperties.getDatasource().get(dataSourceProperties.getPrimary());
			String driverName = Func.toStr(this.driverName, dataSourceProperty.getDriverClassName());
			if (dbType == DbType.MYSQL) {
				dsc.setTypeConvert(new MySqlTypeConvert());
			} else if (dbType == DbType.POSTGRE_SQL) {
				dsc.setTypeConvert(new PostgreSqlTypeConvert());
			} else if (dbType == DbType.SQL_SERVER || dbType == DbType.SQL_SERVER2005) {
				dsc.setTypeConvert(new SqlServerTypeConvert());
			} else {
				dsc.setTypeConvert(new OracleTypeConvert());
			}
//			dsc.setDbType(dbType);
			dsc.setDriverName(driverName);
			dsc.setUrl(Func.toStr(this.url, dataSourceProperty.getUrl()));
			dsc.setUsername(Func.toStr(this.username, dataSourceProperty.getUsername()));
			dsc.setPassword(Func.toStr(this.password, dataSourceProperty.getPassword()));
			mpg.setDataSource(dsc);
		}
		// 包配置
		PackageConfig pc = new PackageConfig();
		// 控制台扫描
		pc.setModuleName(null);
		pc.setParent(packageName);
		pc.setController("controller");
		pc.setEntity("entity");
		pc.setXml("mapper");
		mpg.setPackageInfo(pc);
		mpg.setCfg(getInjectionConfig(includeTableList.size()));
		// 设置模板引擎
		XjrTemplateEngine templateEngine = buildTemplateEngine(codeSchemaDto);
		// 是否预览代码，不写入文件
		templateEngine.setWriteCode(isWriteCode);
		mpg.setTemplateEngine(templateEngine);
		mpg.execute();
		resultCodeMap.putAll(buildResultCode(templateEngine.getResultCodeMap()));
	}

	private InjectionConfig getInjectionConfig(Integer entityCount) {
		String servicePackage = serviceName.split("-").length > 1 ? serviceName.split("-")[1] : serviceName;
		// 自定义配置
		Map<String, Object> map = new HashMap<>(16);
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				map.put("codeName", codeName);
				map.put("serviceName", serviceName);
				map.put("servicePackage", StringUtil.lowerFirst(servicePackage));
//				map.put("tenantColumn", tenantColumn);
				map.put("hasWrapper", hasWrapper);
				this.setMap(map);
			}
		};
		List<FileOutConfig> focList = new ArrayList<>();
//		focList.add(new FileOutConfig("/templates/sql/menu.sql.vm") {
//			@Override
//			public String outputFile(TableInfo tableInfo) {
//				map.put("entityKey", (tableInfo.getEntityName().toLowerCase()));
//				map.put("menuId", IdWorker.getId());
//				map.put("addMenuId", IdWorker.getId());
//				map.put("editMenuId", IdWorker.getId());
//				map.put("removeMenuId", IdWorker.getId());
//				map.put("viewMenuId", IdWorker.getId());
//				return getOutputDir() + "/" + "/sql/" + tableInfo.getEntityName().toLowerCase() + ".menu.mysql";
//			}
//		});
		focList.add(new FileOutConfig("/templates/entityVo.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return getOutputDir() + "/" + packageName.replace(".", "/") + "/" + "vo" + "/" + tableInfo.getEntityName() + "Vo" + StringPool.DOT_JAVA;
			}
		});
		focList.add(new FileOutConfig("/templates/entityDto.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return getOutputDir() + "/" + packageName.replace(".", "/") + "/" + "dto" + "/" + tableInfo.getEntityName() + "Dto" + StringPool.DOT_JAVA;
			}
		});
		focList.add(new FileOutConfig("/templates/saveFormDataDto.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return getOutputDir() + "/" + packageName.replace(".", "/") + "/" + "dto" + "/Save" + tableInfo.getEntityName() + "FormDataDto" + StringPool.DOT_JAVA;
			}
		});
		if (entityCount > 1) {
			focList.add(new FileOutConfig("/templates/formDataVo.java.vm") {
				@Override
				public String outputFile(TableInfo tableInfo) {
					return getOutputDir() + "/" + packageName.replace(".", "/") + "/" + "vo" + "/" + tableInfo.getEntityName() + "FormDataVo" + StringPool.DOT_JAVA;
				}
			});
		}

		focList.add(new FileOutConfig("/templates/listVo.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return getOutputDir() + "/" + packageName.replace(".", "/") + "/" + "vo" + "/" + tableInfo.getEntityName() + "ListVo" + StringPool.DOT_JAVA;
			}
		});
		focList.add(new FileOutConfig("/templates/listDto.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return getOutputDir() + "/" + packageName.replace(".", "/") + "/" + "dto" + "/" + tableInfo.getEntityName() + "ListDto" + StringPool.DOT_JAVA;
			}
		});
		if (Func.isNotBlank(packageWebDir)) {
			if (Func.equals("systemName", "DevelopConstant.SWORD_NAME")) {
				focList.add(new FileOutConfig("/templates/sword/action.js.vm") {
					@Override
					public String outputFile(TableInfo tableInfo) {
						return getOutputWebDir() + "/actions" + "/" + tableInfo.getEntityName().toLowerCase() + ".js";
					}
				});
				focList.add(new FileOutConfig("/templates/sword/model.js.vm") {
					@Override
					public String outputFile(TableInfo tableInfo) {
						return getOutputWebDir() + "/models" + "/" + tableInfo.getEntityName().toLowerCase() + ".js";
					}
				});
				focList.add(new FileOutConfig("/templates/sword/service.js.vm") {
					@Override
					public String outputFile(TableInfo tableInfo) {
						return getOutputWebDir() + "/services" + "/" + tableInfo.getEntityName().toLowerCase() + ".js";
					}
				});
				focList.add(new FileOutConfig("/templates/sword/list.js.vm") {
					@Override
					public String outputFile(TableInfo tableInfo) {
						return getOutputWebDir() + "/pages" + "/" + StringUtil.upperFirst(servicePackage) + "/" + tableInfo.getEntityName() + "/" + tableInfo.getEntityName() + ".js";
					}
				});
				focList.add(new FileOutConfig("/templates/sword/add.js.vm") {
					@Override
					public String outputFile(TableInfo tableInfo) {
						return getOutputWebDir() + "/pages" + "/" + StringUtil.upperFirst(servicePackage) + "/" + tableInfo.getEntityName() + "/" + tableInfo.getEntityName() + "Add.js";
					}
				});
				focList.add(new FileOutConfig("/templates/sword/edit.js.vm") {
					@Override
					public String outputFile(TableInfo tableInfo) {
						return getOutputWebDir() + "/pages" + "/" + StringUtil.upperFirst(servicePackage) + "/" + tableInfo.getEntityName() + "/" + tableInfo.getEntityName() + "Edit.js";
					}
				});
				focList.add(new FileOutConfig("/templates/sword/view.js.vm") {
					@Override
					public String outputFile(TableInfo tableInfo) {
						return getOutputWebDir() + "/pages" + "/" + StringUtil.upperFirst(servicePackage) + "/" + tableInfo.getEntityName() + "/" + tableInfo.getEntityName() + "View.js";
					}
				});
			} else if (Func.equals("systemName", "DevelopConstant.SABER_NAME")) {
				focList.add(new FileOutConfig("/templates/saber/api.js.vm") {
					@Override
					public String outputFile(TableInfo tableInfo) {
						return getOutputWebDir() + "/api" + "/" + servicePackage.toLowerCase() + "/" + tableInfo.getEntityName().toLowerCase() + ".js";
					}
				});
				focList.add(new FileOutConfig("/templates/saber/crud.vue.vm") {
					@Override
					public String outputFile(TableInfo tableInfo) {
						return getOutputWebDir() + "/views" + "/" + servicePackage.toLowerCase() + "/" + tableInfo.getEntityName().toLowerCase() + ".vue";
					}
				});
			}
		}
		cfg.setFileOutConfigList(focList);
		return cfg;
	}

	/**
	 * 生成到项目中
	 *
	 * @return outputDir
	 */
	public String getOutputDir() {
		return (Func.isBlank(packageDir) ? System.getProperty("user.dir") : packageDir) + "/src/main/java";
	}

	/**
	 * 生成到Web项目中
	 *
	 * @return outputDir
	 */
	public String getOutputWebDir() {
		return (Func.isBlank(packageWebDir) ? System.getProperty("user.dir") : packageWebDir) + "/src";
	}

	/**
	 * 页面生成的文件名
	 */
	private String getGeneratorViewPath(String viewOutputDir, TableInfo tableInfo, String suffixPath) {
		String name = StringUtil.lowerFirst(tableInfo.getEntityName());
		String path = viewOutputDir + "/" + name + "/" + name + suffixPath;
		File viewDir = new File(path).getParentFile();
		if (!viewDir.exists()) {
			viewDir.mkdirs();
		}
		return path;
	}

	/**
	 * 构建模板引擎
	 *
	 * @param codeSchemaDto
	 * @return
	 */
	private XjrTemplateEngine buildTemplateEngine(CodeSchemaDto codeSchemaDto) {
		XjrTemplateEngine templateEngine = new XjrTemplateEngine();
		List<DbTableDto> dbTableDtoList = codeSchemaDto.getDbTableDtoList();
		String mainTableName = StringPool.EMPTY;
		for (DbTableDto dbTableDto : dbTableDtoList) {
			if (dbTableDto.isMainTable()) mainTableName = dbTableDto.getName();
		}
		templateEngine.setDbLinkId(codeSchemaDto.getDbLinkId());
		templateEngine.setMainTableName(mainTableName);
		templateEngine.setTableList(BeanUtils.beansToMaps(dbTableDtoList));
		List<FormDataDto> formDataDtoList = codeSchemaDto.getFormDataDtoList();
		List<String> mainTableFieldList = new ArrayList<>();
		Map<String, List<String>> tableFielListdMap = new HashMap<>(dbTableDtoList.size());
		tableFielListdMap.put(mainTableName, mainTableFieldList);
		// 查询字段
		List<QueryDataDto> queryDataDtoList = codeSchemaDto.getQueryDataDtoList();
		List<Map<String, String>> queryFieldList = new ArrayList<>(queryDataDtoList.size());
		// 列表字段
		ColDataDto colDataDto = codeSchemaDto.getColDataDto();
		List<Map<String, Object>> colDataList = colDataDto.getColumnList();
		List<Map<String, String>> columnList = new ArrayList<>(colDataList.size());
		for (FormDataDto formDataDto : formDataDtoList) {
			Map<String, Object> config = formDataDto.getConfig();
			String componentName = MapUtils.getString(config, "componentName");
			if (StringUtil.equalsIgnoreCase(componentName, "avue-tabs")) {
				Map<String, Object> tabsMap = MapUtils.getMap(config, "childrenObj");
				for (Object tabObj: tabsMap.values()) {
					Collection<Map<String, Object>> tabComponents = (Collection<Map<String, Object>>) tabObj;
					for (Map<String, Object> tabComponent : tabComponents) {
						Map<String, Object> tabFieldConfig = MapUtils.getMap(tabComponent, FormConstants.CONFIG);
						parseFieldData(templateEngine, tabFieldConfig, tableFielListdMap, mainTableFieldList, queryDataDtoList, queryFieldList, columnList, colDataDto.getColumnList(), MapUtils.getString(tabComponent, "__type__"), MapUtils.getString(tabComponent, "infoType"));
					}
				}
			} else {
				parseFieldData(templateEngine, config, tableFielListdMap, mainTableFieldList, queryDataDtoList, queryFieldList, columnList, colDataDto.getColumnList(), formDataDto.getComponentType(), formDataDto.getInfoType());
			}
		}
		// 查询字段
		templateEngine.setQueryFieldList(queryFieldList);
		// 列表字段
		templateEngine.setColumnList(columnList);
		templateEngine.setTableFieldListMap(tableFielListdMap);
		templateEngine.setPage(StringUtil.equals(colDataDto.getIsPage(), "1"));
		boolean isTree = colDataDto.getIsTree() != null && colDataDto.getIsTree() == 1;
		templateEngine.setTree(isTree);
		// 是否用于工作流
		Boolean isWorkflowForm = codeSchemaDto.getBaseInfoDto().getIsWorkflowForm();
		templateEngine.setWorkflowForm(isWorkflowForm == null ? false : isWorkflowForm);
		if (isTree) {
			templateEngine.setTreeForm(colDataDto.getTreeForm());
		}
		ImportDataDto importDataDto = colDataDto.getImportDataDto();
		if (importDataDto != null && CollectionUtil.isNotEmpty(importDataDto.getImportFieldsDtoList())) {
			templateEngine.setImport(true);
		}

		return templateEngine;
	}

	public void parseFieldData(XjrTemplateEngine templateEngine, Map<String, Object> config, Map<String, List<String>> tableFielListdMap,
							   List<String> mainTableFieldList, List<QueryDataDto> queryDataDtoList, List<Map<String, String>> queryFieldList,
							   List<Map<String, String>> columnList, List<Map<String, Object>> columnDtoList, String componentType, String infoType) {
		boolean isTrans = templateEngine.isTrans();
		String fieldName = Func.toStr(config.get("bindTableField"));
		Object childrenObj = config.get("children");
		String avueType = MapUtils.getString(config, "avueType");
		// 设置自动编码配置
		if (StringUtil.equalsIgnoreCase(avueType, "autoCode")) {
			templateEngine.setCodeRule(Func.toStr(config.get("autoCodeRule")));
		}
		if (childrenObj != null) {
			List<String> subTableFiledList = new ArrayList<>();
			List<Map<String, Object>> childrenList = (List<Map<String, Object>>) childrenObj;
			for (Map<String, Object> field : childrenList) {
				Map<String, Object> subConfig = (Map<String, Object>) field.get("__config__");
				subTableFiledList.add(Func.toStr(subConfig.get("bindTableField")));
			}
			tableFielListdMap.put(Func.toStr(config.get("bindTable")), subTableFiledList);
		} else {
			mainTableFieldList.add(fieldName);
		}

		// 查询字段
		if (CollectionUtil.isNotEmpty(queryDataDtoList)) {
			for (QueryDataDto queryDataDto : queryDataDtoList) {
				String queryFieldName = queryDataDto.getField();
				if (queryDataDto.getIsDate() != null && queryDataDto.getIsDate()
						&& StringUtil.equals(fieldName, queryFieldName) && !StringUtil.isEmpty(fieldName)) {
					Map<String, String> queryField = new HashMap<>(4);
					queryField.put("fieldName", queryFieldName);
					queryField.put("queryType", BuildCodeUtil.getQueryType(Func.toStr(config.get("avueType"))));
					queryFieldList.add(queryField);
				}
			}
		}

		// 列表字段
		for (Map<String, Object> columnMap: columnDtoList) {
			String columnName = MapUtils.getString(columnMap, "bindColumn");
			if (StringUtil.equals(fieldName, columnName)) {
//				String componentType = formDataDto.getComponentType();
				Map<String, String> columnField = new HashMap<>(8);
				columnField.put("columnName", columnName);
				if (StringUtil.equals(avueType, "select") ||
						StringUtil.equals(avueType, "radio") ||
						StringUtil.equals(avueType, "checkbox")) {
					columnField.put("dataType", MapUtils.getString(config, "dataType"));
					columnField.put("dataItem", MapUtils.getString(config, "dataItem"));
					columnField.put("dataSource", MapUtils.getString(config, "dataSource"));
					columnField.put("showField", MapUtils.getString(config, "showField"));
					columnField.put("saveField", MapUtils.getString(config, "saveField"));
					if (!isTrans) isTrans = true;
				} else if (StringUtil.equals(componentType, "info") ||
						StringUtil.equals(componentType, "company") ||
						StringUtil.equals(componentType, "department") ||
						StringUtil.equals(componentType, "user")) {
					columnField.put("dataType", StringUtil.equals(componentType, "info") ? infoType : componentType);
					if (!isTrans) isTrans = true;
				} else if (StringUtil.equalsIgnoreCase(avueType, "select-area")) {
					columnField.put("dataType", "area");
					if (!isTrans) isTrans = true;
				}
				if (StringUtil.equalsIgnoreCase(avueType, "checkbox") || StringUtil.equalsIgnoreCase(componentType, "user")
						|| StringUtil.equalsIgnoreCase(avueType, "select-area")) {
					columnField.put("multi", "true");
				} else {
					columnField.put("multi", "false");
				}
				columnList.add(columnField);
			}
		}
		templateEngine.setTrans(isTrans);
	}

	public String genListViewCode(String dbLinkId, BaseInfoDto bModel, List<QueryDataDto> queryModel, ColDataDto colModel, DbTableDto parentTable, List<TotalDataDto> totalData, List<FormDataDto> formDataList) {

		StringBuilder sb = new StringBuilder();

		boolean isWorkflow = (Boolean)(colModel.getButtonList().get(8).get("checked"))&&bModel.getIsWorkflowForm();

		sb.append("<template>\r\n");
		sb.append("    <div class=\"main-container\">\r\n");


		//是否有左边树结构
		if(colModel.getIsTree() > 0)
		{
			sb.append("        <main-fixed :title=\"treeTitle\">\r\n");
			sb.append("            <div slot=\"list\">\r\n");
			sb.append("                 <el-tree :data=\"treeData\" :props=\"defaultProps\" ref=\"" + bModel.getName() + "Tree\" node-key=\"" + MapUtils.getString(colModel.getTreeForm(), "treefieldId") + "\"\r\n");
			sb.append("                                        :default-expand-all=\"true\"  @node-click=\"handleNodeClick\"></el-tree>\r\n");
			sb.append("            </div>\r\n");
			sb.append("        </main-fixed>\r\n");
		}


		//-----------------------------搜索开始--------------------------------
		if(colModel.getIsTree() > 0)
		{
			sb.append("    <main-content :title=\"contentTitle\" :paddingLeft=\"210\">\r\n");
		}
		else
		{
			sb.append("    <main-content :title=\"contentTitle\" >\r\n");
		}

		String dataTimeMethod = "";//时间字段方法
		String searchReset = "";//所有搜索字段重置
		String searchInit = "";//所有搜索参数初始化
		String dateTimeProp = "";//时间字段参数
		String specialSearchData = "";//特殊搜索data
		String specialCreated = "";//特殊搜索create周期
		String specialMethod = ""; //特殊搜索方法
		String searchFlag = "";  //搜索data标记


		//根据label名字最长的来显示 表单的 label-width
		Integer maxLength = queryModel.stream().map(x -> x.getName().length()).max(Integer::max).get();
		String labelWidth = maxLength == 0 || (maxLength * 13) < 60 ? "60px" : (maxLength * 13) + "px";


		//启用的查询配置  大于3  就会有展开折叠 按钮
		if (queryModel.stream().filter(x -> x.getIsDate()).collect(Collectors.toList()).size() > 3)
		{
			sb.append("        <div slot=\"search\" style=\"width:100%\">\r\n");
			sb.append("              <!-- 在此处放置搜索内容 -->\r\n");
			sb.append("             <div :class=\"[!searchFlag ? 'searchBox' : '']\">\r\n");
			sb.append("                <el-form label-width=\""+ labelWidth + "\">\r\n");
			sb.append("                    <el-row :gutter=\"20\">\r\n");

			int num = 0;
			for(QueryDataDto item : queryModel)
			{

				//如果是第三条 默认加一串template
				if (num == 3)
				{
					sb.append("                 <template v-if=\"searchFlag\">\r\n");
				}

				//判断是否启用  不启用就不用添加
				if (item.getIsDate())
				{
					if (StringUtil.equals(item.getSearchtype(), "时间查询"))
					{
						sb.append("                  <el-col :span=\"8\">\r\n");
						sb.append("                       <el-form-item  label=\"" + item.getName() + "\">\r\n");
						sb.append("                          <el-date-picker size=\"small\"  type=\"daterange\" value-format=\"yyyy-MM-dd\" v-model=\"" + item.getDateField() + "_Date\"   style=\"width:100%\" range-separator=\"至\" start-placeholder=\"开始日期\" end-placeholder=\"结束日期\" @change=\"" + item.getDateField() + "_DateChange\"> </el-date-picker>\r\n");
						sb.append("                       </el-form-item>\r\n");
						sb.append("                  </el-col>\r\n");

						dataTimeMethod += "                   " + item.getDateField() + "_DateChange(val){\r\n";
						dataTimeMethod += "                       this.searchParam." + item.getDateField() + "_Start = val[0];\r\n";
						dataTimeMethod += "                       this.searchParam." + item.getDateField() + "_End = val[1];\r\n";
						dataTimeMethod += "                   }\r\n";

						//拼接时间查询变量
						dateTimeProp += "                   " + item.getDateField() + "_Date : [],";

						//拼接清理查询字段字符串
						searchReset += "                       this.searchParam." + item.getDateField() + "_Start = null;\r\n";
						searchReset += "                       this.searchParam." + item.getDateField() + "_End = null;\r\n";
						searchReset += "                       this.searchParam." + item.getDateField() + "_Date = null;\r\n";

						//拼接初始化查询字段字符串
						searchInit += "                       " + item.getDateField() + "_Start: '',\r\n";
						searchInit += "                       " + item.getDateField() + "_End: '',\r\n";
						searchInit += "                       " + item.getDateField() + "_Date: '',\r\n";

					}
					else
					{
						//所有需要特殊处理的组件
						List<String> specialComponent = Arrays.asList(
						new String[]{"el-select",
									"el-radio-group",
									"el-checkbox-group",
									"el-switch",
									"el-rate"});
						// 从组件配置里面找到 当前查询绑定的字段相同的组件  并且 是属于特殊组件中的一个
						FormDataDto component = null;
						for (FormDataDto formDataDto : formDataList) {
							if (StringUtils.equalsIgnoreCase(MapUtils.getString(formDataDto.getConfig(), FormConstants.BIND_TABLE_FIELD), item.getField())
									&& specialComponent.contains(MapUtils.getString(formDataDto.getConfig(), "tag"))) {
								component = formDataDto;
								break;
							}
						}

						sb.append("                  <el-col :span=\"8\">\r\n");
						sb.append("                       <el-form-item  label=\"" + item.getName() + "\">\r\n");
						//如果不属于特殊处理组件 直接用input就行了
						if (component == null)
						{


							sb.append("                  <el-input v-model=\"searchParam." + item.getField() + "\" size=\"small\" placeholder=\"请输入" + item.getName() + "要查询的关键字\"   style=\"width:100%\" ></el-input>\r\n");//width: 200px
						}
						else
						{


							specialSearchData += String.format("             %1$s_Option : [],", MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD));

							specialCreated += String.format("                this.get%1$s_Option();", MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD));

							specialMethod += String.format("          get%1$s_Option(){ \r\n", MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD));


							//如果是数据源
							if (StringUtil.equalsIgnoreCase(MapUtils.getString(component.getConfig(), "dataType"), "dataSource"))
							{
								sb.append("                   <el-select v-model=\"searchParam." + item.getField() + "\" filterable clearable style=\"width:100%\"  placeholder=\"请选择\">\r\n");//width: 200px
								sb.append("                      <el-option  v-for=\"(item,index) in " + MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD) + "_Option\"  :key=\"index\" :label=\"item." + MapUtils.getString(component.getConfig(), FormConstants.KEY_OF_DATA_SOURCE_SHOW_FIELD) + "\" :value=\"item." + MapUtils.getString(component.getConfig(), FormConstants.KEY_OF_DATA_SOURCE_SAVE_FIELD) + "\" >  </el-option>\r\n");//width: 200px
								sb.append("                   </el-select> \r\n");//width: 200px

								specialMethod += "             request({ \r\n";
								specialMethod += String.format("                 url : baseUrl + '/data-sources/%1$s/data', \r\n", MapUtils.getString(component.getConfig(), "dataSource"));
								specialMethod += "                 method : 'get', \r\n";
								specialMethod += "             }).then(res => { \r\n";
								specialMethod += String.format("                 this.%1$s_Option = res.data.data \r\n", MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD));
								specialMethod += "             }) \r\n";
							}
							else //如果是数据字典
							{
								sb.append("                   <el-select v-model=\"searchParam." + item.getField() + "\" filterable clearable style=\"width:100%\"   placeholder=\"请选择\">\r\n");//width: 200px
								sb.append("                      <el-option  v-for=\"(item,index) in " + MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD) + "_Option\"  :key=\"index\" :label=\"item.F_ItemName\"  :value=\"item.F_ItemValue\" >  </el-option>\r\n");//width: 200px
								sb.append("                   </el-select> \r\n");//width: 200px

								specialMethod += "             request({ \r\n";
								specialMethod += String.format("                 url : baseUrl + '/data-items/%1$s/detail', \r\n", MapUtils.getString(component.getConfig(), "dataItem"));
								specialMethod += "                 method : 'get', \r\n";
								specialMethod += "             }).then(res => { \r\n";
								specialMethod += String.format("                 this.%1$s_Option = res.data.data \r\n", MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD));
								specialMethod += "             }) \r\n";
							}

							specialMethod += "          }, \r\n";
						}

						sb.append("                       </el-form-item>\r\n");
						sb.append("                  </el-col>\r\n");

						searchReset += "                       this.searchParam." + item.getField() + " = null;\r\n";

						searchInit += "                       " + item.getField() + ": '',\r\n";

					}
					//拼接清理查询字段字符串


					num++;
				}

			}
			sb.append("                 </template>\r\n"); //超过3种查询 必须加


			sb.append("                  </el-row>\r\n");
			sb.append("                </el-form>\r\n");
			sb.append("        </div>\r\n");

			sb.append("                  <div style=\"float:right; margin-top:6px; \">\r\n");

			sb.append("                     <el-button type=\"primary\" icon=\"el-icon-search\" size=\"small\" @click=\"searchClick\">搜索</el-button>\r\n");
			sb.append("                     <el-button icon=\"el-icon-refresh\" size=\"small\" @click=\"searchReset\">重置</el-button>\r\n");
			sb.append("                     <el-link :underline=\"false\"  @click=\"() => searchFlag = !searchFlag\"  style=\"margin-left:10px\"  >{{ searchFlag ? \"收起\" : \"展开\" }}<i :class=\"[!searchFlag ? 'el-icon-arrow-down' : 'el-icon-arrow-up']\"></i></el-link>\r\n");

			sb.append("                  </div>\r\n");


			searchFlag = "                   searchFlag: false,\r\n";
		}
			else
		{


			sb.append("        <div slot=\"search\" style=\"width:100%\">\r\n");
			sb.append("              <!-- 在此处放置搜索内容 -->\r\n");
			sb.append("             <div class=\"searchBox\">\r\n");
			sb.append("                <el-form label-width=\"" + labelWidth + "\">\r\n");
			sb.append("                    <el-row :gutter=\"20\">\r\n");

			for (QueryDataDto item : queryModel)
			{
				//判断是否启用  不启用就不用添加
				if (item.getIsDate())
				{
					if (StringUtil.equalsIgnoreCase(item.getSearchtype(), "时间查询"))
					{
						sb.append("                  <el-col :span=\"8\">\r\n");
						sb.append("                       <el-form-item  label=\"" + item.getName() + "\">\r\n");
						sb.append("                          <el-date-picker size=\"small\"  type=\"daterange\" value-format=\"yyyy-MM-dd\" v-model=\"" + item.getDateField() + "_Date\"   style=\"width:100%\" range-separator=\"至\" start-placeholder=\"开始日期\" end-placeholder=\"结束日期\" @change=\"" + item.getDateField() + "_DateChange\"> </el-date-picker>\r\n");
						sb.append("                       </el-form-item>\r\n");
						sb.append("                  </el-col>\r\n");

						dataTimeMethod += "                   " + item.getDateField() + "_DateChange(val){\r\n";
						dataTimeMethod += "                       this.searchParam." + item.getDateField() + "_Start = val[0];\r\n";
						dataTimeMethod += "                       this.searchParam." + item.getDateField() + "_End = val[1];\r\n";
						dataTimeMethod += "                   }\r\n";

						//拼接时间查询变量
						dateTimeProp += "                   " + item.getDateField() + "_Date : [],";

						//拼接清理查询字段字符串
						searchReset += "                       this.searchParam." + item.getDateField() + "_Start = null;\r\n";
						searchReset += "                       this.searchParam." + item.getDateField() + "_End = null;\r\n";
						searchReset += "                       this.searchParam." + item.getDateField() + "_Date = null;\r\n";

						//拼接初始化查询字段字符串
						searchInit += "                       " + item.getDateField() + "_Start: '',\r\n";
						searchInit += "                       " + item.getDateField() + "_End: '',\r\n";
						searchInit += "                       " + item.getDateField() + "_Date: '',\r\n";

					}
					else
					{
						//所有需要特殊处理的组件
						List<String> specialComponent = Arrays.asList(
						new String[]{
							"el-select",
									"el-radio-group",
									"el-checkbox-group",
									"el-switch",
									"el-rate"
						});

						// 从组件配置里面找到 当前查询绑定的字段相同的组件  并且 是属于特殊组件中的一个
						FormDataDto component = null;
						for (FormDataDto formDataDto : formDataList) {
							if (StringUtils.equalsIgnoreCase(MapUtils.getString(formDataDto.getConfig(), FormConstants.BIND_TABLE_FIELD), item.getField())
									&& specialComponent.contains(MapUtils.getString(formDataDto.getConfig(), "tag"))) {
								component = formDataDto;
								break;
							}
						}
						sb.append("                  <el-col :span=\"8\">\r\n");
						sb.append("                       <el-form-item  label=\"" + item.getName() + "\">\r\n");
						//如果不属于特殊处理组件 直接用input就行了
						if (component == null)
						{


							sb.append("                  <el-input v-model=\"searchParam." + item.getField() + "\" size=\"small\" placeholder=\"请输入" + item.getName() + "要查询的关键字\"   style=\"width:100%\" ></el-input>\r\n");//width: 200px
						}
						else
						{

							specialSearchData += String.format("             %1$s_Option : [],", MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD));

							specialCreated += String.format("                this.get%1$s_Option();", MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD));

							specialMethod += String.format("          get%1$s_Option(){ \r\n", MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD));



							//如果是数据源
							if (StringUtil.equalsIgnoreCase(MapUtils.getString(component.getConfig(), "dataType"), "dataSource"))
							{
								sb.append("                   <el-select v-model=\"searchParam." + item.getField() + "\" filterable clearable style=\"width:100%\"  placeholder=\"请选择\">\r\n");//width: 200px
								sb.append("                      <el-option  v-for=\"(item,index) in " + MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD) + "_Option\"  :key=\"index\" :label=\"item." + MapUtils.getString(component.getConfig(), FormConstants.KEY_OF_DATA_SOURCE_SHOW_FIELD) + "\"  :value=\"item." + MapUtils.getString(component.getConfig(), FormConstants.KEY_OF_DATA_SOURCE_SAVE_FIELD) + "\" >  </el-option>\r\n");//width: 200px
								sb.append("                   </el-select> \r\n");//width: 200px

								specialMethod += "             request({ \r\n";
								specialMethod += String.format("                 url : baseUrl + '/data-sources/%1$s/data', \r\n", MapUtils.getString(component.getConfig(), "dataSource"));
								specialMethod += "                 method : 'get', \r\n";
								specialMethod += "             }).then(res => { \r\n";
								specialMethod += String.format("                 this.%1$s_Option = res.data.data \r\n", MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD));
								specialMethod += "             }) \r\n";
							}
							else //如果是数据字典
							{
								sb.append("                   <el-select v-model=\"searchParam." + item.getField() + "\" filterable clearable style=\"width:100%\"  placeholder=\"请选择\">\r\n");//width: 200px
								sb.append("                      <el-option  v-for=\"(item,index) in " + MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD) + "_Option\"  :key=\"index\" :label=\"item.F_ItemName\" :value=\"item.F_ItemValue\" >  </el-option>\r\n");//width: 200px
								sb.append("                   </el-select> \r\n");//width: 200px

								specialMethod += "             request({ \r\n";
								specialMethod += String.format("                 url : baseUrl + '/data-items/%1$s/detail', \r\n", MapUtils.getString(component.getConfig(), "dataItem"));
								specialMethod += "                 method : 'get', \r\n";
								specialMethod += "             }).then(res => { \r\n";
								specialMethod += String.format("                 this.%1$s_Option = res.data.data \r\n", MapUtils.getString(component.getConfig(), FormConstants.BIND_TABLE_FIELD));
								specialMethod += "             }) \r\n";
							}

							specialMethod += "          }, \r\n";
						}

						sb.append("                       </el-form-item>\r\n");
						sb.append("                  </el-col>\r\n");

						searchReset += "                       this.searchParam." + item.getField() + " = null;\r\n";

						searchInit += "                       " + item.getField() + ": '',\r\n";

					}
					//拼接清理查询字段字符串
				}
			}


			sb.append("                  </el-row>\r\n");
			sb.append("                </el-form>\r\n");
			sb.append("        </div>\r\n");

			if (queryModel.size() > 0)
			{
				sb.append("                  <div style=\"float:right; margin-top:6px; \">\r\n");

				sb.append("                     <el-button type=\"primary\" icon=\"el-icon-search\" size=\"small\" @click=\"searchClick\">搜索</el-button>\r\n");
				sb.append("                     <el-button icon=\"el-icon-refresh\" size=\"small\" @click=\"searchReset\">重置</el-button>\r\n");

				sb.append("                  </div>\r\n");

			}

		}




		sb.append("        </div>\r\n");


		//--------------------------------查询结束   列表开始----------------------------
		sb.append("        <div slot=\"table\">\r\n");
		sb.append("        <!-- 在此处放置表格内容 -->\r\n");


		String batchDeleteOptionStr = StringPool.EMPTY;
		String batchDeletCrudStr = StringPool.EMPTY;
		String batchDeleteMethodStr = StringPool.EMPTY;
		//判断是否有批量删除按钮
		if (colModel.getButtonList().stream().anyMatch(x -> MapUtils.getBoolean(x, "checked") && StringUtil.equalsIgnoreCase(MapUtils.getString(x, "val"), "batchDelete")))
		{
			batchDeleteOptionStr = "                       selection: true,\r\n";
			batchDeletCrudStr = "@selection-change=\"selectionChange\"";

			batchDeleteMethodStr += "                           selectionChange(list){\r\n";
			batchDeleteMethodStr += "                             this.ids = list.map((x) => x." + parentTable.getPk() + ").join(',');\r\n";
			batchDeleteMethodStr += "                           },\r\n";

			batchDeleteMethodStr += "                           batchDelete() {\r\n";


			batchDeleteMethodStr += "                            if (!this.ids || this.ids.split(\",\").length === 0) {\r\n";
			batchDeleteMethodStr += "                                 this.$message.error(\"未选择项目！\");\r\n";
			batchDeleteMethodStr += "                                 return;\r\n";
			batchDeleteMethodStr += "                           }\r\n";

			batchDeleteMethodStr += "                             this.$confirm(\"确定要批量删除所选项吗？\", \"提示\", {\r\n";
			batchDeleteMethodStr += "                               confirmButtonText: \"确定\",\r\n";
			batchDeleteMethodStr += "                               cancelButtonText: \"取消\",\r\n";
			batchDeleteMethodStr += "                               type: \"warning\",\r\n";
			batchDeleteMethodStr += "                             }).then(() => {\r\n";
			batchDeleteMethodStr += "                               request({\r\n";
			batchDeleteMethodStr += "                                 url: baseUrl + \"/" + bModel.getName() + "/\" + this.ids,\r\n";
			batchDeleteMethodStr += "                                 method: \"delete\",\r\n";
			batchDeleteMethodStr += "                               }).then((res) => {\r\n";
			batchDeleteMethodStr += "                                 this.getListData();\r\n";
			batchDeleteMethodStr += "                                 this.$notify({\r\n";
			batchDeleteMethodStr += "                                   title: \"成功\",\r\n";
			batchDeleteMethodStr += "                                   message: \"删除成功\",\r\n";
			batchDeleteMethodStr += "                                   type: \"success\",\r\n";
			batchDeleteMethodStr += "                                 });\r\n";
			batchDeleteMethodStr += "                               });\r\n";
			batchDeleteMethodStr += "                             });\r\n";
			batchDeleteMethodStr += "                           },\r\n";
		}

		if(StringUtil.equalsIgnoreCase(colModel.getIsPage(), "1"))
		{
			sb.append("            <avue-crud ref=\"" + bModel.getName() + "Table\" :data=\"listData\" "+ batchDeletCrudStr + " id=\"printTable\" :option=\"option\" stripe :page.sync=\"page\"\r\n");

			sb.append("                       @on-load=\"onPageLoad\"\r\n");
			sb.append("                       @sort-change=\"sortChange\" >\r\n");
		}
		else
		{
			sb.append("            <avue-crud ref=\"" + bModel.getName() + "Table\" :data=\"listData\" " + batchDeletCrudStr + "  id=\"printTable\" :option=\"option\" stripe \r\n");

			sb.append("                       @sort-change=\"sortChange\">\r\n");
		}


		sb.append("                <template slot-scope=\"scope\" slot=\"menu\">\r\n");
		sb.append("                    <el-button type=\"opr-primary\" size=\"mini\"  icon=\"el-icon-view\"  v-if=\"hasBtns('view')&&hasButtonPermission('view')\" @click=\"viewForm(scope.row)\">查看</el-button>\r\n");
		sb.append("                    <el-button type=\"opr-primary\" size=\"mini\"  icon=\"el-icon-edit\" v-if=\"hasBtns('modify')&&hasButtonPermission('modify')\" @click=\"editForm(scope.row)\">编辑</el-button>\r\n");
		if(isWorkflow){
			sb.append("                   <el-button type=\"opr-primary\" size=\"mini\"  icon=\"el-icon-guide\" v-if=\"hasBtns('createFlow')&&hasButtonPermission('createFlow')\" @click=\"createFlow(scope.row)\">发起流程</el-button>\r\n");
		}
		sb.append("                    <el-button type=\"opr-primary\" size=\"mini\"  icon=\"el-icon-delete\" v-if=\"hasBtns('delete')&&hasButtonPermission('delete')\" @click=\"deleteForm(scope.row)\">删除</el-button>\r\n");
		sb.append("                </template>\r\n");


		//生成按钮
		sb.append("                 <template slot-scope=\"scope\" slot=\"menuLeft\">\r\n");
		if (colModel.getButtonList().size() > 0)
		{
			for(Map<String, Object> btn : colModel.getButtonList())
			{
				if(MapUtils.getBoolean(btn, "checked"))
				{
					switch(MapUtils.getString(btn, "val"))
					{
						case "add":
							sb.append("                     <el-button type=\"primary\"  v-if=\"hasButtonPermission('" + MapUtils.getString(btn, "val") + "')\" icon=\"" + MapUtils.getString(btn, "icon") + "\" size=\"small\" @click=\"openForm()\">新增</el-button>\r\n");
							break;
						case "batchDelete":
							sb.append("                     <el-button type=\"primary\"  v-if=\"hasButtonPermission('" + MapUtils.getString(btn, "val") + "')\" icon=\"" + MapUtils.getString(btn, "icon") + "\" size=\"small\" @click=\"batchDelete()\">批量删除</el-button>\r\n");
							break;
						case "print":
							sb.append("                     <el-button type=\"primary\"  v-if=\"hasButtonPermission('" + MapUtils.getString(btn, "val") + "')\" icon=\"" + MapUtils.getString(btn, "icon") + "\" size=\"small\" @click=\"'#printForm'\">打印</el-button>\r\n");
							break;
						case "import":
							sb.append("                     <el-button type=\"primary\"  v-if=\"hasButtonPermission('" + MapUtils.getString(btn, "val") + "')\" icon=\"" + MapUtils.getString(btn, "icon") + "\" size=\"small\" @click=\"excelDialog = true\">快速导入</el-button>\r\n");
							break;
						case "export":
							sb.append("                     <el-button type=\"primary\"  v-if=\"hasButtonPermission('" + MapUtils.getString(btn, "val") + "')\" icon=\"" + MapUtils.getString(btn, "icon") + "\" size=\"small\" @click=\"tableExport()\">快速导出</el-button>\r\n");
							break;
						case "modify":
						case "delete":
							break;
						default:
							sb.append("                     <el-button type=\"primary\"  v-if=\"hasButtonPermission('" + MapUtils.getString(btn, "val") + "')\" icon=\"" + MapUtils.getString(btn, "icon") + "\" size=\"small\">" + MapUtils.getString(btn, "name") + "</el-button>\r\n");
							break;
					}
				}
			}
		}
		sb.append("                </template>\r\n");


		sb.append("            </avue-crud>\r\n");

		//--------------------------------表单----------------------------

		switch(bModel.getFormSize())
		{
			case "fullscreen": //全屏
				sb.append("            <el-dialog title=\"表单\" :append-to-body=\"true\" :fullscreen=\"true\" :visible.sync=\"flag\"  :class=\"isWorkflowForm ? 'work-form-dialog' : ''\" :show-close=\"!isWorkflowForm\" >\r\n");
				sb.append("                <"+bModel.getName()+"Form ");
				if(isWorkflow){
					sb.append("ref=\"xtForm\"  ");
				}
				sb.append("v-if=\"flag\"  :isEdit=\"isEdit\" :disabled=\"disabled\" @onChange=\"dialogChange\" :keyValue=\"keyValue\"></" + bModel.getName() + "Form>\r\n");
				sb.append("            </el-dialog>\r\n");
				break;
			case "medium": //大
				sb.append("            <el-dialog title=\"表单\" :append-to-body=\"true\" width=\"65%\" :visible.sync=\"flag\" :class=\"isWorkflowForm ? 'work-form-dialog' : ''\" :show-close=\"!isWorkflowForm\">\r\n");
				sb.append("                <"+bModel.getName()+"Form ");
				if(isWorkflow){
					sb.append("ref=\"xtForm\"  ");
				}
				sb.append("v-if=\"flag\"  :isEdit=\"isEdit\" :disabled=\"disabled\" @onChange=\"dialogChange\" :keyValue=\"keyValue\"></" + bModel.getName() + "Form>\r\n");
				sb.append("            </el-dialog>\r\n");
				break;
			case "small": //中
				sb.append("            <el-dialog title=\"表单\" :append-to-body=\"true\" width=\"50%\" :visible.sync=\"flag\" :class=\"isWorkflowForm ? 'work-form-dialog' : ''\" :show-close=\"!isWorkflowForm\">\r\n");
				sb.append("                <"+bModel.getName()+"Form ");
				if(isWorkflow){
					sb.append("ref=\"xtForm\"  ");
				}
				sb.append("v-if=\"flag\"  :isEdit=\"isEdit\" :disabled=\"disabled\" @onChange=\"dialogChange\" :keyValue=\"keyValue\"></" + bModel.getName() + "Form>\r\n");
				sb.append("            </el-dialog>\r\n");
				break;
			case "mini": //小
				sb.append("            <el-dialog title=\"表单\" :append-to-body=\"true\" width=\"35%\" :visible.sync=\"flag\" :class=\"isWorkflowForm ? 'work-form-dialog' : ''\" :show-close=\"!isWorkflowForm\">\r\n");
				sb.append("                <"+bModel.getName()+"Form ");
				if(isWorkflow){
					sb.append("ref=\"xtForm\"  ");
				}
				sb.append("v-if=\"flag\"  :isEdit=\"isEdit\" :disabled=\"disabled\" @onChange=\"dialogChange\" :keyValue=\"keyValue\"></" + bModel.getName() + "Form>\r\n");
				sb.append("            </el-dialog>\r\n");
				break;
			default:  //默认全屏
				sb.append("            <el-dialog title=\"表单\" :append-to-body=\"true\" :fullscreen=\"true\" :visible.sync=\"flag\" :class=\"isWorkflowForm ? 'work-form-dialog' : ''\" :show-close=\"!isWorkflowForm\">\r\n");
				sb.append("                <"+bModel.getName()+"Form ");
				if(isWorkflow){
					sb.append("ref=\"xtForm\"  ");
				}
				sb.append("v-if=\"flag\"  :isEdit=\"isEdit\" :disabled=\"disabled\" @onChange=\"dialogChange\" :keyValue=\"keyValue\"></" + bModel.getName() + "Form>\r\n");
				sb.append("            </el-dialog>\r\n");
				break;

		}

		//--------------------------------表单结束----------------------------

		//--------------------------------导入----------------------------
		//如果有导出按钮
		if (colModel.getButtonList().stream().anyMatch(x -> MapUtils.getBoolean(x, "checked") && StringUtil.equalsIgnoreCase(MapUtils.getString(x, "val"), "import")))
		{
			sb.append("      <el-dialog title=\"快速导入\" width=\"30%\" :append-to-body=\"true\" :visible.sync=\"excelDialog\">\r\n");
			sb.append("        <el-upload\r\n");
			sb.append("           class=\"upload-demo\"\r\n");
			sb.append("           ref=\"upload\"\r\n");
			sb.append("           :data=\"uploadData\"\r\n");
			sb.append("           :before-upload=\"beforeUpload\"\r\n");
			sb.append("           :on-success=\"successUpload\"\r\n");
			sb.append("           :show-file-list=\"true\"\r\n");
			sb.append("           drag\r\n");
			sb.append("           :headers=\"headers\"\r\n");
			sb.append("           accept=\".csv,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\"\r\n");
			sb.append("           :action=\"baseUrl + '/" + bModel.getName().toLowerCase() + "/import'\">\r\n");

			sb.append("           <i class=\"el-icon-upload\"></i>\r\n");
			sb.append("           <div class=\"el-upload__text\">将文件拖到此处，或<em>点击上传</em></div>\r\n");
			sb.append("           <div class=\"el-upload__tip\" slot=\"tip\">只能上传Excel文件，且不超过1G</div>\r\n");
			sb.append("        </el-upload>\r\n");


			sb.append("         <el-row type=\"flex\">\r\n");
			sb.append("           <el-col>\r\n");
			sb.append("           </el-col>\r\n");
			sb.append("           <el-col style=\"text-align:right;\">\r\n");
			sb.append("            <p></p>\r\n");
			sb.append("            <el-button icon=\"el-icon-download\" @click=\"download\"> 下载模板</el-button>\r\n");
			sb.append("           </el-col>\r\n");
			sb.append("         </el-row>\r\n");
			sb.append("     </el-dialog>\r\n");
		}
		//--------------------------------导入结束----------------------------


		sb.append("        </div>\r\n");
		//--------------------------------列表结束----------------------------
		sb.append("    </main-content>\r\n");
		if(isWorkflow){
			sb.append("    <createFlow v-if=\"isCreateFlow\" :row=\"scopeRow\" :scheme=\"scheme\" @closedCreateFlow=\"closedCreateFlow\"></createFlow>\r\n");
		}




		sb.append("    </div>\r\n");
		sb.append("</template>\r\n");


		//--------------------------------JS开始----------------------------
		sb.append("<script>\r\n");
        if(isWorkflow){
        	sb.append("      import createFlow from '@/components/formInitiationProcess/createFlow';\r\n");
        	sb.append("      import schemeMixins from '@/components/formInitiationProcess/schemeMixins';\r\n");
		}
		if (queryModel.size() > 0)
		{
			sb.append("     import MainFixed from \"@/page/main/MainFixed\";\r\n");
		}
		sb.append("      import authorizeMixin from \"@/mixins/authorize\";\r\n");
		sb.append("      import tableOptionMixin from \"@/mixins/tableOption\"; //table 高度计算\r\n");


		sb.append("     import MainContent from \"@/page/main/MainContent\";\r\n");

		sb.append("      import request from '@/router/axios';\r\n");
		sb.append("      import { baseUrl } from '@/config/env';\r\n");
		sb.append("      import { getOrder } from '@/util/util';\r\n");

		////如果有导出按钮
		//if (colModel.Btns.Exists(x=>x.Checked && x.Val == "export"))
		//{
		//    sb.append("      import { saveAs } from \"file-saver\";\r\n");
		//    sb.append("      import XLSX from \"xlsx\";\r\n");
		//}


		//引入表单页
		sb.append("      import " + bModel.getName() + "Form from './" + bModel.getName() + "Form';\r\n");




		sb.append("     export default {\r\n");
		sb.append("         name: \"" + bModel.getName() + "\",\r\n");
		sb.append("         mixins: [authorizeMixin,tableOptionMixin");
		if(isWorkflow){
			sb.append(",schemeMixins");
		}
		sb.append("],\r\n");
		sb.append("         components: { MainContent, MainFixed," + bModel.getName() + "Form");
		if(isWorkflow){
			sb.append(",createFlow");
		}
		sb.append(" },\r\n");
		sb.append("         data() {\r\n");
		sb.append("              return {\r\n");

		sb.append("computedHeight:50,");
		sb.append("				 	  btns:[\r\n");
		if (colModel.getButtonList().size() > 0) {
			int index = 0;
			for (Map<String, Object> btn : colModel.getButtonList()) {
				if (!MapUtils.getBoolean(btn, "checked")) {
					continue;
				}
				if (index > 0) {
					sb.append(",\r\n");
				}
				sb.append("				 	     '" + btn.get("val") + "'");
				index++;
			}
		}
		sb.append("\r\n");
		sb.append("                   ],\r\n");

		sb.append("                   contentTitle: '" + bModel.getDescribe() + "列表',\r\n");

		if (colModel.getIsTree() > 0)
		{

			sb.append("                   treeTitle: '" + MapUtils.getString(colModel.getTreeForm(), "treeTitleName") + "',\r\n");
			sb.append("                   treeData: [],\r\n");
			sb.append("                   treeSelect: '',//左边树所选\r\n");
		}
		if(StringUtil.equalsIgnoreCase(colModel.getIsPage(), "1"))
		{
			sb.append("                   page: {\r\n");
			sb.append("                       pageSize: " + colModel.getPageSize() + ",\r\n");
			sb.append("                       total: 0,\r\n");
			sb.append("                       currentPage: 1\r\n");
			sb.append("                   },\r\n");
		}
		if(colModel.getButtonList().stream().anyMatch(x -> MapUtils.getBoolean(x, "checked") && StringUtil.equalsIgnoreCase(MapUtils.getString(x, "val"), "batchDelete")))
		sb.append("                   ids: '',\r\n");

		sb.append(dateTimeProp);

		sb.append("                   listData: [],\r\n");
		sb.append("                   keyValue: '',\r\n");

		sb.append("                   flag:false,\r\n");

		sb.append("                   isEdit: true, // 查看-不显示按钮,\r\n");
		sb.append("                   disabled: false, // 查看-禁用input,\r\n");
		sb.append("                   isWorkflowForm:false,//默认值 false\r\n");

		sb.append("                   searchParam: {");
		sb.append(searchInit);
		sb.append("},\r\n");

		sb.append(searchFlag);

		sb.append("                   defaultProps: {\r\n");
		sb.append("                       children: 'children',\r\n");
		sb.append("                       label: '" + MapUtils.getString(colModel.getTreeForm(), "treefieldShow") + "'\r\n");
		sb.append("                   },\r\n");

		//如果有导出按钮
		if (colModel.getButtonList().stream().anyMatch(x -> MapUtils.getBoolean(x, "checked") && StringUtil.equalsIgnoreCase(MapUtils.getString(x, "val"), "import")))
		{
			sb.append("                    uploadData: {  F_ModuleId: '' },\r\n");
			sb.append("                    headers: {   },\r\n");
			sb.append("                    excelDialog: false,\r\n");
		}



		sb.append(specialSearchData);
		sb.append("                   option:{\r\n");
		sb.append("                       rowKey:'"+ parentTable.getPk() + "',\r\n");
		sb.append("                       highlightCurrentRow: true,\r\n");
		sb.append("                       stripe: true,\r\n");
		sb.append("                       addBtn: false,\r\n");
		sb.append("                       menuWidth:");
		if(isWorkflow){
			sb.append(" 280,\r\n");
		}else {
			sb.append(" 230,\r\n");
		}
//		sb.append("                       maxHeight: 550,\r\n");
		sb.append("                       border: true,\r\n");

		//是否分页
		if(StringUtil.equalsIgnoreCase(colModel.getIsPage(), "1"))
		{
			sb.append("                       page: true,\r\n");
		}
		else
		{
			sb.append("                       page: false,\r\n");
		}

		//是否有合计配置
		if(totalData.size() > 0)
		{
			sb.append("                       showSummary: false,\r\n");
			sb.append("                       sumColumnList: [\r\n");
			for(TotalDataDto item : totalData)
			{
				sb.append("                         {\r\n");

				//如果有设置合计label
				if(!StringUtil.isEmpty(item.getLabel()))
				{
					sb.append("                            label : '" + item.getLabel() + ": ',\r\n");
				}
				sb.append("                            name : '" + item.getName() + "',\r\n");
				sb.append("                            type : '" + item.getType() + "',\r\n");

				sb.append("                         },\r\n");
			}
			sb.append("                       ],\r\n");
		}

		sb.append(batchDeleteOptionStr);
		sb.append("                       index: true,\r\n");
		sb.append("                       indexLabel: '序号',\r\n");
		sb.append("                       align: 'center',\r\n");
		sb.append("                       menuAlign: 'center',\r\n");
		sb.append("                       delBtn: false,\r\n");
		sb.append("                       editBtn: false,\r\n");
		sb.append("                       column: [\r\n");


		for(Map<String, Object> item : colModel.getColumnList())
		{
			sb.append("                           {\r\n");
			sb.append("                               sortable: true,\r\n");
			sb.append("                               label: '"+item.get("fieldName")+"',\r\n");
			sb.append("                               prop: '"+item.get("bindColumn")+"',\r\n");//width

			if(!StringUtil.isEmpty(item.get("width")))
			{
				sb.append("                               width: '" + item.get("width") + "'\r\n");
			}

			sb.append("                           },\r\n");
		}

		sb.append("                       ]\r\n");
		sb.append("                   }\r\n");



		sb.append("              }\r\n");




		sb.append("                   },\r\n");

		sb.append("                   created() {\r\n");

		if(colModel.getIsTree() == 1) //如果是树结构  默认执行左边树的方法
		{
			sb.append("                       this.getTreeData()\r\n");
		}
		else//如果不是树结构  直接请求获取列表数据
		{
			//如果不分页 需要自己请求  如果分页 前端组件会帮忙发送请求
			if (!StringUtil.equalsIgnoreCase(colModel.getIsPage(), "1"))
				sb.append("                       this.getListData()\r\n");
		}
		sb.append("                       /** 工作流  系统表单  相关方法 start*/\r\n");
		sb.append("                       let self = this;\r\n");
		sb.append("                       window.parent.openDialog = () => {\r\n");
		sb.append("                         self.isWorkflowForm = true;\r\n");
		sb.append("                         self.openForm();\r\n");
		sb.append("                       };\r\n");
		sb.append("                         /** 工作流 系统表单  相关方法 end */\r\n");

		sb.append(specialCreated);

		//如果有导出按钮
		if (colModel.getButtonList().stream().anyMatch(x -> MapUtils.getBoolean(x, "checked") && StringUtil.equalsIgnoreCase(MapUtils.getString(x, "val"), "import")))
		{
			sb.append("                      const token = JSON.parse(localStorage.getItem('avue-token'))['content'];\r\n");
			sb.append("                      this.headers.Authorization = 'Bearer ' + token\r\n");
			sb.append("                       this.uploadData = { F_ModuleId: this.$route.meta.moduleid };\r\n");
		}

		sb.append("                   },\r\n");


		//------------------method start-----------------------
		sb.append("                   methods: {\r\n");
		sb.append("                         printForm() {\r\n");
		sb.append("                         	let element = window.document.getElementById(\"printTable\");\r\n");
		sb.append("                         	setTimeout(() => {\r\n");
		sb.append("                         	  html2canvas(element, {\r\n");
		sb.append("                         		backgroundColor: null,\r\n");
		sb.append("                         		useCORS: true,\r\n");
		sb.append("                         		windowHeight: document.body.scrollHeight\r\n");
		sb.append("                         	  }).then(canvas => {\r\n");
		sb.append("                         		const url = canvas.toDataURL();\r\n");
		sb.append("                         		this.printImg = url;\r\n");
		sb.append("                         		printJS({\r\n");
		sb.append("                         		  printable: url,\r\n");
		sb.append("                         		  type: \"image\",\r\n");
		sb.append("                         		  documentTitle: '打印',\r\n");
		sb.append("                         		  scanStyles: false,\r\n");
		sb.append("                         		  repeatTableHeader: false,\r\n");
		sb.append("                         		  header: null\r\n");
		sb.append("                         		});\r\n");
		sb.append("                          	  });\r\n");
		sb.append("                         	}, 1);\r\n");
		sb.append("                           },\r\n");
		//添加是设置按钮方法
		sb.append("                         hasBtns(encode){\r\n");
		sb.append("                         	return this.btns.includes(encode);\r\n");
		sb.append("                         },\r\n");

		//如果发布流程则添加
		if(isWorkflow){
			sb.append("                         /**发起流程 */\r\n");
			sb.append("                         createFlow(val) {\r\n");
			sb.append("                            this.scopeRow = val;\r\n");
			sb.append("                            this.changeSystemSchemeInfo();\r\n");
			sb.append("                         },\r\n");
		}
		sb.append(specialMethod);

		//如果有导出按钮 新增导入按钮所需要方法
		if (colModel.getButtonList().stream().anyMatch(x -> MapUtils.getBoolean(x, "checked") && StringUtil.equalsIgnoreCase(MapUtils.getString(x, "val"), "import")))
		{
			sb.append("                         download(){\r\n");
			sb.append("                             request({\r\n");
			sb.append("                                 url: baseUrl + '/excel-import/download/' + this.$route.meta.moduleid,\r\n");
			sb.append("                                 method: 'get',\r\n");
			sb.append("                                 responseType: 'blob'\r\n");
			sb.append("                              }).then(res =>{                  \r\n");
			sb.append("                                    let url = window.URL.createObjectURL(new Blob([res.data],{type : \"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\"}));\r\n");
			sb.append("                                    let link = document.createElement('a');\r\n");
			sb.append("                                    link.style.display = 'none';\r\n");
			sb.append("                                    link.href = url;\r\n");
			sb.append("                                    link.setAttribute('download', '" + bModel.getName() + ".xlsx');\r\n");
			sb.append("                                     document.body.appendChild(link);\r\n");
			sb.append("                                     link.click();\r\n");
			sb.append("                                     document.body.removeChild(link);\r\n");
			sb.append("                             })\r\n");
			sb.append("                         },\r\n");

			sb.append("                         upload(){\r\n");
			sb.append("                            this.$refs['upload'].submit();\r\n");
			sb.append("                         },\r\n");

			//sb.append("                         beforeUpload () {\r\n");
			//sb.append("                             this.uploadData = { F_ModuleId: this.$route.meta.moduleid };\r\n");
			//sb.append("                         },\r\n");

			sb.append("                         successUpload (res) {\r\n");
			sb.append("                             if(res.code === 0)  {this.$message.success('导入完成！'); this.getListData(); } else this.$message.error(res.msg);\r\n");
			sb.append("                         },\r\n");
		}

		if (colModel.getIsTree() > 0)
		{

			sb.append("                        /* 获取树形菜单数据 */\r\n");
			sb.append("                        getTreeData(){\r\n");
			sb.append("                            request({\r\n");
			sb.append("                                url: baseUrl + '/data-sources/" + colModel.getTreeForm().get("treeSourceId") + "/tree',\r\n");
			sb.append("                                method: 'get',\r\n");

			if (!StringUtil.isEmpty(colModel.getTreeForm().get("treeSql")))
			{
				sb.append("                                params: { sql:'" + MapUtils.getString(colModel.getTreeForm(), "treeSql", "").replace("\r\n"," ") + "', field:'" + colModel.getTreeForm().get("treefieldId") + "',parentfield:'" + colModel.getTreeForm().get("treeParentId") + "' }\r\n");
			}
			else
			{
				sb.append("                                params: { field:'" + colModel.getTreeForm().get("treefieldId") + "',parentfield:'" + colModel.getTreeForm().get("treeParentId") + "'}\r\n");
			}

			sb.append("                            }).then(res =>{\r\n");
			sb.append("                                this.treeData = res.data.data\r\n");
			sb.append("                                this.$nextTick(function () {\r\n");
			sb.append("                                    this.$refs." + bModel.getName() + "Tree.setCurrentKey(this.treeData[0]?." + colModel.getTreeForm().get("treefieldId") + ");\r\n");
			sb.append("                                })\r\n");
			sb.append("                            })\r\n");
			sb.append("                        },\r\n");

			//树结构绑定的是数据源
			//if (!colModel.TreeForm.TreeSql.IsNull())
			//{
			//    sb.append("                        /* 获取树形菜单数据 */\r\n");
			//    sb.append("                        getTreeData(){\r\n");
			//    sb.append("                            request({\r\n");
			//    sb.append("                                url: baseUrl + '/data-sources/" + colModel.TreeForm.TreeSourceId + "/tree',\r\n");
			//    sb.append("                                method: 'get',\r\n");
			//    sb.append("                                params: { field:'" + colModel.TreeForm.TreefieldId + "',parentfield:'" + colModel.TreeForm.TreeParentId + "'}\r\n");
			//    sb.append("                            }).then(res =>{\r\n");
			//    sb.append("                                this.treeData = res.data.data\r\n");
			//    sb.append("                                this.$nextTick(function () {\r\n");
			//    sb.append("                                    this.$refs." + bModel.name + "Tree.setCurrentKey(this.treeData[0]." + colModel.TreeForm.TreefieldId + ");\r\n");
			//    sb.append("                                })\r\n");
			//    sb.append("                            })\r\n");
			//    sb.append("                        },\r\n");


			//}
			//else // 树结构如果用的是sql
			//{
			//    sb.append("                        /* 获取树形菜单数据 */\r\n");
			//    sb.append("                        getTreeData(){\r\n");
			//    sb.append("                            request({\r\n");
			//    sb.append("                                url: baseUrl + '/database-links/" + dbLinkId + "/tree',\r\n");
			//    sb.append("                                method: 'get',\r\n");
			//    sb.append("                                params: { sql:'"+colModel.TreeForm.TreeSql.Replace("\r\n"," ")+"', field:'" + colModel.TreeForm.TreefieldId + "',parentfield:'" + colModel.TreeForm.TreeParentId + "' }\r\n");
			//    sb.append("                            }).then(res =>{\r\n");
			//    sb.append("                                this.treeData = res.data.data\r\n");
			//    sb.append("                                this.$nextTick(function () {\r\n");
			//    sb.append("                                    this.$refs." + bModel.name + "Tree.setCurrentKey(this.treeData[0]." + colModel.TreeForm.TreefieldId + ");\r\n");
			//    sb.append("                                })\r\n");
			//    sb.append("                            })\r\n");
			//    sb.append("                        },\r\n");

			//}

			sb.append("                        /* 选择树加载列表数据 */\r\n");
			sb.append("                        handleNodeClick(data) {\r\n");
			sb.append("                            this.searchParam ={\r\n");
			sb.append("                                " + colModel.getTreeForm().get("treefieldId") + " : data." + colModel.getTreeForm().get("treefieldId") + "\r\n");
			sb.append("                            }\r\n");
			sb.append("                            this.treeSelect = data." + colModel.getTreeForm().get("treefieldId") + "\r\n");
			sb.append("                            this.getListData()\r\n");
			sb.append("                        },\r\n");

		}
		if (StringUtil.equalsIgnoreCase(colModel.getIsPage(), "1"))
		{
			//-------------------如果是分页的获取数据方法----------------------
			sb.append("                         /* 获取公司用户数据 */\r\n");
			sb.append("                         getListData() {\r\n");
			if (totalData.size() > 0) {
				sb.append("                                this.option.showSummary = false;\r\n");
			}
			sb.append("                            request({\r\n");
			sb.append("                                url: baseUrl + '/" + StringUtils.lowerCase(bModel.getName()) + "',\r\n");
			sb.append("                                method: 'get',\r\n");
			sb.append("                                params: this.searchParam\r\n");
			sb.append("                            }).then(res =>{\r\n");
			sb.append("                                this.listData = res.data.data.Rows\r\n");
			sb.append("                                this.page.total = res.data.data.Total;\r\n");
			if (totalData.size() > 0) {
				sb.append("                                this.option.showSummary = true;\r\n");
			}
			sb.append("                            })\r\n");
			sb.append("                         },\r\n");


			sb.append("                         /* 页面加载 */\r\n");
			sb.append("                         onPageLoad(page) {\r\n");
			sb.append("                            this.searchParam.limit = page.currentPage;\r\n");
			sb.append("                            this.searchParam.size = page.pageSize;\r\n");
			sb.append("                            this.getListData();\r\n");
			sb.append("                         },\r\n");




			sb.append("                          /* 查询 */\r\n");
			sb.append("                         searchClick() {\r\n");

			sb.append("                            this.searchParam.limit = 1\r\n");
			sb.append("                            this.searchParam.order = null\r\n");
			sb.append("                            this.searchParam.orderfield = null\r\n");
			sb.append("                            this.getListData()\r\n");
			sb.append("                         },\r\n");
		}
		else
		{
			//-------------------不分页的获取数据方法----------------------
			sb.append("                         /* 获取公司用户数据 */\r\n");
			sb.append("                         getListData() {\r\n");
			if (totalData.size() > 0) {
				sb.append("                                this.option.showSummary = false;\r\n");
			}
			sb.append("                            request({\r\n");
			sb.append("                                url: baseUrl + '/" + StringUtils.lowerCase(bModel.getName()) + "',\r\n");
			sb.append("                                method: 'get',\r\n");
			sb.append("                                params: this.searchParam\r\n");
			sb.append("                            }).then(res =>{\r\n");
			sb.append("                                this.listData = res.data.data\r\n");
			if (totalData.size() > 0) {
				sb.append("                                this.option.showSummary = true;\r\n");
			}
			sb.append("                            })\r\n");
			sb.append("                         },\r\n");





			sb.append("                          /* 查询 */\r\n");
			sb.append("                         searchClick() {\r\n");
			sb.append("                            this.searchParam.order = null\r\n");
			sb.append("                            this.searchParam.orderfield = null\r\n");
			sb.append("                            this.getListData()\r\n");
			sb.append("                         },\r\n");


		}

		sb.append("                          /* 查询表单重置 */\r\n");
		sb.append("                         searchReset() {\r\n");
		sb.append(searchReset);
		sb.append("                            this.getListData()\r\n");
		sb.append("                         },\r\n");


		sb.append("                          /*查看*/\r\n");
		sb.append("                         viewForm(val) {\r\n");
		sb.append("                           this.flag = true;\r\n");
		sb.append("                           this.isEdit = false;\r\n");
		sb.append("                           this.disabled = true;\r\n");
		sb.append("                           this.keyValue = val." + parentTable.getPk() + "; \r\n");
		sb.append("                         },\r\n");

		sb.append("                          /* 编辑 */\r\n");
		sb.append("                         editForm(val) {\r\n");
		sb.append("                           this.flag = true; \r\n");
		sb.append("                           this.isEdit = true;\r\n");
		sb.append("                           this.disabled = false;\r\n");
		sb.append("                           this.keyValue = val." + parentTable.getPk() + "; \r\n");
		sb.append("                         },\r\n");

		sb.append("                          /* 打开表单 */\r\n");
		sb.append("                         openForm(){\r\n");
		sb.append("							  if(!this.isWorkflowForm){\r\n");
		sb.append("                             //不处理工作流表单时候，表单类型重置\r\n");
		sb.append("                             window.localStorage.setItem('systemFormType',0);\r\n");
		sb.append("                           }\r\n");
		sb.append("                           this.keyValue = null; \r\n");
		sb.append("                           this.flag = true; \r\n");
		sb.append("                           this.isEdit = true; \r\n");
		sb.append("                         },\r\n");

		sb.append("                          /* 删除 */\r\n");
		sb.append("                         deleteForm(val) {\r\n");
		sb.append("                            this.$confirm(\"确定要删除此项吗？\", \"提示\", {\r\n");
		sb.append("                              confirmButtonText: \"确定\",\r\n");
		sb.append("                              cancelButtonText: \"取消\",\r\n");
		sb.append("                              type: \"warning\",\r\n");
		sb.append("                             }).then(() => {\r\n");
		sb.append("                                 request({\r\n");
		sb.append("                                     url: baseUrl + \"/" + StringUtils.lowerCase(bModel.getName()) + "/\" + val." + parentTable.getPk() + ",\r\n");
		sb.append("                                     method: \"delete\",\r\n");
		sb.append("                                 }).then((res) => {\r\n");
		sb.append("                                     this.getListData()\r\n");
		sb.append("                                     this.$notify({\r\n");
		sb.append("                                      title: \"成功\",\r\n");
		sb.append("                                      message: \"删除成功\",\r\n");
		sb.append("                                      type: \"success\",\r\n");
		sb.append("                                     });\r\n");
		sb.append("                                 });\r\n");
		sb.append("                             });\r\n");
		sb.append("                         },\r\n");

		sb.append("                          /* 分页 */\r\n");
		sb.append("                         sortChange(data) {\r\n");
		sb.append("                            this.searchParam.order = getOrder(data.order);\r\n");
		sb.append("                            this.searchParam.orderfield = data.prop;\r\n");
		sb.append("                            this.getListData()\r\n");
		sb.append("                         },\r\n");

		sb.append("                          /* 弹窗回调 */\r\n");
		sb.append("                          dialogChange(b){\r\n");
		sb.append("                              this.flag = false;\r\n");
		sb.append("                              this.$nextTick(()=>{\r\n");
		sb.append("                              this.getListData();\r\n");
		sb.append("                         })},\r\n");

		//导出
		if (colModel.getButtonList().stream().anyMatch(x -> MapUtils.getBoolean(x, "checked") && StringUtil.equalsIgnoreCase(MapUtils.getString(x, "val"), "export")))
		{
			sb.append("                          /* 导出 */\r\n");
			sb.append("                          tableExport(){\r\n");
			sb.append("                            this.$refs[\""+bModel.getName()+"Table\"].rowExcel()\r\n");
			sb.append("                            return;\r\n");
			sb.append("                         },\r\n");
		}


		sb.append(batchDeleteMethodStr);


		sb.append(dataTimeMethod);
		//---------------method over-----------------



		sb.append("         }\r\n");
		sb.append("     }\r\n");
		sb.append("</script>\r\n");

		sb.append("<style scoped>\r\n");

		//导入
		if (colModel.getButtonList().stream().anyMatch(x -> MapUtils.getBoolean(x, "checked") && StringUtil.equalsIgnoreCase(MapUtils.getString(x, "val"), "import")))
		{
			sb.append(">>> .el-upload{\r\n");
			sb.append("   width: 100%;\r\n");
			sb.append("}\r\n");
			sb.append(">>> .el-upload-dragger{\r\n");
			sb.append("   width: 100%;\r\n");
			sb.append("}\r\n");
		}

		sb.append(".searchBox {\r\n");
		sb.append("   width: calc(100% - 218px);\r\n");
		sb.append("   display: inline-block;\r\n");
		sb.append("}\r\n");

		sb.append("</style>\r\n");

		return sb.toString();
	}

	public Map<String, String> buildResultCode(Map<String, StringBuilder> resultCodeMap) {
		Map<String, String> resultMap = new HashMap<>(16);
		resultMap.put("controllerCode", MapUtils.getString(resultCodeMap, "/templates/controller.java.vm"));
		resultMap.put("dbConnectionCode", "");
		resultMap.put("dbFlagCode", "");
		resultMap.put("entityCode", MapUtils.getString(resultCodeMap, "/templates/entity.java.vm"));
		resultMap.put("formHTML", "");
		resultMap.put("forminputDtoCode", MapUtils.getString(resultCodeMap, "/templates/saveFormDataDto.java.vm"));
		resultMap.put("formoutputDtoCode", MapUtils.getString(resultCodeMap, "/templates/formDataVo.java.vm"));
		resultMap.put("irepositoryCode", MapUtils.getString(resultCodeMap, "/templates/mapper.java.vm"));
		resultMap.put("iserviceCode", MapUtils.getString(resultCodeMap, "/templates/service.java.vm"));
//		resultMap.put("listHTML", "");
		resultMap.put("pageinputDtoCode", MapUtils.getString(resultCodeMap, "/templates/listDto.java.vm"));
		resultMap.put("pageoutputDtoCode", MapUtils.getString(resultCodeMap, "/templates/listVo.java.vm"));
		resultMap.put("repositoryCode", MapUtils.getString(resultCodeMap, "/templates/mapper.xml.vm"));
		resultMap.put("serviceCode", MapUtils.getString(resultCodeMap, "/templates/serviceImpl.java.vm"));
		return resultMap;
	}
}