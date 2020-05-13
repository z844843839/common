package com.crt.common.constant;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2018/7/25 14:57
 * @Description 用户上下文常量
 **/
public class Constants {
    /**
     * 用户ID 在header中的名称
     */
    public static final String USER_ID = "userId";
    /**
     * webgisuserid 在header中的名称
     */
    public static final String WEBGIS_USER_ID = "webgisUserId";
    /**
     * 公司ID 在header中的名称
     */
    public static final String CORP_ID = "corpId";
    /**
     * 组织结构ID 在header中的名称
     */
    public static final String ORG_ID = "orgId";

    public static final String PARENT_ID = "parentId";

    /**
     * HashMap使用如下构造方法进行初始化，
     * 如果暂时无法确定集合大小，那么指定默认值（16）即可
     */
    public static final int HASHMAP_DEFAULT_SIZE = 16;

    /**
     * 工作流完成状态
     */
    public static final String WORK_FLOW_STATUS_COMPLETE = "complete";

    /**
     * 工作流审批状态
     */
    public static final String WORK_FLOW_STATUS_ACTIVE = "active";

    /**
     * 工作流驳回状态
     */
    public static final String WORK_FLOW_STATUS_ROLLBACK = "rollback";


    /**
     * 工作流终止状态
     */
    public static final String WORK_FLOW_STATUS_DELETE = "delete";

    /**
     * 无维修价值
     */
    public static final String WORK_FLOW_STATUS_NOREPAIRVALUE = "noRepairValue";

    /**
     * 无维修价值
     */
    public static final String WORK_FLOW_STATUS_WITHDRAW = "withdraw";

    /**
     * 工作流分类-采购合同
     */
    public static final String WORK_FLOW_DOC_TYPE_CONTRACT_PURCH = "采购合同";

    /**
     * 工作流分类-销售合同
     */
    public static final String WORK_FLOW_DOC_TYPE_CONTRACT_SALE = "销售合同";

    /**
 * 工作流分类-销售合同
 */
public static final String WORK_FLOW_DOC_TYPE_PROJECT_GROUP = "项目组";

    /**
     * 工作流分类-销售报价
     */
    public static final String WORK_FLOW_DOC_TYPE_SALES_QUOTATION = "销售报价";

    /**
     * 工作流分类-运输方案
     */
    public static final String WORK_FLOW_DOC_TYPE_TRANSPORT_SCHEME = "运输方案";

    /**
     * 工作流分类-投箱管理
     */
    public static final String WORK_FLOW_DOC_TYPE_SIGN_IN = "投箱管理";

    /**
     * 工作流分类- 调令申请
     */
    public static final String WORK_FLOW_DOC_TYPE_SHIF_ORDER = "调令申请";

    /**
     * 工作流分类-箱源匹配
     */
    public static final String WORK_FLOW_DOC_TYPE_SIGN_IN_RELATION = "箱源匹配";

    /**
     * 工作流分类-退箱申请
     */
    public static final String WORK_FLOW_DOC_TYPE_SIGN_OUT = "退箱申请";

    /**
     * 工作流分类-应收结算单
     */
    public static final String WORK_FLOW_DOC_TYPE_AR_SETTLEMENT_BILL = "应收结算单";

    /**
     * 工作流分类-应收结算单业务收入
     */
    public static final String WORK_FLOW_DOC_TYPE_AR_SETTLEMENT_BILL_INCOME = "应收结算单业务收入";

    /**
     * 工作流分类-应收发票
     */
    public static final String WORK_FLOW_DOC_TYPE_AR_INVOICE = "应收发票";

    /**
     * 工作流分类-收款收据
     */
    public static final String WORK_FLOW_DOC_TYPE_AR_RECEIPT = "收款收据";

    /**
     * 工作流分类-付款申请
     */
    public static final String WORK_FLOW_DOC_TYPE_APPLICATION_PAYMENT = "付款申请";

    /**
     * 工作流分类-承兑汇票
     */
    public static final String WORK_FLOW_DOC_TYPE_ACCEPTANCE_BILL = "承兑汇票";

    /**
     * 工作流标题的“申请”后缀
     */
    public static final String WORK_FLOW_TITLE_SUFFIX = "申请";

    /**
     * 工作流分类-增值服务费
     */
    public static final String WORK_FLOW_DOC_TYPE_ADDED_VALUE = "增值服务费";

    /**
     * 工作流标题的“-”
     */
    public static final String WORK_FLOW_TITLE_MIDDLE_LINE = "-";

    /**
     * bpmProcessId
     */
    public static final String WORK_FLOW_BPMPROCESS_ID = "bpmProcessId";

    /**
     * entity_id
     */
    public static final String WORK_FLOW_ENTITY_ID = "entity_id";

