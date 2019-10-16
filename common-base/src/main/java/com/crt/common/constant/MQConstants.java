package com.crt.common.constant;

/**
 * @Author wanglonglong@e6yun.com
 * @Date 2019/8/21 14:57
 * @Description mq常量
 **/
public class MQConstants {

    /**
     * 工作流交换器名称
     */
    public static final String WORK_FLOW_EXCHANGE = "crt_work_flow_exchange";

    /**
     * 工作流ROUTINGKEY前缀
     */
    public static final String WORK_FLOW_ROUTINGKEY_PREFIX = "crt_work_flow_routingkey";

    /**
     * 工作流合同队列前缀
     */
    public  static final  String CONTRACT_QUEUE_PREFIX="com.crt.middle.module.contract.queue_";

    /**
     * 工作流箱管理队列前缀
     */
    public  static final  String ATCM_QUEUE_PREFIX="com.crt.middle.module.atcm.queue_";

    /**
     * 工作流意向队列前缀
     */
    public  static final  String PRECONT_QUEUE_PREFIX="com.crt.middle.module.precont.queue_";
}
