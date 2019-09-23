package com.crt.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.crt.common.constant.Constants;
import com.crt.common.vo.E6Wrapper;
import com.crt.common.vo.E6WrapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 工作流工具类
 * @Author malin@e6yun.com
 * @Created Date: 2019/08/20 16:16
 * @ClassName WorkFlowUtil
 * @Version: 1.0
 */
@Component
@PropertySource(value = {"classpath:workflowsetting.properties"},
        ignoreResourceNotFound = true, encoding = "UTF-8", name = "workflowsetting.properties")
public class WorkFlowUtil {

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowUtil.class);

    private static String url;

    @Value("${workflow.url}")
    public void setUrl(String url) {
        WorkFlowUtil.url = url;
    }

    /**
     * 流程启动方法
     * @param map Map<String,Object>
     * @return
     */
    public static E6Wrapper flowStartup(Map<String,Object> map){
        String workFlowUrl=url+ Constants.PROCESS_START_URL_SUFFIX;
        return WorkFlowUtil.executeWorkFlow(map,workFlowUrl);
    }

    /**
     * 流程驳回后再提交
     * @param map Map<String,Object>
     * @return
     */
    public static E6Wrapper flowReset(Map<String,Object> map){
        String workFlowUrl=url+ Constants.PROCESS_RESET_URL_SUFFIX;
        return WorkFlowUtil.executeWorkFlow(map,workFlowUrl);
    }

    /**
     * 流程驳回后再提交
     * @param map Map<String,Object>
     * @return
     */
    public static E6Wrapper flowStop(Map<String,Object> map){
        String workFlowUrl=url+ Constants.PROCESS_STOP_URL_SUFFIX;
        return WorkFlowUtil.executeWorkFlow(map,workFlowUrl);
    }



    public static E6Wrapper executeWorkFlow(Map<String,Object> map,String workFlowUrl){
        if (null != map && map.size() > 0){
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("Authorization");
            RestTemplate restTemplate = new RestTemplate();
            //api url地址
            MultiValueMap<String, Object> mvMap= new LinkedMultiValueMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                mvMap.add(entry.getKey(),entry.getValue());
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization",token);
            HttpEntity<MultiValueMap> entity = new HttpEntity<>(mvMap,headers);
            logger.info("WorkFlowUtil执行工作流前数据: {}", JSON.toJSONString(entity, SerializerFeature.WriteMapNullValue));
            logger.info("WorkFlowUtil执行工作流的URL: {}", workFlowUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(workFlowUrl,
                    HttpMethod.POST, entity, String.class);
            String result =  exchange.getBody();
            JSONObject json = JSONObject.parseObject(result);
            logger.info("WorkFlowUtil执行工作流返回数据: {}", json);
            if (200 == Integer.parseInt(json.get("code").toString())){
                logger.error(" result ====> "+ json.get("message"));
                return E6WrapperUtil.ok(json);
            }else{
                return E6WrapperUtil.paramError(Integer.parseInt(json.get("code").toString()),json.get("message").toString());
            }
        }
        return E6WrapperUtil.paramError("参数不能未空！");
    }

}
