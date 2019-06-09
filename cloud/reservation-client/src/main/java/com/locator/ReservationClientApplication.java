package com.locator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Log4j2
@EnableEurekaClient
@SpringBootApplication
public class ReservationClientApplication {

	public static void main(String[] args) {
	    log.debug("Application Context Running");
		SpringApplication.run(ReservationClientApplication.class, args);
	}

}

//@Component
//class QueueReceiver implements ApplicationRunner {
//
//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		var queueName = "hello_queue";
//
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost("localhost");
//		Connection connection = factory.newConnection();
//		Channel channel = connection.createChannel();
//
//		channel.queueDeclare(queueName, false, false, false, null);
//		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//
//		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//			String message = new String(delivery.getBody(), "UTF-8");
//			System.out.println(" [x] Received '" + message + "'");
//		};
//		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
//	}
//
//}
//
//@Component
//class Receiver {
//
//	private CountDownLatch latch = new CountDownLatch(1);
//
//	public void receiveMessage(String message) {
//		System.out.println("Received <" + message + ">");
//		latch.countDown();
//	}
//
//	public CountDownLatch getLatch() {
//		return latch;
//	}
//
//}