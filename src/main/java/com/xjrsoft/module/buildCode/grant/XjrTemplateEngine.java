package com.xjrsoft.module.buildCode.grant;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.module.buildCode.config.InjectionConfig;
import com.xjrsoft.module.buildCode.config.builder.ConfigBuilder;
import com.xjrsoft.module.buildCode.engine.AbstractTemplateEngine;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.util.*;

@Data
public class XjrTemplateEngine extends AbstractTemplateEngine {

    /**
     * 是否写入文件
     */
    private boolean isWriteCode;

    private static final String DOT_VM = ".vm";

    private boolean isOutputFile;

    private String dbLinkId;

    private boolean isPage;

    private boolean isImport;

    private boolean isTree;

    private boolean isTrans;

    private boolean isWorkflowForm;

    private String mainTableName;

    /**
     * 自动编码编号
     */
    private String codeRule;

    private List<Map<String, Object>> tableList;

    private Map<String, List<String>> tableFieldListMap;

    private List<Map<String, String>> columnList;

    private List<Map<String, String>> queryFieldList;

    private Map<String, Object> treeForm;

    private VelocityEngine velocityEngine;

    private Map<String, StringBuilder> resultCodeMap = new HashMap<>(16);

    @Override
    public XjrTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        if (null == velocityEngine) {
            Properties p = new Properties();
            p.setProperty(ConstVal.VM_LOAD_PATH_KEY, ConstVal.VM_LOAD_PATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, StringPool.EMPTY);
            p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
            p.setProperty("file.resource.loader.unicode", StringPool.TRUE);
            velocityEngine = new VelocityEngine(p);
        }
        return this;
    }

    @Override
    public AbstractTemplateEngine batchOutput() {
        try {
            Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
            TemplateConfig template = getConfigBuilder().getTemplate();
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
            TableInfo mainTableInfo = null;
            List<TableInfo> subTableInfoList = new ArrayList<>();
            for (TableInfo tableInfo : tableInfoList) {
                String tableName = tableInfo.getName();
                boolean isMainTable = false;
                if (StringUtils.equalsIgnoreCase(tableName, mainTableName)) {
                    mainTableInfo = tableInfo;
                    isMainTable = true;
                } else {
                    subTableInfoList.add(tableInfo);
                }
                List<TableField> fields = tableInfo.getFields();
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                // 自定义内容
                if (null != injectionConfig && !isMainTable) {
                    injectionConfig.initTableMap(tableInfo);
                    objectMap.put("cfg", injectionConfig.getMap());
                    objectMap.put("fieldNameList", tableFieldListMap.get(tableName));
                    objectMap.put("isMainTable", false);
                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                    if (CollectionUtils.isNotEmpty(focList)) {
                        for (FileOutConfig foc : focList) {
                            if (isCreate(FileType.OTHER, foc.outputFile(tableInfo)) && (foc.getTemplatePath().equals("/templates/entityVo.java.vm") ||
                                    foc.getTemplatePath().equals("/templates/entityDto.java.vm"))) {
                                writerFile(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                            }
                        }
                    }
                }
                // Mp.java
                String entityName = tableInfo.getEntityName();
                if (null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
                    String entityFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.ENTITY, entityFile)) {
                        writerFile(objectMap, templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())), entityFile);
                    }
                }
                // MpMapper.java
                if (null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH)) {
                    String mapperFile = String.format((pathInfo.get(ConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.MAPPER, mapperFile)) {
                        writerFile(objectMap, templateFilePath(template.getMapper()), mapperFile);
                    }
                }
                // MpMapper.xml
                if (null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH)) {
                    String xmlFile = String.format((pathInfo.get(ConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + ConstVal.XML_SUFFIX), entityName);
                    if (isCreate(FileType.XML, xmlFile)) {
                        writerFile(objectMap, templateFilePath(template.getXml()), xmlFile);
                    }
                }
            }
            String mainEntityName = mainTableInfo.getEntityName();
            String serviceName = Func.toStr(injectionConfig.getMap().get("serviceName"));
            Map<String, Object> objectMap = getObjectMap(mainTableInfo);
            injectionConfig.initTableMap(mainTableInfo);
            objectMap.put("cfg", injectionConfig.getMap());
            objectMap.put("subTableList", subTableInfoList);
            objectMap.put("tableList", tableList);
            objectMap.put("queryFieldList", queryFieldList);
            objectMap.put("treeForm", treeForm);
            objectMap.put("isImport", isImport);
            objectMap.put("isPage", isPage);
            objectMap.put("dbLinkId", dbLinkId);
            objectMap.put("isTree", isTree);
            objectMap.put("isTrans", isTrans);
            objectMap.put("isWorkflowForm", isWorkflowForm);
            objectMap.put("codeRule", codeRule);
            // IMpService.java
            if (null != mainTableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH)) {
                String serviceFile = String.format((pathInfo.get(ConstVal.SERVICE_PATH) + File.separator + mainTableInfo.getServiceName() + suffixJavaOrKt()), serviceName);
                if (isCreate(FileType.SERVICE, serviceFile)) {
                    writerFile(objectMap, templateFilePath(template.getService()), serviceFile);
                }
            }
            // MpServiceImpl.java
            if (null != mainTableInfo.getServiceImplName() && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH)) {
                String implFile = String.format((pathInfo.get(ConstVal.SERVICE_IMPL_PATH) + File.separator + mainTableInfo.getServiceImplName() + suffixJavaOrKt()), serviceName);
                if (isCreate(FileType.SERVICE_IMPL, implFile)) {
                    writerFile(objectMap, templateFilePath(template.getServiceImpl()), implFile);
                }
            }
            // MpController.java
            if (null != mainTableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {
                String controllerFile = String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator + mainTableInfo.getControllerName() + suffixJavaOrKt()), serviceName);
                if (isCreate(FileType.CONTROLLER, controllerFile)) {
                    writerFile(objectMap, templateFilePath(template.getController()), controllerFile);
                }
            }
            // 自定义内容
            if (null != injectionConfig) {
                List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                if (CollectionUtils.isNotEmpty(focList)) {
                    objectMap.put("fieldNameList", tableFieldListMap.get(mainTableName));
                    objectMap.put("columnList", columnList);
                    objectMap.put("isMainTable", true);
                    for (FileOutConfig foc : focList) {
                        if (isCreate(FileType.OTHER, foc.outputFile(mainTableInfo))) {
                            writerFile(objectMap, foc.getTemplatePath(), foc.outputFile(mainTableInfo));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }

    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (!this.isWriteCode) {
            // 预览代码
            renderContent(objectMap, templatePath);
            return;
        }
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        try (FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter ow = new OutputStreamWriter(fos, ConstVal.UTF8);
            BufferedWriter writer = new BufferedWriter(ow)) {
            template.merge(new VelocityContext(objectMap), writer);
        }
        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }

    public void renderContent(Map<String, Object> objectMap, String templatePath) throws Exception {
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        StringWriter sw = new StringWriter();
        try (BufferedWriter writer = new BufferedWriter(sw)) {
            template.merge(new VelocityContext(objectMap), writer);
        }
        StringBuilder code = resultCodeMap.get(templatePath);
        if (code == null) {
            resultCodeMap.put(templatePath, new StringBuilder(sw.getBuffer().toString()));
        } else {
            // 换行
            code.append(System.lineSeparator());
            code.append(System.lineSeparator());
            code.append(System.lineSeparator());
            code.append(sw.getBuffer().toString());
        }
        logger.debug("模板:" + templatePath + ";");
    }

    @Override
    public String templateFilePath(String filePath) {
        if (null == filePath || filePath.contains(DOT_VM)) {
            return filePath;
        }
        return filePath + DOT_VM;
    }

    @Override
    public AbstractTemplateEngine mkdirs() {
        if (isOutputFile) {
            return super.mkdirs();
        }
        return this;
    }
}
