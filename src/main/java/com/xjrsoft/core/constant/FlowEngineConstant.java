package com.xjrsoft.core.constant;

/**
* @Author:湘北智造-框架开发组
* @Date:2020/10/19
* @Description:工作流配置类
*/
public class FlowEngineConstant {

	public static final String FLOWABLE_BASE_PACKAGES = "org.flowable.ui";

	public static final String SUFFIX = ".bpmn20.xml";

	public static final String ACTIVE = "active";

	public static final String SUSPEND = "suspend";

	public static final String STATUS_TODO = "todo";

	public static final String STATUS_CLAIM = "claim";

	public static final String STATUS_SEND = "send";

	public static final String STATUS_DONE = "done";

	public static final String STATUS_FINISHED = "finished";

	public static final String STATUS_UNFINISHED = "unfinished";

	public static final String STATUS_FINISH = "finish";

	public static final String START_EVENT = "startEvent";

	public static final String END_EVENT = "endEvent";

	// 按钮的值
	/**
	 * 同意
	 */
	public static final String AGREE = "agree_";

	/**
	 * 不同意
	 */
	public static final String DISAGREE = "disagree_";

	/**
	 * 驳回
	 */
	public static final String REJECT = "reject_";

	/**
	 * 结束
	 */
	public static final String FINISH = "finish_";

	// 人员审批条件
	/**
	 * 岗位
	 */
	public static final String type_post = "1";

	/**
	 * 角色
	 */
	public static final String type_department = "2";

	/**
	 * 用户
	 */
	public static final String type_user = "3";

	/**
	 * 指定节点审批人
	 */
	public static final String specify_node = "4";

	/**
	 * 节点条件
	 */
	public static final String node_limication = "5";

	/**
	 * 表单字段
	 */
	public static final String form_field = "6";

	/**
	 * 同一部门
	 */
	public static final String same_department = "sameDepartment";

	/**
	 * 同一公司
	 */
	public static final String same_company = "sameCompany";

	/**
	 * 发起人同一部门
	 */
	public static final String start_user_depart = "initiatorSameDepartment";

	/**
	 * 发起人同一公司
	 */
	public static final String start_user_company = "initiatorSameCompany";

	// 流程任务命名规则
	/**
	 * 模板编号
	 */
	public static final String nwfScheme_id = "nwfSchemeId";

	/**
	 * 模板名称
	 */
	public static final String nwfScheme_name = "nwfSchemeName";

	/**
	 * 模板分类
	 */
	public static final String nwfScheme_category = "nwfSchemeCategory";

	/**
	 * 模板备注
	 */
	public static final String nwfScheme_description = "nwfSchemeDescription";

	/**
	 * 发起人名称
	 */
	public static final String start_user_name = "startUserName";

	public static final String processInstanceId = "processInstanceId";

}
