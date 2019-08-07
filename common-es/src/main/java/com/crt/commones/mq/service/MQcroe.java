package com.crt.commones.mq.service;

import com.crt.commones.mq.config.RabbitMQConfig;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 动态监听
 */
@Component
public class MQcroe {
	private static Logger logger = LoggerFactory.getLogger(MQcroe.class);

	@Autowired
	RabbitMQConfig rabbitMQConfig;

	@Autowired
	RabbitTemplate rabbitTemplate;


	/**
	 * 创建监听器，监听队列
	 */
	public List mqMessageListenner(String queue,String routineKey,String exchange) throws AmqpException, IOException {
      	//注意这里：mq本身 创建物理连接
		Connection connection  = rabbitMQConfig.connectionFactory();
		//创建虚拟连接，信道
		Channel channel =  connection.createChannel();
		channel.basicQos(64);//需要开启手动应答模式，否则无效
		//处理队列动作，包含创建队列，交换机，以及绑定动作
		channel.exchangeDeclare(exchange,"topic",true);

		//队列名称
		channel.queueDeclare(queue,true,false,false,null);
		//绑定交换机队列
		channel.queueBind(queue,exchange,routineKey);

		List list = new ArrayList();
		//声明监听队列
		Consumer comsumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
									   byte[] body)throws IOException{
				long deliveryTag = envelope.getDeliveryTag();
				String str = new String(body,"UTF-8");
				list.add(str);
				channel.basicAck(deliveryTag, false);
			}
		};

		//消费队列信息
		channel.basicConsume(queue,false,comsumer);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return list;
	}


	/**
	 * 创建消息发送信息
	 */
	public boolean mqMessageSendS(String routineKey,String exchange,String body) throws AmqpException, IOException {
		//创建物理连接
		Connection connection  = rabbitMQConfig.connectionFactory();
		//创建虚拟连接，信道
		Channel channel =  connection.createChannel();

		channel.basicPublish(exchange, routineKey,null,body.getBytes());
		return true;
	}
}
