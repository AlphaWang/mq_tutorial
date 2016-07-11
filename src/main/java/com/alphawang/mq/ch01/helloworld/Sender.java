package com.alphawang.mq.ch01.helloworld;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

/**
 * Created by Alpha on 7/11/16.
 *
 * refer to http://www.rabbitmq.com/tutorials/tutorial-one-java.html
 */
public class Sender {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws java.io.IOException {

		// The connection abstracts the socket connection,
		// and takes care of protocol version negotiation and authentication and so on for us.
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		// Next we create a channel, which is where most of the API for getting things done resides.
		Channel channel = connection.createChannel();

		// To send, we must declare a queue for us to send to; then we can publish a message to the queue:
		// Declaring a queue is idempotent - it will only be created if it doesn't exist already
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello World!!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}

}
