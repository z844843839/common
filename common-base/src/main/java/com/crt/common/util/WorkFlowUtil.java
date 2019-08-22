package com.crt.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
public class WorkFlowUtil {

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowUtil.class);

//    @Autowired
//    MQcroe mQcroe;
    @Value("${workflow.url}")
    private static String url;

    /**
     * 流程启动方法
     * @param map Map<String,Object>
     * @return
     */
    public static void flowStartup(Map<String,Object> map){
//        url = "http://middle-common-bpm.dev.chinacrt.com:23456/bpm/operation/process-operation-confirmStartProcess";
        if (null != map && map.size() > 0){
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//            String token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiIxNTY2MzY4ODczNDY2Iiwic3ViIjoic3VwZXIiLCJhdWQiOiJzdXBlciBNb3ppbGxhLzUuM" +
//                    "CAoV2luZG93cyBOVCAxMC4wOyBXT1c2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzc1LjAuMzc3MC4xNDIgU2FmYXJpLzUzNy4zNi" +
//                    "IsImlhdCI6MTU2NjM2ODg3MywiZXhwIjoxNTY2Mzk3NjczLCJjdXN0b21BdHRyIjoiMTU2NTYwMzI5MzA5NyJ9.TWtOCmeaZ7kyTrYF4WInMa1-Y1XqQxngkItuJHUsymk";
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
            logger.error(" response ====> "+ exchange.getBody());
        }
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