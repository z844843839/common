package com.crt.common.aspect.log;

import com.alibaba.fastjson.JSON;
import com.crt.common.aspect.base.E6InterceptorOfContain;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author liupengfei@e6yun.com
 * @Date 2019/5/15 17:35
 * @Description 日志拦截器，使用时要用Configuration类手工配置
 **/
public class E6Interceptor4OperateLog implements E6InterceptorOfContain {
    private static Logger logger = LoggerFactory.getLogger(E6Interceptor4OperateLog.class);

    JdbcTemplate jdbcTemplate;
    /**
     * 执行查询的SQL
     */
    String DEFAULT_SQL = "SELECT * FROM config_log_template";
    String sql = DEFAULT_SQL;
    /**
     * 日志输出 消费者
     */
    Consumer<OperateLogPojo> operateLogPojoConsumer;

    Map<String, OperateLogPojo> logConfigMap;

    public E6Interceptor4OperateLog(DataSource dataSource) {
        new E6Interceptor4OperateLog(dataSource,DEFAULT_SQL,null);
    }
    public E6Interceptor4OperateLog(DataSource dataSource,Consumer<OperateLogPojo> consumer) {
        new E6Interceptor4OperateLog(dataSource,DEFAULT_SQL,consumer);
    }
    public E6Interceptor4OperateLog(DataSource dataSource,String sql,Consumer<OperateLogPojo> consumer) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.sql = sql;
        this.operateLogPojoConsumer = consumer;
        try{
            loadConfig();
        }catch (Exception ex){
            logger.error("从数据库初始化日志配置发生错误,sql={}",sql,ex);
        }
    }

    public void loadConfig(){
        Map<String, OperateLogPojo> configMap = new HashMap();
        jdbcTemplate.query(this.sql, (ResultSet resultSet) -> {
            do{
                String method = resultSet.getString(1);
                String template = resultSet.getString(2);
                //把其他列都存起来
                Map<String, String> datas = new HashMap();
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int count = rsmd.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    String key = rsmd.getColumnLabel(i);
                    String value = resultSet.getString(i);
                    datas.put(key, value);
                }
                OperateLogPojo operateLogPojo = new OperateLogPojo(method,template,datas);
                configMap.put(method,operateLogPojo);
            }while (resultSet.next());
        });
        logConfigMap = configMap;
        logger.info("加载日志配置{}条",logConfigMap.size());
    }

    @Override
    public Object doBefore(String className,String methodName, ProceedingJoinPoint joinPoint) throws Throwable {
        String signatureStr = className+"."+methodName;
        try {
            Object[] args = joinPoint.getArgs();
            String log = logTemplate(args, logConfigMap.get(signatureStr).getTemplate());
            if(operateLogPojoConsumer!=null) {
                operateLogPojoConsumer.accept(new OperateLogPojo(logConfigMap.get(signatureStr), log));
            }else{
                logger.info(log);
            }
        } catch (Exception ex) {
            logger.error("日志拦截器异常", ex);
        }
        //不返回任何东西xx
        return null;
    }

    @Override
    public boolean containsSignature(String className,String methodName) {
        if(logConfigMap==null || logConfigMap.isEmpty()){
            return false;
        }
        String signatureStr = className+"."+methodName;
        return logConfigMap.containsKey(signatureStr);
    }

    /**
     * 通过参数填充日志模板
     * @param objs 参数列表
     * @param template 模板
     * @return
     */
    public String logTemplate(Object[] objs,String template){
        //从模板中截取出要替换的变量
        Pattern pattern = Pattern.compile("\\{.*?\\}");
        Matcher matcher = pattern.matcher(template);
        Map<String,String> paramsMap = new HashMap<>();
        while(matcher.find()){
            String key = matcher.group(0);
            String value = key.replace("{","").replace("}","");
            paramsMap.put(key,value);
        }
        for(Map.Entry<String,String> entry:paramsMap.entrySet()) {
            String paramName = entry.getValue();
            Object result = getParamFromObjs(objs,paramName);
            //这里拿到的结果 有可能不是String
            String resultStr = result==null?"*":result.toString();
            entry.setValue(resultStr);
            template = StringUtils.replaceAll(template,"\\"+entry.getKey(),resultStr);
        }
        return template;
    }
    public Object getParamFromObjs(Object[] objs,String expression){
        int index = -1;
        String first = null;
        if(StringUtils.isNumeric(expression)){
            first=expression;
            index = Integer.parseInt(expression);
            expression="";
        }else {
            first = expression.split("\\.")[0];
            //判断是否是数字
            if (!StringUtils.isNumeric(first)) {
                return null;
            }
            index = Integer.parseInt(first);
            expression = expression.substring(first.length()+1);
        }
        //判断所取参数的位置index是否超过方法的参数个数
        if(index>=objs.length){
            return null;
        }
        return getParamFromObj(objs[index],expression);
    }
    public Object getParamFromObj(Object obj,String expression){
        if(obj==null){
            return null;
        }
        if(StringUtils.isEmpty(expression)){
            return getShow(obj);
        }
        String fieldName = expression.split("\\.")[0];
        //反射取到这个属性
        /* 得到类中的所有属性集合 */
        Class userCla = obj.getClass();
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            if (!f.getName().equals(fieldName)) {
                continue;
            }
            f.setAccessible(true); // 设置些属性是可以访问的
            try {
                Object val = f.get(obj);
                //如果表达式已经晚了
                if(expression.equals(fieldName)){
                    return getShow(val);
                }
                //表达式还有后续
                expression = expression.substring(fieldName.length() + 1);
                return getParamFromObj(val, expression);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //没找到 配置出错
        return null;
    }

    public Object getShow(Object obj){
        //基本类型 直接返回
        if(obj.getClass().getName().indexOf("java.lang.")>-1){
            return obj;
        }
        //非基本类型，就返回json
        return JSON.toJSONString(obj);
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setOperateLogPojoConsumer(Consumer<OperateLogPojo> operateLogPojoConsumer) {
        this.operateLogPojoConsumer = operateLogPojoConsumer;
    }
}
