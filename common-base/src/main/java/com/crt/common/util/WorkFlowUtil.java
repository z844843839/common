package com.crt.common.util;

import com.alibaba.fastjson.JSONObject;
import com.crt.commones.mq.service.MQcroe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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

    @Autowired
    MQcroe mQcroe;

    /**
     * 流程启动方法
     * @param map Map<String,Object>
     * @return
     */
    public static void flowStartup(Map<String,Object> map){
        JSONObject json = new JSONObject();
        if (null != map && map.size() > 0){
            RestTemplate restTemplate = new RestTemplate();
            //api url地址
            String url = "http://middle-common-bpm.dev.chinacrt.com:23456/";
            url += "/operation/process-operation-confirmStartProcess";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> mvMap= new LinkedMultiValueMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                mvMap.add(entry.getKey(),entry.getValue());
            }
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(mvMap, headers);
            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
            logger.error(" response ====> "+ response.getBody());
        }
    }

    /**
     * 订阅MQ 处理消费结果
     * @param queue  doc_type
     * @param routineKey
     */
    public void resultProcess(String queue,String routineKey){
        try{
            mQcroe.mqMessageListenner(queue,routineKey);
        }catch (IOException e){
            logger.error(" ioexception =====> " + e.getMessage());
        }
    }
}
