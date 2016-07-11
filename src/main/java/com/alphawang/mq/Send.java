package com.alphawang.mq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

/**
 * Created by Alpha on 7/11/16.
 */
public class Send {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws java.io.IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
	}

}
