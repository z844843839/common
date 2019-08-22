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
     * 需要透传的header的名称
     */
    public static final String[] HEADERS = {USER_ID,WEBGIS_USER_ID,CORP_ID,ORG_ID,PARENT_ID};

    //会计凭证
    public static int  ACCOUNTING_VOUCHERS = 660;
    //应付发票
    public static int  INVOICE_PAYABLE = 650;
    //应收发票
    public static int AR_INVOICE = 640;
    //付款申请
    public static  int  APPLICATION_PAYMENT = 630 ;
    //付款单
    public static int PAYMENT_SLIP = 620;
    //收据信息表
    public static int RECEIPT_INFORMATION_FORM = 615;
    //收款单
    public static int RECEIPT = 610;
    //箱配合出库单
    public static int BOX_MATCHING_OUTLET_BILL = 582;
    //箱配合入库单
    public static int BOX_MATCHING_WAREHOUSING_BILL = 581;
    //箱配件申请单
    public static int  APPLICATION_FORM_BOX_ACCESSORIES_BILL = 580;
    //箱报废单
    public static int BOX_SCRAP_BILL = 570;
    //维修结果单
    public static int MAINTENANCE_RESULT_BILL = 565;
    //维修申请单
    public static int MAINTENANCE_APPLICATION_BILL = 560;
    //定检结果单
    public static int VERIFICATION_RESULT_BILL = 558;
    //送检单
    public static int INSPECTION_SLIP = 555;
    //定检申请单
    public static int VERIFICATION_APPLICATION_BILL = 553;
    //定检计划单
    public static int  VERIFICATION_PLAN_BILL= 550;
    //验箱单
    public static int Check_BOX_BILL = 540;
    //Constants(包括自备箱进出)L)
    public static int  BOX_IN_AND_OUT = 530;
    //验箱收货单
    public static int  INSPECTION_RECEIPT = 520;
    //箱服务合同
    public static int  BOX_SERVICE_CONTRACT = 510;
    //运量确认单
    public static int TRAFFIC_CONFIRMATION_BILL = 480;
    //作业单
    public static int WORKSHEET = 470;
    //供应商订单
    public static int  SUPPLIER_ORDERS = 460;
    //客户订单
    public static int  CUSTOMER_ORDERS = 450;
    //客户运输计划
    public static int CUSTOMER_TRANSPORTATION_PLAN = 440;
    //调令单
    public static int  CALL_ORDER = 430;
    //退箱单
    public static int  RETURN_BILL = 420;
    //排箱单
    public static int ARRANGE_BILL = 410;
    //应付结算单
    public static int  STATEMENT_PAYABLE = 340;
    //采购合同
    public static int PROCUREMENT_CONTRACT = 330;
    //供应商准入
    public static int supplier_access = 320;
    //供应商询价
    public static int SUPPLIER_INQUIRY = 310;
    //结算方式
    public static int SETTLEMENT_METHOD = 299;
    //归属公司
    public static int  Ownership_Company = 298;
    //付款方式
    public static int  Payment_WAY = 297;
    //全程客户类型
    public static int WHOLE_CUSTOMER_TYPE = 296;
    //应收结算单
    public static int ACCOUNTS_RECEIVABLE_BILL= 270;
    //销售合同
    public static int SALES_CONTRACT = 260;
    //客户信用评估
    public static int CUSTOMER_CREDIT_ASSESSMENT = 250;
    //客户报价
    public static int Customer_Quotation = 240;
    //物流运输方案
    public static int  LOGISTICS_TRANSPORTATION_SCHEME = 230;
    //业务可行性分析
    public static int  BUSINESS_FEASIBILITY_ANALYSIS = 220;
    //客户意向表
    public static int  Customer_Intention_Statement = 210;
    //员工主数据
    public static int  EMPLOYEE_MASTER_DATA = 150;
    //地理地标
    public static int  GEOGRAPHICAL_LANDMARKS = 140;
    //业务项目主数据
    public static int  BUSINESS_PROJECT_MASTER_DATA = 130;
    //品类主数据
    public static int  CATEGORY_MASTER_DATA = 120;
    //箱技术参数
    public static int  TECHNICAL_PARAMETERS_BOX = 114;
    //箱修字典
    public static int  BOX_REPAIR_DICTIONARY = 112;
    //箱主数据
    public static int  BOX_MASTER_DATA = 110;
    //客商数据
    public static int  BUSINESS_DATA = 100;

    public static String CONTAINSTR = "?";
}