    /**
     * doc_type
     */
    public static final String WORK_FLOW_DOC_TYPE = "doc_type";

    /**
     * doc_type_name
     */
    public static final String WORK_FLOW_DOC_TYPE_NAME = "doc_type_name";

    /**
     * entity_tiltle
     */
    public static final String WORK_FLOW_ENTITY_TILTLE = "entity_tiltle";

    /**
     * bpm_details_url
     */
    public static final String WORK_FLOW_BPM_DETAILS_URL = "bpm_details_url";
    /**
     * 工作流发起部门id
     */
    public static final String WORK_FLOW_ORG_ID = "work_flow_org_id";
    /**
     * 工作流发起部门名称
     */
    public static final String WORK_FLOW_ORG_NAME = "work_flow_org_name";
    /**
     * 单据号
     */
    public static final String WORK_FLOW_DOCUMENT_NO = "work_flow_document_no";
    /**
     * 合作伙伴id  客户id/箱修厂id
     */
    public static final String WORK_FLOW_PARTNER_ID = "work_flow_partner_id";
    /**
     * 合作伙伴  客户名称/箱修厂名称
     */
    public static final String WORK_FLOW_PARTNER_NAME = "work_flow_partner_name";
	 /**
     * 详情页面预览时候添加的模类型参数
     */
    public static final String WORK_FLOW_PREVIEWID = "work_flow_previewId";

    /**
     * 菜单
     */
    public static final int MENU = 0;
    /**
     * 资源
     */
    public static final int RESOURCE = 1;
    /**
     * 系统
     */
    public static final int FROM_SYSTEM = 0;
    /**
     * 平台
     */
    public static final int FROM_PLATFORM = 1;
    /**
     * 门户
     */
    public static final int FROM_PORTAL = 2;
    /**
     * 删除 1
     */
    public static final int DELETE = 1;
    /**
     * 未删除 0
     */
    public static final int NOT_DELETE = 0;
    /**
     * 根节点
     */
    public static final String ROOT_NODE = "0";


    /**
     * 需要透传的header的名称
     */
    public static final String[] HEADERS = {USER_ID, WEBGIS_USER_ID, CORP_ID, ORG_ID, PARENT_ID};

