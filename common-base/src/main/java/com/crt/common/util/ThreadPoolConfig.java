package com.crt.common.util;


import com.crt.common.constant.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 数据收集配置，主要作用在于Spring启动时自动加载一个ExecutorService对象.
 * @Author malin@e6yun.com
 * @Created Date: 2019/11/06
 */
@Configuration
public class ThreadPoolConfig {

	@Bean
	public ExecutorService executor(){
		return Executors.newFixedThreadPool(Constants.NUMBER_TEN);
	}
}
