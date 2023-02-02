package com.xjrsoft.core.tool.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xjrsoft.module.base.entity.XjrBaseTempfield;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.service.IXjrBaseSpecialPostService;
import com.xjrsoft.module.base.service.IXjrBaseTempfieldService;
import com.xjrsoft.module.base.service.IXjrBaseUserRelationService;
//import com.xjrsoft.module.workflow.entity.XjrNwfScheme;
//import com.xjrsoft.module.workflow.service.IXjrNwfSchemeService;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author:光华科技-软件研发部
 * @Date:2020/12/9
 * @Description:工作流json处理工具类
 */
public class JsonUtil {

    @Autowired
    static IXjrBaseTempfieldService baseTempService = SpringUtil.getBean(IXjrBaseTempfieldService.class);

    @Autowired
    static IXjrBaseUserRelationService userRelationService = SpringUtil.getBean(IXjrBaseUserRelationService.class);

    @Autowired
    static IXjrBaseSpecialPostService specialPostService = SpringUtil.getBean(IXjrBaseSpecialPostService.class);

    @Autowired
//    static IXjrNwfSchemeService nwfSchemeService = SpringUtil.getBean(IXjrNwfSchemeService.class);

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/12/21
     * @Param:[jsonStr, nwfSchemeInfoId]
     * @return:java.lang.String
     * @Description:流程json处理
     */
    public static String processJsonHandle(String jsonStr, String nwfSchemeInfoId) {
        JSONObject obj = JSONObject.parseObject(jsonStr);
        // 设置部署ID
        // 获取resourceId
        String allResourceId = obj.getString("resourceId");
        JSONObject firstProperties = obj.getJSONObject("properties");
        if (!Collections.isEmpty(firstProperties)) {
            firstProperties.put("process_id", "process" + allResourceId);
            firstProperties.put("documentation", nwfSchemeInfoId);
            obj.put("properties", firstProperties);
        }
        JSONArray childShapes = obj.getJSONArray("childShapes");
        for (int i = 0; i < childShapes.size(); i++) {
            JSONObject childShape = childShapes.getJSONObject(i);
            if (childShape == null) {
                continue;
            }
            JSONObject properties = childShape.getJSONObject("properties");
            if (properties == null) {
                continue;
            }
            // 是否是会签节点类型
            String mulType = properties.getString("multiinstance_type");
            boolean isMulti = !StringUtils.equalsIgnoreCase(mulType, "None") && StringUtils.isNotBlank(mulType);
            // 节点的类型
            String stencil = childShape.getJSONObject("stencil").getString("id");
//            // 0.保存流程候选人
//            if (StringUtils.equalsIgnoreCase(stencil, "UserTask") && !isMulti) {
//                setAssignee(properties,null );
//            }
            // 1.判断是否是会签节点
            if (isMulti) {
                properties = multiNodeHandler(childShapes, childShape, nwfSchemeInfoId);
            }
            // 2.流转条件格式转换
            String conditionSequenceFlow = properties.getString("conditionsequenceflow");
            if (StringUtils.equalsIgnoreCase(stencil, "SequenceFlow") && StringUtils.isNotBlank(conditionSequenceFlow) && !StringUtils.equalsIgnoreCase(conditionSequenceFlow, "[]")) {
                String expression = replaceCondition(conditionSequenceFlow);
                properties.put("conditionsequenceflow", expression);
            }

//            if (!isMulti) {
//                if (StringUtils.equalsIgnoreCase(stencil, "UserTask")) {
//                    // 2.加监听器
//                    JSONObject listenerMap = new JSONObject();
//                    JSONArray listenerList = new JSONArray();
//                    Map<String, Object> completeMap = new HashMap<>(16);
//                    completeMap.put("event", "assignment");
//                    completeMap.put("className", "com.xjrsoft.module.workflow.listener.TaskExecutionListener");
//                    listenerList.add(completeMap);
//                    listenerMap.put("taskListeners", listenerList);
//                    properties.put("tasklisteners", listenerMap);
//                }
//            }
            // 3.脚本任务的处理
            if (StringUtils.equalsIgnoreCase("ScriptTask", stencil)) {
                properties = scriptTaskHandler(properties);
            }
            // 4.调用活动
//            if (StringUtils.equalsIgnoreCase("CallActivity", stencil)) {
//                properties = formatCallAct(properties);
//
//            }
            childShape.put("properties", properties);
        }
        return JSONObject.toJSONString(obj);
    }