    //岗位管理
    public final static int POST_MANAGER = 840;
    //角色管理
    public final static int ROLE_MANAGER = 830;
    //组织角色设置
    public final static int ORG_ROLE_MANAGER = 823;
    //组织管理
    public final static int ORG_MANAGER = 820;
    //用户管理
    public final static int USER_MANAGER = 810;
    //菜单管理
    public final static int MENU_MANAGER = 800;
    //会计凭证
    public final static int ACCOUNTING_VOUCHERS = 660;
    //应付发票
    public final static int INVOICE_PAYABLE = 650;
    //应收发票
    public final static int AR_INVOICE = 640;
    //付款申请
    public final static int APPLICATION_PAYMENT = 630;
    //承兑汇票
    public final static int ACCEPTANCE_BILL = 631;
    //付款单
    public final static int PAYMENT_SLIP = 620;
    //收据信息表
    public static int RECEIPT_INFORMATION_FORM = 615;
    //收款单
    public final static int RECEIPT = 610;
    //箱配合出库单
    public final static int BOX_MATCHING_OUTLET_BILL = 582;
    //箱配合入库单
    public final static int BOX_MATCHING_WAREHOUSING_BILL = 581;
    //箱配件申请单
    public final static int APPLICATION_FORM_BOX_ACCESSORIES_BILL = 580;
    //箱报废单
    public final static int BOX_SCRAP_BILL = 570;
    //维修结果单
    public final static int MAINTENANCE_RESULT_BILL = 565;
    //维修申请单
    public final static int MAINTENANCE_APPLICATION_BILL = 560;
    //定检结果单
    public final static int VERIFICATION_RESULT_BILL = 558;
    //送检单
    public final static int INSPECTION_SLIP = 555;
    //定检申请单
    public final static int VERIFICATION_APPLICATION_BILL = 553;
    //定检计划单
    public final static int VERIFICATION_PLAN_BILL = 550;
    //验箱单
    public final static int CHECK_BOX_BILL = 540;
    //Constants(包括自备箱进出)L)
    public final static int BOX_IN_AND_OUT = 530;
    //验箱收货单
    public final static int INSPECTION_RECEIPT = 520;
    //箱服务合同
    public final static int BOX_SERVICE_CONTRACT = 510;
    //运量确认单
    public final static int TRAFFIC_CONFIRMATION_BILL = 480;
    //作业单
    public final static int WORKSHEET = 470;
    //供应商订单
    public final static int SUPPLIER_ORDERS = 460;
    //客户订单
    public final static int CUSTOMER_ORDERS = 450;
    //客户运输计划
    public final static int CUSTOMER_TRANSPORTATION_PLAN = 440;
    //调令单
    public final static int CALL_ORDER = 430;
    //退箱单
    public final static int RETURN_BILL = 420;
    //排箱单
    public final static int ARRANGE_BILL = 410;
    //箱匹配
    public final static int SIGN_IN_RELATION = 411;
    //应付结算单
    public final static int STATEMENT_PAYABLE = 340;
    //采购合同
    public final static int PROCUREMENT_CONTRACT = 330;
    //供应商准入
    public final static int SUPPLIER_ACCESS = 320;
    //供应商询价
    public final static int SUPPLIER_INQUIRY = 310;
    //结算方式
    public final static int SETTLEMENT_METHOD = 299;
    //归属公司
    public final static int OWNERSHIP_COMPANY = 298;
    //付款方式
    public final static int PAYMENT_WAY = 297;
    //全程客户类型
    public final static int WHOLE_CUSTOMER_TYPE = 296;
    //项目组
    public final static int PROJECT_GROUP = 170;
    //应收结算单
    public final static int ACCOUNTS_RECEIVABLE_BILL = 270;
    //应收结算单业务收入
    public final static int ACCOUNTS_RECEIVABLE_BILL_INCOME = 275;
    //销售合同
    public final static int SALES_CONTRACT = 260;
    //客户信用评估
    public final static int CUSTOMER_CREDIT_ASSESSMENT = 250;
    //客户报价
    public final static int CUSTOMER_QUOTATION = 240;
    //物流运输方案
    public final static int LOGISTICS_TRANSPORTATION_SCHEME = 230;
    //业务可行性分析
    public final static int BUSINESS_FEASIBILITY_ANALYSIS = 220;
    //客户意向表
    public final static int CUSTOMER_INTENTION_STATEMENT = 210;
    //员工主数据
    public final static int EMPLOYEE_MASTER_DATA = 150;
    //地理地标
    public final static int GEOGRAPHICAL_LANDMARKS = 140;
    //业务项目主数据
    public final static int BUSINESS_PROJECT_MASTER_DATA = 130;
    //品类主数据
    public final static int CATEGORY_MASTER_DATA = 120;
    //箱技术参数
    public final static int TECHNICAL_PARAMETERS_BOX = 114;
    //箱修字典
    public final static int BOX_REPAIR_DICTIONARY = 112;
    //箱主数据
    public final static int BOX_MASTER_DATA = 110;
    //客商数据
    public static String CONTAINSTR = "?";

    //增值服务费用
    public final static int ADDED_VALUE = 490;

    public final static int BUSINESS_DATA = 100;
    /**
     * sql操作符
     */
    public final static String LEFT_PARENTHESES = "(";

    public final static String RIGHT_PARENTHESES = ")";

    public final static String SPOT = ".";

    public final static String SPACE = " ";

    public final static String COMMA = ",";

    public final static String SINGLE_QUOTATION_MARK = "'";

    public final static String PERCENTAGE_MARK = "%";

    public final static String NUMBER_TEPY = "bigint,int,tinyint,smallint";

    public final static String FUZZY_QUERY_KEY = "LIKE";

    public final static String CONNECTOR_OR = "OR";

    public final static String CONNECTOR_AND = "AND";

    public final static String SQL_KEY_AS = "AS";

    public final static String SQL_KEY_LIMIT = "LIMIT";

    public final static String SQL_KEY_COUNT = "COUNT";

    public final static String SQL_KEY_WHERE = "WHERE";

    public final static String SQL_KEY_FROM = "FROM";

    public final static String SQL_KEY_GROUP_BY = "GROUP BY";

    public final static String SQL_KEY_ORDER_BY = "ORDER BY";

    public final static String SQL_KEY_ON = "ON";

    /**
     * 预检单
     */
    public final static int FIX_CHECK = 561;

    /**
     * 修竣单
     */
    public final static int FIX_FINISH = 562;

    /**
     * 工作流启动地址
     */
    public final static String PROCESS_START_URL_SUFFIX = "bpm/operation/process-operation-confirmStartProcess";
    /**
     * 工作流驳回后再提交地址
     */
    public final static String PROCESS_RESET_URL_SUFFIX = "bpm/operation/task-operation-completeTask";
    /**
     * 工作流终止地址
     */
    public final static String PROCESS_STOP_URL_SUFFIX = "/bpm/workspace-endProcessInstance";


