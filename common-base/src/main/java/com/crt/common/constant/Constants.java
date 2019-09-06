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
     * 工作流分类-采购合同
     */
    public static final String WORK_FLOW_DOC_TYPE_CONTRACT_PURCH = "采购合同";
    
    /**
     * 工作流分类-销售合同
     */
    public static final String WORK_FLOW_DOC_TYPE_CONTRACT_SALE = "销售合同";

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
     *工作流分类- 调令申请
     */
    public static final String WORK_FLOW_DOC_TYPE_SHIF_ORDER = "调令申请";

    /**
     * 工作流分类-箱源匹配
     */
    public static final String WORK_FLOW_DOC_TYPE_SIGN_IN_RELATION = "箱源匹配";

    /**
     * 工作流分类-退箱管理
     */
    public static final String WORK_FLOW_DOC_TYPE_SIGN_OUT = "退箱管理";

    /**
     * 工作流标题的“申请”后缀
     */
    public static final String WORK_FLOW_TITLE_SUFFIX = "申请";

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
     * 需要透传的header的名称
     */
    public static final String[] HEADERS = {USER_ID,WEBGIS_USER_ID,CORP_ID,ORG_ID,PARENT_ID};
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
    public final static int  ACCOUNTING_VOUCHERS = 660;
    //应付发票
    public final static int  INVOICE_PAYABLE = 650;
    //应收发票
    public  final static int AR_INVOICE = 640;
    //付款申请
    public final static  int  APPLICATION_PAYMENT = 630 ;
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
    public final static int  APPLICATION_FORM_BOX_ACCESSORIES_BILL = 580;
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
    public final static int  VERIFICATION_PLAN_BILL= 550;
    //验箱单
    public final static int CHECK_BOX_BILL = 540;
    //Constants(包括自备箱进出)L)
    public final static int  BOX_IN_AND_OUT = 530;
    //验箱收货单
    public final static int  INSPECTION_RECEIPT = 520;
    //箱服务合同
    public final static int  BOX_SERVICE_CONTRACT = 510;
    //运量确认单
    public final static int TRAFFIC_CONFIRMATION_BILL = 480;
    //作业单
    public final static int WORKSHEET = 470;
    //供应商订单
    public final static int  SUPPLIER_ORDERS = 460;
    //客户订单
    public final static int  CUSTOMER_ORDERS = 450;
    //客户运输计划
    public final static int CUSTOMER_TRANSPORTATION_PLAN = 440;
    //调令单
    public final static int  CALL_ORDER = 430;
    //退箱单
    public final static int  RETURN_BILL = 420;
    //排箱单
    public final static int ARRANGE_BILL = 410;
    //箱匹配
    public final static int SIGN_IN_RELATION = 411;
    //应付结算单
    public final static int  STATEMENT_PAYABLE = 340;
    //采购合同
    public final  static int PROCUREMENT_CONTRACT = 330;
    //供应商准入
    public final static int SUPPLIER_ACCESS = 320;
    //供应商询价
    public final static int SUPPLIER_INQUIRY = 310;
    //结算方式
    public final static int SETTLEMENT_METHOD = 299;
    //归属公司
    public final static int  Ownership_Company = 298;
    //付款方式
    public final static int  Payment_WAY = 297;
    //全程客户类型
    public final static int WHOLE_CUSTOMER_TYPE = 296;
    //应收结算单
    public final static int ACCOUNTS_RECEIVABLE_BILL= 270;
    //销售合同
    public final static int SALES_CONTRACT = 260;
    //客户信用评估
    public final static int CUSTOMER_CREDIT_ASSESSMENT = 250;
    //客户报价
    public final static int CUSTOMER_QUOTATION = 240;
    //物流运输方案
    public final static int  LOGISTICS_TRANSPORTATION_SCHEME = 230;
    //业务可行性分析
    public final static int  BUSINESS_FEASIBILITY_ANALYSIS = 220;
    //客户意向表
    public final static int  CUSTOMER_INTENTION_STATEMENT = 210;
    //员工主数据
    public final static int  EMPLOYEE_MASTER_DATA = 150;
    //地理地标
    public final static int  GEOGRAPHICAL_LANDMARKS = 140;
    //业务项目主数据
    public final static int  BUSINESS_PROJECT_MASTER_DATA = 130;
    //品类主数据
    public final static int  CATEGORY_MASTER_DATA = 120;
    //箱技术参数
    public final static int  TECHNICAL_PARAMETERS_BOX = 114;
    //箱修字典
    public final static int  BOX_REPAIR_DICTIONARY = 112;
    //箱主数据
    public final static int  BOX_MASTER_DATA = 110;
    //客商数据

    public static String CONTAINSTR = "?";

    public final static int  BUSINESS_DATA = 100;
    /**
     * sql操作符
     */
    public final static String LEFT_PARENTHESES = "(";

    public final static String RIGHT_PARENTHESES = ")";

    public final static String SPOT = ".";

    public final static String SPACE = " ";

    public final static String SINGLE_QUOTATION_MARK = "'";

    public final static String PERCENTAGE_MARK = "%";

    public final static String NUMBER_TEPY = "bigint,int,tinyint,smallint";

    public final static String FUZZY_QUERY_KEY = "LIKE";

    public final static String CONNECTOR_OR = "OR";

    public final static String CONNECTOR_AND = "AND";

}
