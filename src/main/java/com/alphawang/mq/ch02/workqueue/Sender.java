package com.alphawang.mq.ch02.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * Created by Alpha on 7/11/16.
 *
 * refer to http://www.rabbitmq.com/tutorials/tutorial-two-java.html
 */
public class Sender {

	private final static String QUEUE_NAME = "durable_queue";

	public static void main(String[] argv) throws java.io.IOException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		boolean durable = true;
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

		String message = getMessage(argv);
		channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}

	private static String getMessage(String[] strings){
		if (strings.length < 1)
			return "Hello World!~!~``";
		return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if (length == 0) return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for (int i = 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}

}