    /**
     * 数字集合
     */
    public final static Integer NUMBER_NEGATIVE_ONE = -1;
    public final static Integer NUMBER_NEGATIVE_THREE = -3;
    public final static Integer NUMBER_ZERO = 0;
    public final static Integer NUMBER_ONE = 1;
    public final static Integer NUMBER_TWO = 2;
    public final static Integer NUMBER_THREE = 3;
    public final static Integer NUMBER_FOUR = 4;
    public final static Integer NUMBER_FIVE = 5;
    public final static Integer NUMBER_SIX = 6;
    public final static Integer NUMBER_SEVEN = 7;
    public final static Integer NUMBER_EIGHT = 8;
    public final static Integer NUMBER_NINE = 9;
    public final static Integer NUMBER_TEN = 10;
    public final static Integer NUMBER_ELEVEN = 11;
    public final static Integer NUMBER_TWELVE = 12;
    public final static Integer NUMBER_THIRTEEN = 13;
    public final static Integer NUMBER_FOURTEEN = 14;
    public final static Integer NUMBER_FIFTEEN = 15;
    public final static Integer NUMBER_SIXTEEN = 16;
    public final static Integer NUMBER_SEVENTEEN = 17;
    public final static Integer NUMBER_EIGHTEEN = 18;
    public final static Integer NUMBER_NINETEEN = 19;
    public final static Integer NUMBER_TWENTY = 20;
    public final static Integer NUMBER_THIRTY = 30;
    /***闰年天数***/
    public final static Integer LEAP_YEAR_DAYS = 366;



    /**
     * 单据状态 BILL_STATUS
     */

    /**
     * 关闭
     */
    public final static Integer BILL_STATUS_CLOSED = -2;
    /**
     * 驳回
     */
    public final static Integer BILL_STATUS_REJECT = -1;
    /**
     *  保存
     */
    public final static Integer BILL_STATUS_SAVE = 0;
    /**
     *  已提交（待审批）
     */
    public final static Integer BILL_STATUS_SUBMIT = 100;
    /**
     *  已审批（供下游查询）
     */
    public final static Integer BILL_STATUS_APPROVAL = 300;
    /**
     *  已生效（下游已使用）
     */
    public final static Integer BILL_STATUS_EFFECTIVE = 500;

    /**
     * 超级管理员登陆名称
     */
    public final static String SUPER_ADMINISTRATOR = "super";

    public final static String DEFAULT_OPERATER = "系统管理员";

    /**
     * java基础类型数组
     */
    public static String[] BASIC_TYPES = {"java.lang.Integer", "java.lang.Double",
            "java.lang.Float", "java.lang.Long", "java.lang.Short",
            "java.lang.Byte", "java.lang.Boolean", "java.lang.Char",
            "java.lang.String", "int", "double", "long", "short", "byte",
            "boolean", "char", "float"};

    public static String OPERATE_TYPE_SAVE = "新增";

    public static String OPERATE_TYPE_MODIFY = "修改";

    public static String OPERATE_TYPE_DELETE = "删除";

    /**
     * 返回状态200 成功
     */
    public static Integer STATUS_CODE_SUCCESS = 200;
    /**
     * 查询无错误，返回无结果
     */
    public static Integer STATUS_NO_RESULT = 304;
    /**
     * 返回状态500 内部错误
     */
    public static Integer INTERNAL_SERVER_ERROR = 500;
    /**
     * 操作结果0 成功
     */
    public static int RESULT_SUCCESS = 0;
    /**
     * 操作结果1 失败
     */
    public static int RESULT_FAIL = 1;
    /**
     * 操作结果2 未知
     */
    public static int RESULT_UNKNOWN = 2;

    /**
     * 行级权限内置变量1 当前用户ID
     */
    public static String BUILT_IN_VARIABLES1 = "当前用户ID";
    /**
     * 行级权限内置变量2 当前用户上级ID
     */
    public static String BUILT_IN_VARIABLES2 = "当前用户上级ID";
    /**
     * 行级权限内置变量3 当前用户所属专业公司ID
     */
    public static String BUILT_IN_VARIABLES3 = "当前用户所属专业公司ID";
    /**
     * 行级权限内置变量4 当前用户所属区域公司ID
     */
    public static String BUILT_IN_VARIABLES4 = "当前用户所属区域公司ID";
    /**
     * 行级权限内置变量5 当前用户所属部门ID
     */
    public static String BUILT_IN_VARIABLES5 = "当前用户所属部门ID";
    /**
     * 行级权限内置变量6 当前用户所属部门名称
     */
    public static String BUILT_IN_VARIABLES6 = "当前用户所属部门名称";
    /**
     * 行级权限内置变量7 当前用户所属专业公司CODE
     */
    public static String BUILT_IN_VARIABLES7 = "当前用户所属专业公司CODE";
    /**
     * 行级权限内置变量8 当前用户所属区域公司CODE
     */
    public static String BUILT_IN_VARIABLES8 = "当前用户所属区域公司CODE";


}
