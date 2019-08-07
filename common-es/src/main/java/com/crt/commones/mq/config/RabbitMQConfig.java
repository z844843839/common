package com.crt.commones.mq.config;

import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Json对象消息序列器
 * Direct 模式就是直接用队列的名字来进行绑定，实现点对点的消息传输。
 *
 * Topic 模式是根据 Config 中设置的 RoutineKey 还有发送消息时候的 topic 来判断是否会传输。
 *
 * Fanout 模式类似于广播，不用设置路由，只要发送消息设置了对应的 Exchange 就可以对该 Exchange 中的接收者进行广播。
 *
 * Headers 模式比较不同于其他三种模式。Headers 是一个键值对，可以定义成 Hashtable。发送者在发送的时候定义一些键值对，
 * 接收者也可以再绑定时候传入一些键值对，两者匹配的话，则对应的队列就可以收到消息。匹配有两种方式all和any。
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {
	private static Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);
	@Bean
	public MessageConverter MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Value("${spring.rabbitmq.host}")
	String host;//MQ地址
	@Value("${spring.rabbitmq.port}")
	int port;//MQ地址
	@Value("${spring.rabbitmq.username}")
	String username;//MQ登录名
	@Value("${spring.rabbitmq.password}")
	String password;//MQ登录密码
	@Value("${spring.rabbitmq.virtual-host}")
	String vHost;//MQ的虚拟主机名
	//初始化链接信息
	@Bean
	public Connection connectionFactory() {
		Connection connection = null;
		com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(vHost);
		//进程断掉重连 默认是true
		factory.setAutomaticRecoveryEnabled(true);
		log.info("Create ConnectionFactory bean ..");
		try {
			connection = factory.newConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 默认交换机
	 */
	public static final String TOPIC_EXCHANGE = "e6_default_exchange";

	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(TOPIC_EXCHANGE);
	}



}
