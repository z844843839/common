package com.crt.common.aspect.log;

import java.util.Map;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/5/24 14:29
 * @Description 操作日志实体类
 **/
public class OperateLogPojo {
    String method;
    String template;
    String log;
    /**
     * 设置表中的所有数据
     */
    Map<String, String> datas;

    public OperateLogPojo(OperateLogPojo operateLogPojo, String log) {
        this.method = operateLogPojo.getMethod();
        this.template = operateLogPojo.getTemplate();
        this.datas = operateLogPojo.getDatas();
        this.log = log;
    }

    public OperateLogPojo(String method, String template, Map<String, String> datas) {
        this.method = method;
        this.template = template;
        this.datas = datas;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Map<String, String> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, String> datas) {
        this.datas = datas;
    }
}
