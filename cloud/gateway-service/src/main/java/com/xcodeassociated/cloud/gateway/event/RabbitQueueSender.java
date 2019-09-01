package com.xcodeassociated.cloud.gateway.event;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Log4j2
@Component
public class RabbitQueueSender {
    private Channel channel;
    private static final String QUEUE_NAME = "hello_queue";

    public RabbitQueueSender() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    }

    public void basicPublish(String data) throws Exception {
        if (this.channel.isOpen()) {
            this.channel.basicPublish("", QUEUE_NAME, null, data.getBytes("UTF-8"));
            log.info(" [x] Sent '" + data + "'");
        } else {
            log.error("Channel is closed!");
            throw new Exception("Could not send the message on a queue");
        }
    }
}