    private static JSONObject scriptTaskHandler(JSONObject properties) {
        JSONArray totalAry = new JSONArray();
        // 获取配置参数
        // 脚本配置
        JSONObject taskScriptObj = properties.getJSONObject("taskscript");
        taskScriptObj.put("type", "taskScriptObj");
        Boolean scriptFlag = taskScriptObj.getBoolean("isActvate");
        // api配置
        JSONObject taskApiObj = properties.getJSONObject("taskapi");
        taskApiObj.put("type", "taskApiObj");
        Boolean apiFlag = taskApiObj.getBoolean("isActvate");
        // sql配置
        JSONObject taskSqlObj = properties.getJSONObject("tasksql");
        taskSqlObj.put("type", "taskSqlObj");
        Boolean sqlFlag = taskSqlObj.getBoolean("isActvate");
        if (scriptFlag) totalAry.add(JSON.toJSONString(taskScriptObj).trim());
        if (apiFlag) totalAry.add(JSON.toJSONString(taskApiObj).trim());
        if (sqlFlag) totalAry.add(JSON.toJSONString(taskSqlObj).trim());
        properties.put("scriptformat", "groovy");
        String scripttext = "def scriptVar = " + JSON.toJSONString(totalAry) + StringPool.NEWLINE + "execution.setVariable(\"myVar\", scriptVar)";
        properties.put("scripttext", scripttext);
        //脚本任务变量存到临时表中
        baseTempService.remove(Wrappers.<XjrBaseTempfield>query().lambda().eq(XjrBaseTempfield::getFkey, properties.getString("id")).eq(XjrBaseTempfield::getType, "20"));
        XjrBaseTempfield xjrBaseTempfield = new XjrBaseTempfield();
        xjrBaseTempfield.setFkey(properties.getString("id"));
        xjrBaseTempfield.setFvalue(totalAry.toString());
        xjrBaseTempfield.setType("20");
        baseTempService.save(xjrBaseTempfield);
        return properties;
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/12/24
     * @Param:[conditionSequenceFlow]
     * @return:java.lang.String
     * @Description:替换流转条件
     */
    private static String replaceCondition(String conditionSequenceFlow) {
        StringBuffer sBuffer = new StringBuffer();
        JSONArray conditionArray = JSONArray.parseArray(conditionSequenceFlow);
        String content = "";
        // 字段类型
        String fileType = "";
        for (int i = 0; i < conditionArray.size(); i++) {
            JSONObject object = conditionArray.getJSONObject(i);
            String type = object.getString("type");
            if (StringUtils.equals(type, "condition")) {
                // resourceId
                String resourceId = object.getString("resourceId");
                // 表名
                String bindTable = object.getString("bindTable");
                // 字段
                String fieldId = object.getString("fieldId");
                // 表单Id+字段名
                content = resourceId + StringPool.UNDERSCORE + bindTable + StringPool.UNDERSCORE + fieldId;
//                fileType = String.valueOf(retMap.get("type"));
            } else if (StringUtils.equals(type, "button")) {
                // 按钮组
                content = "btnValue";
                fileType = "varchar";
            } else if (StringUtils.equals(type, "value")) {
                // 值
                String value = String.valueOf(object.get("value"));
                if (StringUtils.isNumeric(value)) {
                    content = String.valueOf(object.get("value"));
                } else {
                    content = "'" + String.valueOf(object.get("value")) + "'";
                }
            } else if (StringUtils.equals(type, "andornot")) {
                // 操作符
                String value = object.getString("value");
                if (StringUtils.equals(value, "or")) {
                    content = "||";
                } else if (StringUtils.equals(value, "and")) {
                    content = "&&";
                } else {
                    content = "!";
                }
            } else if (StringUtils.equals(type, "operator")) {
                String value = object.getString("value");
                // 操作符
                if (StringUtils.equals(value, "=")) {
                    content = StringPool.SPACE + "==" + StringPool.SPACE;
//                }else if(StringUtils.equals(value, "⊇")){
//                    content = StringPool.DOT + "contains";
                }else {
                    content = object.getString("value");
                }
            } else {
                content = object.getString("value");
            }
            sBuffer.append(content);
        }
        return String.valueOf("${" + sBuffer + "}");
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/12/21
     * @Param:[fkUserEm]
     * @return:void
     * @Description:设置流程审批人
     */
    private static JSONObject setAssignee(JSONObject properties, String key) {
        JSONArray userArray = new JSONArray();
        JSONObject usertaskassignment = properties.getJSONObject("usertaskassignment");
        if (usertaskassignment != null) {
            JSONObject assignment = usertaskassignment.getJSONObject("assignment");
            JSONArray fkUserEm = assignment.getJSONArray("FKtableEM");
            for (int i = 0; i < fkUserEm.size(); i++) {
                JSONObject userObj = fkUserEm.getJSONObject(i);
                userArray.add(userObj);
            }
            // 流程候选人
            if (StringUtils.isNotBlank(key) && !Collections.isEmpty(userArray)) {
                XjrBaseTempfield xjrBaseTempfield = new XjrBaseTempfield();
                xjrBaseTempfield.setFkey(key);
                xjrBaseTempfield.setType("4");
                xjrBaseTempfield.setFvalue(JSON.toJSONString(userArray));
                baseTempService.save(xjrBaseTempfield);
            }
        }
        return properties;
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/12/21
     * @Param:[fkUserEm]
     * @return:void
     * @Description:设置会签流程审批人
     */
    private static JSONObject setMultiAssignee(JSONObject properties) {
        JSONObject usertaskassignment = properties.getJSONObject("usertaskassignment");
        if (usertaskassignment != null) {
            JSONObject assignment = usertaskassignment.getJSONObject("assignment");
            JSONArray fkUserEm = assignment.getJSONArray("FKtableEM");
            Set<Map<String, Object>> candidateUsers = new HashSet<>();
            for (int i = 0; i < fkUserEm.size(); i++) {
                JSONObject userObj = fkUserEm.getJSONObject(i);
                Integer t = Integer.valueOf(userObj.getString("F_ObjType"));
                if (userObj.getBoolean("check")) {
                    String fObjId = userObj.getString("F_ObjId");
                    switch (t) {
                        case 1:
                            // 岗位,把岗位下的用户设置成候选人
                            List<XjrBaseUser> usersOfPost = userRelationService.getUsersOfObject(fObjId, 2);
                            for (XjrBaseUser xjrBaseUser : usersOfPost) {
                                JSONObject userInfo = new JSONObject();
                                userInfo.put("value", xjrBaseUser.getUserId());
                                candidateUsers.add(userInfo);
                            }
                            break;
                        case 2:
                            // 角色的限制条件
                            List<XjrBaseUser> usersOfRole = userRelationService.getUsersOfObject(fObjId, 1);
                            for (XjrBaseUser xjrBaseUser : usersOfRole) {
                                JSONObject userInfo = new JSONObject();
                                userInfo.put("value", xjrBaseUser.getUserId());
                                candidateUsers.add(userInfo);
                            }
                            break;
                        case 3:
                            // 指定用户
                            JSONObject userInfo = new JSONObject();
                            userInfo.put("value", fObjId);
                            candidateUsers.add(userInfo);
                            break;
                        default:
                            // 动态候选人保存
                            break;
                    }
                }
            }
            assignment.put("candidateUsers", candidateUsers);
            usertaskassignment.put("assignment", assignment);
            properties.put("usertaskassignment", usertaskassignment);
        }
        return properties;
    }


    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/12/26
     * @Param:[jsonObj, version, nwfSchemeInfoId]
     * @return:void
     * @Description:保存节点的相关信息
     */
    public static void saveNodeInfo(String jsonObj, String version, String nwfSchemeInfoId) {
        String key = "";
        List<XjrBaseTempfield> saveList = new ArrayList<>(10);
        JSONObject obj = JSONObject.parseObject(jsonObj);
        //是否为下一节点选定审批人
        JSONArray childShapes = obj.getJSONArray("childShapes");
        for (int i = 0; i < childShapes.size(); i++) {
            JSONObject childShape = childShapes.getJSONObject(i);
            if (childShape == null) {
                continue;
            }
            // 节点的properties属性
            JSONObject properties = childShape.getJSONObject("properties");
            // 节点的resourceId
            String resourceId = childShape.getString("resourceId");
            if (properties == null) {
                continue;
            }
            key = nwfSchemeInfoId + StringPool.UNDERSCORE + resourceId + StringPool.UNDERSCORE + version;
            // 1.保存会签userNameList
            String mulType = properties.getString("multiinstance_type");
            if (!StringUtils.equalsIgnoreCase(mulType, "None") && StringUtils.isNotBlank(mulType)) {
                // 获取会签上一个节点的resourceId
                JSONArray userTaskList = getFlowResourceId(childShapes, resourceId);
                JSONObject usertaskassignment = properties.getJSONObject("usertaskassignment");
                if (usertaskassignment != null) {
                    JSONObject assignment = usertaskassignment.getJSONObject("assignment");
                    JSONArray fkUserEm = assignment.getJSONArray("FKtableEM");
                    JSONArray userArray = new JSONArray();
                    for (int k = 0; k < fkUserEm.size(); k++) {
                        JSONObject userObj = fkUserEm.getJSONObject(k);
                        if (userObj.getBoolean("check")) {
                            userArray.add(userObj);
                        }
                    }
                    for (int j = 0; j < userTaskList.size(); j++) {
                        String userTaskResourceId = userTaskList.getString(j);
                        XjrBaseTempfield xjrBaseTempfield = new XjrBaseTempfield();
                        xjrBaseTempfield.setFvalue(JSON.toJSONString(userArray));
                        xjrBaseTempfield.setType("1");
                        xjrBaseTempfield.setFkey(nwfSchemeInfoId + StringPool.UNDERSCORE + userTaskResourceId + StringPool.UNDERSCORE + version);
                        baseTempService.save(xjrBaseTempfield);
                    }
                }
            }
                // 2.保存字段权限
                if (properties.containsKey("tableData")) {
                    JSONArray tableDataArray = properties.getJSONArray("tableData");
                    if (!Collections.isEmpty(tableDataArray)) {
                        String tableData = JSON.toJSONString(tableDataArray);
                        XjrBaseTempfield xjrTempKeyvalue = new XjrBaseTempfield();
                        xjrTempKeyvalue.setFkey(key);
                        xjrTempKeyvalue.setType("3");
                        xjrTempKeyvalue.setFvalue(tableData);
                        saveList.add(xjrTempKeyvalue);
                    }
                }
                // 3.保存流转按钮信息
                if (properties.containsKey("fkbuttonpopu") || properties.containsKey("functionButton")) {
                    JSONArray buttonArray = properties.getJSONArray("fkbuttonpopu");
                    if (CollectionUtil.isNotEmpty(buttonArray)) {
                        Iterator<Object> iterator = buttonArray.iterator();
                        while (iterator.hasNext()) {
                            JSONObject next = (JSONObject) iterator.next();
                            Boolean check = next.getBoolean("check");
                            if (!check) {
                                iterator.remove();
                            }
                        }
                        String fkbuttonpopuJson = JSON.toJSONString(buttonArray);
                        XjrBaseTempfield fkbuttonpopuJsonKeyvalue = new XjrBaseTempfield();
                        fkbuttonpopuJsonKeyvalue.setFkey(key);
                        fkbuttonpopuJsonKeyvalue.setType("2");
                        fkbuttonpopuJsonKeyvalue.setFvalue(fkbuttonpopuJson);
                        saveList.add(fkbuttonpopuJsonKeyvalue);
                    }
                    //功能按钮
                    JSONArray functionButton = properties.getJSONArray("functionButton");
                    String functionButtonJson = "[]";
                    if (CollectionUtil.isNotEmpty(functionButton)) {
                        Iterator<Object> iterator2 = functionButton.iterator();
                        while (iterator2.hasNext()) {
                            JSONObject next = (JSONObject) iterator2.next();
                            Boolean check = next.getBoolean("check");
                            if (!check) {
                                iterator2.remove();
                            }
                        }
                        functionButtonJson = JSON.toJSONString(functionButton);
                    }
                    XjrBaseTempfield functionButtonKeyvalue = new XjrBaseTempfield();
                    functionButtonKeyvalue.setFkey(key);
                    functionButtonKeyvalue.setType("24");
                    functionButtonKeyvalue.setFvalue(functionButtonJson);
                    saveList.add(functionButtonKeyvalue);
                }

                // 4.保存流程候选人
                if (properties.containsKey("usertaskassignment")) {
                    setAssignee(properties, key);
                }
                // 5.保存审批意见框
                JSONObject fkrelationregion = properties.getJSONObject("fkrelationregion");
                if (!Collections.isEmpty(fkrelationregion)) {
                    XjrBaseTempfield xjrBaseTempfield = new XjrBaseTempfield();
                    xjrBaseTempfield.setFkey(key);
                    xjrBaseTempfield.setType("5");
                    xjrBaseTempfield.setFvalue(JSON.toJSONString(fkrelationregion));
                    saveList.add(xjrBaseTempfield);
                }

                //保存传阅人到临时表
                JSONObject fkforwarderpackage = properties.getJSONObject("fkforwarderpackage");
                if (fkforwarderpackage != null && !"".equals(fkforwarderpackage.toString())) {
                    String manualInfoStr = String.valueOf(fkforwarderpackage);
                    Map<String, Object> manualInfoMap = (Map<String, Object>) JSONObject.parse(manualInfoStr);
                    Map<String, Object> assignment = (Map<String, Object>) manualInfoMap.get("assignment");
                    List<Map<String, Object>> fKtableEM = (List<Map<String, Object>>) assignment.get("FKtableEM");
                    XjrBaseTempfield xjrBaseTempfield = new XjrBaseTempfield();
                    xjrBaseTempfield.setFkey(key);
                    xjrBaseTempfield.setType("7");
                    xjrBaseTempfield.setFvalue(JSON.toJSONString(fKtableEM));
                    saveList.add(xjrBaseTempfield);
                }
            }

            for (XjrBaseTempfield tempKeyvalue : saveList) {
                String colType = tempKeyvalue.getType();
                XjrBaseTempfield oldTempKeyvalue = baseTempService.getByIdAndType(key, colType);
                if (oldTempKeyvalue == null) {
                    tempKeyvalue.setId(IdWorker.getIdStr());
                    baseTempService.save(tempKeyvalue);
                } else {
                    // 更新数据
                    baseTempService.updateById(tempKeyvalue);
                }
            }
        }


        /**
         * @param
         * @author 光华科技-软件研发部
         * @date:2020年4月1日
         * @description:设置候选组人员为候选人
         */
        private static JSONObject groupToUsers (JSONObject properties, Map < String, Object > usertaskassignment){
            Map<String, Object> assignment = (Map<String, Object>) usertaskassignment.get("assignment");
            // 添加了候选组的节点
            if (!Collections.isEmpty(assignment) && assignment.containsKey("FKtableEM")) {
                // 获取岗位下的用户
                List<XjrBaseUser> userList = getUserList(assignment);
                if (!Collections.isEmpty(userList)) {
                    List<Map<String, String>> candidateUserList = null;
                    // 获取candidateUsersList
                    Object object = assignment.get("candidateUsers");
                    if (object != null && !"".equals(object)) {
                        candidateUserList = (List<Map<String, String>>) assignment.get("candidateUsers");
                    } else {
                        candidateUserList = new ArrayList<>(10);
                    }
                    // 把人员放到候选人中
                    for (XjrBaseUser xjrBaseUser : userList) {
                        String userId = xjrBaseUser.getUserId();
                        Map<String, String> userMap = new HashMap<String, String>(16);
                        userMap.put("value", userId);
                        candidateUserList.add(userMap);
                    }
                    assignment.put("candidateUsers", candidateUserList);
                    usertaskassignment.put("assignment", assignment);
                    properties.put("usertaskassignment", usertaskassignment);
                }
            }
            return properties;
        }

        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[assigment]
         * @return:java.util.List<Record>
         * @Description:获取候选组节点的人员信息
         */
        private static List<XjrBaseUser> getUserList (Map < String, Object > assigment){
            List<XjrBaseUser> userList = new ArrayList<XjrBaseUser>(10);
            List<Map<String, Object>> FKtableEMList = (List<Map<String, Object>>) assigment.get("FKtableEM");
            for (Map<String, Object> map : FKtableEMList) {
                // 岗位的type等于1
                String fObjType = (String) map.get("F_ObjType");
                String postId = "";
                if (StringUtils.equals(fObjType, "1")) {
                    // 获取岗位id
                    Object object = map.get("F_ObjId");
                    Class<? extends Object> aclass = object.getClass();
                    if (aclass == Integer.class) {
                        Integer postIdInt = (Integer) map.get("F_ObjId");
                        postId = String.valueOf(postIdInt);
                    } else if (aclass == String.class) {
                        postId = (String) map.get("F_ObjId");
                    }
                    // 查询岗位下的所有人员
                    userList = userRelationService.getUsersOfObject(postId, 2);
                }
            }
            return userList;
        }


        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[childShapes, childShape, properties, nwfSchemeInfoId]
         * @return:com.alibaba.fastjson.JSONObject
         * @Description:会签节点的处理
         */
        private static JSONObject multiNodeHandler (JSONArray childShapes, JSONObject childShape, String nwfSchemeInfoId)
        {
            // 节点的properties
            JSONObject properties = childShape.getJSONObject("properties");
            // 获取会签节点的resourceId
            String resourceId = childShape.getString("resourceId");
            // 1.替换会签表达式
            if (properties.containsKey("multiinstance_condition")) {
                String conditionRet = "";
                // 传过来的例子：percent,12
                String conditionStr = (String) properties.get("multiinstance_condition");
                if (StringUtils.isNotBlank(conditionStr)) {
                    if (StringUtils.equals(conditionStr, "all")) {
                        // 全部通过
                        conditionRet = "${nrOfCompletedInstances/nrOfInstances>=1}";
                    } else if (StringUtils.equals(conditionStr, "single")) {
                        //单个通过
                        conditionRet = "${nrOfCompletedInstances/nrOfInstances>0}";
                    } else {
                        // 百分比
                        String[] split = conditionStr.split(",");
                        BigDecimal bigDecimal = new BigDecimal(split[1]).divide(new BigDecimal(100));
                        conditionRet = "${count/usernamelist.size() >= " + bigDecimal.toString() + "}";
                    }
                }
                properties.put("multiinstance_condition", conditionRet);
                properties.put("multiinstance_variable", "username");
            }
            // 2.加监听器
            JSONObject listenerMap = new JSONObject();
            JSONArray listenerList = new JSONArray();
            // 完成任务事件监听器
            JSONObject completeMap = new JSONObject();
            completeMap.put("event", "complete");
            completeMap.put("implementation", "com.xjrsoft.module.workflow.listener.TaskCompleteListener");
            completeMap.put("className", "com.xjrsoft.module.workflow.listener.TaskCompleteListener");
            completeMap.put("expression", "");
            completeMap.put("delegateExpression", "");
            listenerList.add(completeMap);
            listenerMap.put("taskListeners", listenerList);
            properties.put("tasklisteners", listenerMap);
            // 3.给会签节点的上一个连线设置监听器
            addListener(childShapes, resourceId);
            return properties;
        }

        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[properties, callResourceId, nwfSchemeInfoId]
         * @return:com.alibaba.fastjson.JSONObject
         * @Description: 格式化调用节点活动属性 保存活动节点的入参和出参
         */
        private static JSONObject formatCallAct (JSONObject properties){
            JSONObject callActElement = properties.getJSONObject("callactivitycalledelement");
            String modelId = callActElement.getString("F_modelId");
//            String definitionKey = FlowableUtil.getDefinitionKeyByModelId(modelId);
//            // 调用子流程的key值
//            properties.put("callactivitycalledelement", definitionKey);
            // 输入参数
            JSONObject inparameters = properties.getJSONObject("callactivityinparameters");
            JSONArray inParameters = inparameters.getJSONArray("inParameters");
            JSONArray inRetList = new JSONArray();
            inparameters.put("inParameters", inRetList);
            properties.put("callactivityinparameters", inparameters);

            // 输出参数
            JSONObject actOutParameters = properties.getJSONObject("callactivityoutparameters");
            JSONArray outParameters = actOutParameters.getJSONArray("outParameters");
            JSONArray outRetList = new JSONArray();
            inparameters.put("outParameters", outRetList);
            properties.put("callactivityoutparameters", actOutParameters);

            return properties;
        }


        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[childShapes, resourceId]
         * @return:java.util.List<java.lang.String>
         * @Description:根据会签节点的resourceId获取会签上一usertask节点的resourceId
         */
        private static JSONArray getFlowResourceId (JSONArray childShapes, String resourceId){
            JSONArray userTaskList = new JSONArray();
            for (int i = 0; i < childShapes.size(); i++) {
                JSONObject childShape = childShapes.getJSONObject(i);
                JSONObject targetMap = childShape.getJSONObject("target");
                // 先获取到连线的resourceId,再获取连线的上一节点，并判断该节点是否为usertask
                if (!Collections.isEmpty(targetMap)) {
                    String targetId = targetMap.getString("resourceId");
                    // 会签节点的上一个连线
                    if (StringUtils.equalsIgnoreCase(targetId, resourceId)) {
                        // 获取连线的resourceId
                        String folwResourceId = childShape.getString("resourceId");
                        // 再获取连线的上一个节点Id并判断是否是usertask
                        userTaskList = getUserTaskResId(childShapes, folwResourceId);
                    }
                }
            }
            return userTaskList;
        }

        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[childShapes, resourceId]
         * @return:java.util.List<java.lang.String>
         * @Description:根据连线id获取usertask的resourceId
         */
        public static JSONArray getUserTaskResId (JSONArray childShapes, String resourceId){
            JSONArray retList = new JSONArray();
            for (int i = 0; i < childShapes.size(); i++) {
                JSONObject childShape = childShapes.getJSONObject(i);
                JSONArray array = childShape.getJSONArray("outgoing");
                for (int j = 0; j < array.size(); j++) {
                    JSONObject jsonObject = array.getJSONObject(j);
                    String onResourceId = jsonObject.getString("resourceId");
                    // 是该条连线的上一个节点
                    if (StringUtils.equalsIgnoreCase(resourceId, onResourceId)) {
                        JSONObject stencilMap = childShape.getJSONObject("stencil");
                        if (!Collections.isEmpty(stencilMap)) {
                            String string = stencilMap.getString("id");
                            // 判断是否是usertask类型
                            if (StringUtils.equalsIgnoreCase(string, "UserTask") || StringUtils.equalsIgnoreCase(string, "StartNoneEvent")) {
                                String object = childShape.getString("resourceId");
                                retList.add(object);
                            } else {
                                retList.addAll(getFlowResourceId(childShapes, childShape.getJSONObject("properties").getString("id")));
                            }
                        }
                    }
                }
            }
            return retList;
        }


        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[childShapes, currentResourceId]
         * @return:boolean
         * @Description:根据节点ID获取下一用户节点的ID
         */
        public static boolean checkNextFlowNodeHaveAssignee (JSONArray childShapes){
            boolean flag = true;
            // 获取开始节点的resourceId
            String currentResourceId = childShapes.getJSONObject(0).getString("resourceId");
            List<String> userTaskList = new ArrayList<String>();
            for (int i = 0; i < childShapes.size(); i++) {
                JSONObject childShape = childShapes.getJSONObject(i);
                String resourceId = childShape.getString("resourceId");
                String stencilId = childShape.getJSONObject("stencil").getString("id");
                // 获取连线ID
                if (StringUtils.equals(currentResourceId, resourceId)) {
                    if (StringUtils.equals(stencilId, "SequenceFlow")) {
                        // 获取连线的下一节点ID
                        JSONArray outgoingAry = childShape.getJSONArray("outgoing");
                        for (int j = 0; j < outgoingAry.size(); j++) {
                            String reString = outgoingAry.getJSONObject(j).getString("resourceId");
                            userTaskList.add(reString);
                        }
                    }
                }
            }
            // 判断下一节点是否有候选人
            for (String nextResourceId : userTaskList) {
                for (int i = 0; i < childShapes.size(); i++) {
                    JSONObject childShape = childShapes.getJSONObject(i);
                    // 节点id
                    String resourceId = childShape.getString("resourceId");
                    // 节点类型
                    String stencilId = childShape.getJSONObject("stencil").getString("id");
                    // 是下一个节点的信息
                    if (StringUtils.equals(resourceId, nextResourceId) && StringUtils.equals(stencilId, "UserTask")) {
                        // 判断是否有候选人
                        Object usertaskassignmentObj = childShape.get("usertaskassignment");
                        if (!(usertaskassignmentObj instanceof String)) {
                            flag = false;
                        }
                    }
                }
            }
            return flag;
        }


        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[childShapes, resourceId]
         * @return:void
         * @Description:给会签节点的上一个连线设置监听器
         */
        private static void addListener (JSONArray childShapes, String resourceId){
            for (int i = 0; i < childShapes.size(); i++) {
                JSONObject childShape = childShapes.getJSONObject(i);
                JSONObject properties = childShape.getJSONObject("properties");
                Map<String, Object> targetMap = (Map<String, Object>) childShape.get("target");
                if (!Collections.isEmpty(targetMap)) {
                    String targetId = (String) targetMap.get("resourceId");
                    if (resourceId.equals(targetId)) {
                        JSONObject listenerMap = new JSONObject();
                        // 给该条连线设置监听器
                        List<Map<String, Object>> listenerList = new ArrayList<>(10);
                        // take任务事件监听器
                        Map<String, Object> completeMap = new HashMap<>(16);
                        completeMap.put("event", "take");
                        completeMap.put("implementation", "${execution.setVariable('count', 0)}");
                        completeMap.put("className", "");
                        completeMap.put("expression", "${execution.setVariable('count', 0)}");
                        completeMap.put("delegateExpression", "");
                        listenerList.add(completeMap);
                        listenerMap.put("executionListeners", listenerList);
                        properties.put("executionlisteners", listenerMap);
                        childShape.put("properties", properties);
                    }
                }
            }
        }

        /**
         * 传入任意一个 object对象生成一个指定规格的字符串
         *
         * @param object 任意对象
         * @return String
         */
        public static String objectToJson (Object object){
            StringBuilder json = new StringBuilder();
            if (object == null) {
                json.append("\"\"");
            } else if (object instanceof String || object instanceof Integer || object instanceof Double) {
                json.append("\"").append(object.toString()).append("\"");
            } else {
                json.append(beanToJson(object));
            }
            return json.toString();
        }

        /**
         * 传入任意一个 Javabean对象生成一个指定规格的字符串
         *
         * @param bean bean对象
         * @return String "{}"
         */
        public static String beanToJson (Object bean){
            StringBuilder json = new StringBuilder();
            json.append("{");
            PropertyDescriptor[] props = null;
            try {
                props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (props != null) {
                for (int i = 0; i < props.length; i++) {
                    try {
                        String name = objectToJson(props[i].getName());
                        String value = objectToJson(props[i].getReadMethod().invoke(bean));
                        json.append(name);
                        json.append(":");
                        json.append(value);
                        json.append(",");
                    } catch (Exception e) {
                    }
                }
                json.setCharAt(json.length() - 1, '}');
            } else {
                json.append("}");
            }
            return json.toString();
        }

        /**
         * 通过传入一个列表对象,调用指定方法将列表中的数据生成一个JSON规格指定字符串
         *
         * @param list 列表对象
         * @return String "[{},{}]"
         */
        public static String listToJson (List < ? > list){
            StringBuilder json = new StringBuilder();
            json.append("[");
            if (list != null && list.size() > 0) {
                for (Object obj : list) {
                    json.append(objectToJson(obj));
                    json.append(",");
                }
                json.setCharAt(json.length() - 1, ']');
            } else {
                json.append("]");
            }
            return json.toString();
        }

        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[fSchemeInfo, fieldId]
         * @return:java.lang.String[]
         * @Description:解析表单字段
         */
        public static String[] parseFormField (String fSchemeInfo, String fieldId){
            String[] result = new String[]{};
            Map<String, Object> fScheme = (Map<String, Object>) JSONObject.parse(fSchemeInfo);
            List<Map<String, Object>> data = (List<Map<String, Object>>) fScheme.get("data");
            for (Map<String, Object> dataMap : data) {
                List<Map<String, Object>> componts = (List<Map<String, Object>>) dataMap.get("componts");
                for (Map<String, Object> compontsMap : componts) {
                    // 来源字段等于compontsMap中的字段名
                    if (compontsMap.get("id").equals(fieldId)) {
                        // 表名
                        String tableName = (String) compontsMap.get("table");
                        // 来源变量
                        // 表单字段名
                        String fieldName = (String) compontsMap.get("field");
                        result = new String[]{tableName, fieldName};
                    }
                }
            }
            return result;
        }


        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[jsonStr, timeOut, messageInterval, pushTimes]
         * @return:java.lang.String
         * @Description:给每个用户任务添加超时事件
         */
        public static JSONObject userTaskAddEvent (JSONObject obj, String timeOut){
            JSONArray retArray = new JSONArray();
            JSONArray childShapes = obj.getJSONArray("childShapes");
            for (int i = 0; i < childShapes.size(); i++) {
                JSONObject childShape = childShapes.getJSONObject(i);
                JSONArray outgoingArray = (JSONArray) childShape.get("outgoing");
                JSONObject stencilMap = (JSONObject) childShape.get("stencil");
                String stencil = stencilMap.getString("id");
                // 判断是否是用户任务
                if (StringUtils.equals(stencil, "UserTask")) {
                    // 获取userTask的bounds
                    JSONObject uTBounds = (JSONObject) childShape.get("bounds");
                    // userTask的lowerRight
                    JSONObject utLowerRight = uTBounds.getJSONObject("lowerRight");
                    Integer rx = utLowerRight.getInteger("x");
                    Integer ry = utLowerRight.getInteger("y");
                    // user的upperleft
                    JSONObject utUpperLeft = uTBounds.getJSONObject("upperLeft");
                    Integer lx = utUpperLeft.getInteger("x");
                    Integer ly = utUpperLeft.getInteger("y");
                    // 边界事件的id
                    String eventId = "sid-" + UUID.randomUUID().toString();
                    // 连线的id
//				String sequenceFlowId = "sid-" + UUID.randomUUID().toString();
                    // 给outgoing加上边界事件的id
                    Map<String, String> outgoingMap = new HashMap<>(16);
                    outgoingMap.put("resourceId", eventId);
                    outgoingArray.add(outgoingMap);
                    childShape.put("outgoing", outgoingArray);
                    // 添加边界事件
                    Map<String, Object> eventMap = new HashMap<>(10);
                    eventMap.put("resourceId", eventId);
                    // 边界事件的properties
                    Map<String, Object> propertiesMap = new HashMap<>(16);
                    propertiesMap.put("overrideid", "");
                    propertiesMap.put("name", "超时事件");
                    propertiesMap.put("documentation", "");
                    //				propertiesMap.put("timercycledefinition", "R" + messageInterval + "/PT" + timeOut + "S");
                    propertiesMap.put("timercycledefinition", "");
                    propertiesMap.put("timerdatedefinition", "");
                    propertiesMap.put("timerdurationdefinition", "PT" + timeOut + "S");
                    propertiesMap.put("timerenddatedefinition", "");
                    propertiesMap.put("cancelactivity", false);
                    eventMap.put("properties", propertiesMap);
                    Map<String, String> newStencilMap = new HashMap<>(1);
                    newStencilMap.put("id", "BoundaryTimerEvent");
                    eventMap.put("stencil", newStencilMap);
                    eventMap.put("childShapes", new JSONArray());
                    // 边界事件的去向
                    JSONArray boundaryTimerOutgoingAry = new JSONArray();
                    JSONObject boundaryTimerOutgoingObj = new JSONObject();
                    boundaryTimerOutgoingObj.put("resourceId", /*sequenceFlowId*/"");
                    boundaryTimerOutgoingAry.add(boundaryTimerOutgoingObj);
                    eventMap.put("outgoing", boundaryTimerOutgoingAry);
                    Map<String, Object> boundsMap = new HashMap<>(2);
                    // lowerRight
                    JSONObject lowerRight = new JSONObject();
                    lowerRight.put("x", rx + 28);
                    lowerRight.put("y", ry + 15);
                    // upperLeft
                    JSONObject upperLeft = new JSONObject();
                    upperLeft.put("x", lx + 10);
                    upperLeft.put("y", ly + 64);
                    boundsMap.put("lowerRight", lowerRight);
                    boundsMap.put("upperLeft", upperLeft);
                    eventMap.put("bounds", boundsMap);
                    // 边界事件的dockers
                    JSONArray eventDockers = new JSONArray();
                    JSONObject eventDocker = new JSONObject();
                    eventDocker.put("x", 39);
                    eventDocker.put("y", 70);
                    eventDockers.add(eventDocker);
                    eventMap.put("dockers", eventDockers);
                    retArray.add(eventMap);
                }
                retArray.add(childShape);
            }
            obj.put("childShapes", retArray);
            return obj;
        }

        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[sourceJsonArray, targetJsonArray]
         * @return:java.lang.Integer
         * @Description:对比两个json返回相同节点的个数
         */
        public static Integer comparisonJson (JSONArray sourceJsonArray, JSONArray targetJsonArray){
            Integer count = 0;
            Iterator<Object> sourceIterator = sourceJsonArray.iterator();
            Iterator<Object> targetIterator = targetJsonArray.iterator();
            while (sourceIterator.hasNext()) {
                JSONObject sourceNext = (JSONObject) sourceIterator.next();
                // 源节点ID
                String sourceResourceId = sourceNext.getString("resourceId");
                JSONObject sourceStencil = sourceNext.getJSONObject("stencil");
                // 源节点类型
                String sourceStencilString = sourceStencil.getString("id");
                while (targetIterator.hasNext()) {
                    JSONObject targetNext = (JSONObject) targetIterator.next();
                    // 目标节点ID
                    String targetResourceId = targetNext.getString("resourceId");
                    JSONObject targetStencil = targetNext.getJSONObject("stencil");
                    // 目标节点类型
                    String targetStencilString = targetStencil.getString("id");
                    // 判断是否是用户任务
                    if (StringUtils.equalsIgnoreCase(sourceStencilString, "UserTask")) {
                        if (StringUtils.equalsIgnoreCase(sourceStencilString, targetStencilString)) {
                            // 源节点的表单信息
                            JSONObject sourcePropertiesObj = sourceNext.getJSONObject("properties");
                            JSONObject sourceFormkeydefinition = sourcePropertiesObj.getJSONObject("formkeydefinition");
                            JSONArray sourceFormKeyList = sourceFormkeydefinition.getJSONArray("formKey");
                            // 源节点的审批人信息
                            JSONArray sourceFKtableEM = null;
                            JSONObject sourceUsertaskassignment = sourcePropertiesObj.getJSONObject("usertaskassignment");
                            if (sourceUsertaskassignment != null) {
                                JSONObject sourceAssignmentObj = sourceUsertaskassignment.getJSONObject("assignment");
                                sourceFKtableEM = sourceAssignmentObj.getJSONArray("FKtableEM");
                            }
                            // 目标节点的表单信息
                            JSONObject targetPropertiesObj = targetNext.getJSONObject("properties");
                            JSONObject targetFormkeydefinition = targetPropertiesObj.getJSONObject("formkeydefinition");
                            JSONArray targetFormKeyList = targetFormkeydefinition.getJSONArray("formKey");
                            // 目标节点的审批人信息
                            JSONArray targetFKtableEM = null;
                            JSONObject targetUsertaskassignment = targetPropertiesObj.getJSONObject("usertaskassignment");
                            if (targetUsertaskassignment != null) {
                                JSONObject targetAssignmentObj = targetUsertaskassignment.getJSONObject("assignment");
                                targetFKtableEM = targetAssignmentObj.getJSONArray("FKtableEM");
                            }
                            // 用户节点表单是否相等标识
                            boolean isFormEqual = false;
                            // 用户节点审批人信息是否相等标识
                            boolean isAssinessEqual = false;
                            // 判断两个用户任务节点ID是否相等
                            if (StringUtils.equalsIgnoreCase(sourceResourceId, targetResourceId)) {
                                // 对比表单信息
                                if (sourceFormKeyList.size() == targetFormKeyList.size()) {
                                    Iterator<Object> sourceFormIterator = sourceFormKeyList.iterator();
                                    Iterator<Object> targetFormIterator = targetFormKeyList.iterator();
                                    flagForm:
                                    while (sourceFormIterator.hasNext()) {
                                        JSONObject sourceFormKeyMap = (JSONObject) sourceFormIterator.next();
                                        String sourceFormKeyValue = sourceFormKeyMap.getString("value");
                                        while (targetFormIterator.hasNext()) {
                                            JSONObject targetFormKeyMap = (JSONObject) targetFormIterator.next();
                                            String targetFormKeyValue = targetFormKeyMap.getString("value");
                                            if (StringUtils.equalsIgnoreCase(sourceFormKeyValue, targetFormKeyValue)) {
                                                // 删除已经对比过的元素
                                                sourceFormIterator.remove();
                                                targetFormIterator.remove();
                                            } else {
                                                break flagForm;
                                            }
                                            isFormEqual = true;
                                        }
                                    }
                                    // 表单不相等就可以不用比较后面的审批人信息
                                    if (isFormEqual) {
                                        // 审批人信息是否相等
                                        if (sourceFKtableEM != null && targetFKtableEM != null) {
                                            if (sourceFKtableEM.size() == targetFKtableEM.size()) {
                                                Iterator<Object> sourceEMIterator = sourceFKtableEM.iterator();
                                                Iterator<Object> targetEMIterator = targetFKtableEM.iterator();
                                                flagAssignee:
                                                while (sourceEMIterator.hasNext()) {
                                                    while (targetEMIterator.hasNext()) {
                                                        JSONObject sourceFormKeyMap = (JSONObject) sourceEMIterator.next();
                                                        String sourceFormKeyValue = sourceFormKeyMap.getString("F_ObjId");
                                                        JSONObject targetFormKeyMap = (JSONObject) targetEMIterator.next();
                                                        String targetFormKeyValue = targetFormKeyMap.getString("F_ObjId");
                                                        if (StringUtils.equalsIgnoreCase(sourceFormKeyValue, targetFormKeyValue)) {
                                                            // 删除已经对比过的元素
                                                            sourceEMIterator.remove();
                                                            targetEMIterator.remove();
                                                        } else {
                                                            break flagAssignee;
                                                        }
                                                        isAssinessEqual = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                break;
                            }
                            if (isFormEqual && isAssinessEqual) {
                                // 次数加1
                                count += 1;
                                break;
                            }
                        } else {
                            // 源流程节点的用户任务改成了非用户任务的情况下
                            if (!StringUtils.equalsIgnoreCase(targetStencilString, "UserTask")) {
                                return count -= 1;
                            }
                            return count;
                        }
                    } else {
                        break;
                    }
                }
            }
            return count;
        }

        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[sourceJsonArray, targetJsonArray, resourceId]
         * @return:boolean
         * @Description:根据resourceId判断两个json是否相等（比较表单和审批人）
         */
        public static boolean comparisonJson (JSONArray sourceJsonArray, JSONArray targetJsonArray, String resourceId){
            // 根据resourceId获取properties
            JSONObject jsonArray1 = getPropertiesByResId(sourceJsonArray, resourceId);
            JSONObject jsonArray2 = getPropertiesByResId(targetJsonArray, resourceId);
            if (jsonArray1 == null || jsonArray2 == null) {
                return false;
            }
            // 获取sourceJsonArrayd的表单信息
            JSONObject sourceFormkeydefinition = (JSONObject) jsonArray1.get("formkeydefinition");
            JSONArray sourceFormKeyList = (JSONArray) sourceFormkeydefinition.get("formKey");
            // 获取targetJsonArray的表单信息
            JSONObject targetFormkeydefinition = (JSONObject) jsonArray2.get("formkeydefinition");
            JSONArray targetFormKeyList = (JSONArray) targetFormkeydefinition.get("formKey");
            // 获取sourceJsonArrayd的审批人信息
            JSONArray sourceFKtableEM = null;
            JSONObject sourceUsertaskassignment = (JSONObject) jsonArray1.get("usertaskassignment");
            if (sourceUsertaskassignment != null) {
                JSONObject sourceAssignmentObj = sourceUsertaskassignment.getJSONObject("assignment");
                sourceFKtableEM = sourceAssignmentObj.getJSONArray("FKtableEM");
            }
            // 获取targetJsonArray的表单信息
            JSONArray targetFKtableEM = null;
            JSONObject targetUsertaskassignment = (JSONObject) jsonArray2.get("usertaskassignment");
            if (targetUsertaskassignment != null) {
                JSONObject targetAssignmentObj = targetUsertaskassignment.getJSONObject("assignment");
                targetFKtableEM = targetAssignmentObj.getJSONArray("FKtableEM");
            }
            // 对比表单信息
            if (sourceFormKeyList.size() != targetFormKeyList.size()) {
                return false;
            }
            Iterator<Object> sourceIterator = sourceFormKeyList.iterator();
            Iterator<Object> targetIterator = targetFormKeyList.iterator();
            while (sourceIterator.hasNext()) {
                JSONObject sourceFormKeyMap = (JSONObject) sourceIterator.next();
                String sourceFormKeyValue = sourceFormKeyMap.getString("value");
                while (targetIterator.hasNext()) {
                    JSONObject targetFormKeyMap = (JSONObject) targetIterator.next();
                    String targetFormKeyValue = targetFormKeyMap.getString("value");
                    if (StringUtils.equalsIgnoreCase(sourceFormKeyValue, targetFormKeyValue)) {
                        // 删除已经对比过的元素
                        sourceIterator.remove();
                        targetIterator.remove();
                    } else {
                        return false;
                    }
                }
            }
            // 对比审批人信息
            if (sourceFKtableEM != null && targetFKtableEM != null) {
                if (sourceFKtableEM.size() != targetFKtableEM.size()) {
                    return false;
                }
                Iterator<Object> sourceEMIterator = sourceFKtableEM.iterator();
                Iterator<Object> targetEMIterator = targetFKtableEM.iterator();
                while (sourceEMIterator.hasNext()) {
                    while (targetEMIterator.hasNext()) {
                        JSONObject sourceFormKeyMap = (JSONObject) sourceEMIterator.next();
                        String sourceFormKeyValue = sourceFormKeyMap.getString("F_ObjId");
                        JSONObject targetFormKeyMap = (JSONObject) targetEMIterator.next();
                        String targetFormKeyValue = targetFormKeyMap.getString("F_ObjId");
                        if (StringUtils.equalsIgnoreCase(sourceFormKeyValue, targetFormKeyValue)) {
                            // 删除已经对比过的元素
                            sourceEMIterator.remove();
                            targetEMIterator.remove();
                        } else {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        /**
         * @Author:yh
         * @Date:2020/9/15
         * @Param:[jsonArray, resourceId]
         * @return:net.sf.json.JSONObject
         * @Description:判断数组中是否存在改resourceId,存在则返回properties
         */
        public static JSONObject getPropertiesByResId (JSONArray jsonArray, String resourceId){
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String sourceResId = jsonObject1.getString("resourceId");
                if (StringUtils.equals(sourceResId, resourceId)) {
                    JSONObject properties = (JSONObject) jsonObject1.get("properties");
                    return properties;
                }
            }
            return null;
        }

        /**
         * @Author:光华科技-软件研发部
         * @Date:2020/11/10
         * @Param:[fkbuttonpopuList]
         * @return:java.util.List<java.util.Map<java.lang.String,java.lang.String>>
         * @Description:去除list里面重复的对象，根据key值判断
         */
        public static List<Map<String, String>> removeSameList (List < Map < String, String >> fkbuttonpopuList){
            for (int i = 0; i < fkbuttonpopuList.size() - 1; i++) {
                for (int j = fkbuttonpopuList.size() - 1; j > i; j--) {
                    if (fkbuttonpopuList.get(j).get("key").equals(fkbuttonpopuList.get(i).get("key"))) {
                        fkbuttonpopuList.remove(j);// 删除重复元素
                    }
                }
            }
            return fkbuttonpopuList;
        }


        /**
         * @Author:光华科技-软件研发部
         * @Date:2021/2/22
         * @Param:[nwfSchemeInfoId]
         * @return:java.lang.String
         * @Description:获取开始节点的resourceId
         */
        public static String getStartNodeId (String nwfSchemeInfoId){
//            XjrNwfScheme xjrNwfScheme = nwfSchemeService.getBySchemeInfoId(nwfSchemeInfoId);
//            if (xjrNwfScheme != null) {
//                String json = xjrNwfScheme.getFjson();
//                JSONObject jsonObj = JSON.parseObject(json);
//                JSONArray childShapes = jsonObj.getJSONArray("childShapes");
//                for (int i = 0; i < childShapes.size(); i++) {
//                    String type = childShapes.getJSONObject(i).getJSONObject("stencil").getString("id");
//                    if (StringUtil.equalsIgnoreCase("StartNoneEvent", type)) {
//                        return childShapes.getJSONObject(i).getString("resourceId");
//                    }
//                }
//            }
            return null;
        }

        /**
         * @Author:光华科技-软件研发部
         * @Date:2021/3/3
         * @Param:[json]
         * @return:java.lang.String
         * @Description:判断开始节点的下一/多节点是否有审批人
         */
        public static boolean checkHaveAssignee (String json){
            boolean flag = true;
            JSONObject map = new JSONObject();
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray childShapes = jsonObject.getJSONArray("childShapes");
            // 开始节点的resourceId
            String resourceId = childShapes.getJSONObject(0).getString("resourceId");
            // 获取开始节点的outgoingIds
            JSONArray outgoingArrays = childShapes.getJSONObject(0).getJSONArray("outgoing");
            // 放到map中
            for (int i = 0; i < childShapes.size(); i++) {
                JSONObject childShape = childShapes.getJSONObject(i);
                map.put(childShape.getString("resourceId"), childShape);
            }
            // 获取开始节点的下一/多个用户任务
            JSONArray userTaskOfSd = getUserTaskOfSd(outgoingArrays, map, null);
            for (int i = 0; i < userTaskOfSd.size(); i++) {
                String userTaskResId = userTaskOfSd.getString(i);
                JSONObject usertaskassignment = map.getJSONObject(userTaskResId).getJSONObject("properties").getJSONObject("usertaskassignment");
                JSONArray fKtableEM = usertaskassignment.getJSONObject("assignment").getJSONArray("FKtableEM");
                if (Collections.isEmpty(fKtableEM)) {
                    // 无审批人
                    flag = false;
                }
            }
            return flag;
        }

        /**
         * @Author:光华科技-软件研发部
         * @Date:2021/3/4
         * @Param:[map, taskArrays]
         * @return:com.alibaba.fastjson.JSONArray
         * @Description:获取开始节点下的用户任务
         */
        public static JSONArray getUserTaskOfSd (JSONArray outgoingAry, JSONObject map, JSONArray taskArrays){
            if (Collections.isEmpty(taskArrays)) {
                taskArrays = new JSONArray();
            }
            for (int i = 0; i < outgoingAry.size(); i++) {
                String resourceId = outgoingAry.getJSONObject(i).getString("resourceId");
                JSONObject outgoingObj = map.getJSONObject(resourceId);
                String stencilId = outgoingObj.getJSONObject("stencil").getString("id");
                if (StringUtils.equals(stencilId, "UserTask")) {
                    taskArrays.add(resourceId);
                } else {
                    JSONArray nextNodeArray = new JSONArray();
                    JSONObject target = outgoingObj.getJSONObject("target");
                    if (!Collections.isEmpty(target)) {
                        nextNodeArray.add(target);
                    } else {
                        nextNodeArray = outgoingObj.getJSONArray("outgoing");
                    }
                    return getUserTaskOfSd(nextNodeArray, map, taskArrays);
                }
            }
            return taskArrays;
        }
    /**
     * 将json对象转换为HashMap
     * @param json
     * @return
     */
    public static Map<String, Object> parseJSON2Map(net.sf.json.JSONObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 最外层解析
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            // 如果内层还是json数组的话，继续解析
            if (v instanceof net.sf.json.JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<net.sf.json.JSONObject> it = ((net.sf.json.JSONArray) v).iterator();
                while (it.hasNext()) {
                    net.sf.json.JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2));
                }
                map.put(k.toString(), list);
            } else if (v instanceof net.sf.json.JSONObject) {
                // 如果内层是json对象的话，继续解析
                map.put(k.toString(), parseJSON2Map((net.sf.json.JSONObject) v));
            } else {
                // 如果内层是普通对象的话，直接放入map中
                map.put(k.toString(), v);
            }
        }
        return map;
    }
    /**
     * 将json字符串转换为Map
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJSONstr2Map(String jsonStr) {
        net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(jsonStr);
        Map<String, Object> map = parseJSON2Map(json);
        return map;
    }
}

