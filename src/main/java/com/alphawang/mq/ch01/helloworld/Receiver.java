package com.alphawang.mq.ch01.helloworld;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * Created by Alpha on 7/11/16.
 */
public class Receiver {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv)
		throws java.io.IOException,
		java.lang.InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// Note that we declare the queue here, as well.
		// Because we might start the receiver before the sender,
		// we want to make sure the queue exists before we try to consume messages from it.
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


		// Since it will push us messages asynchronously,
		// we provide a callback in the form of an object that will buffer the messages until we're ready to use them.
		// That is what a DefaultConsumer subclass does.
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
				throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}


}
