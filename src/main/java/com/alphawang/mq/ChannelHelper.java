package com.alphawang.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import javafx.util.Pair;

import java.io.IOException;

/**
 * Created by Alpha on 7/11/16.
 */
public class ChannelHelper {

	public Pair<Connection, Channel> create() throws IOException {
		return create("localhost");
	}

	public Pair<Connection, Channel> create(String host) throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		return new Pair<>(connection, channel);
	}

	public void close(Pair<Connection, Channel> pair) throws IOException {
		pair.getValue().close();
		pair.getKey().close();
	}
}
