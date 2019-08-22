package com.crt.common.util;

import com.alibaba.fastjson.JSONObject;
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
            ResponseEntity<String> exchange = restTemplate.exchange(url,
                    HttpMethod.POST, entity, String.class);
            String result =  exchange.getBody();
            JSONObject json = JSONObject.parseObject(result);
            if (200 == Integer.parseInt(json.get("code").toString())){
                logger.error(" result ====> "+ json.get("message"));
                return E6WrapperUtil.ok(json);
            }else{
                return E6WrapperUtil.paramError(Integer.parseInt(json.get("code").toString()),json.get("message").toString());
            }
        }
        return E6WrapperUtil.paramError("参数不能未空！");
    }


//    /**
//     * 订阅MQ 处理消费结果
//     * @param queue  doc_type
//     * @param routineKey
//     */
//    public void resultProcess(String queue,String routineKey){
//        try{
//            mQcroe.mqMessageListenner(queue,routineKey);
//        }catch (IOException e){
//            logger.error(" ioexception =====> " + e.getMessage());
//        }
//    }
}
