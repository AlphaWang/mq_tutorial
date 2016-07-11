package com.alphawang.mq.ch03.exchanges;

import com.alphawang.mq.ChannelHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Alpha on 7/11/16.
 */
public class LogPublisher {

	public static void main(String[] args) throws IOException {
		ChannelHelper helper = new ChannelHelper();
		Pair<Connection, Channel> pair = helper.create();

		// There are a few exchange types available: direct, topic, headers and fanout.
		// `fanout` just broadcasts all the messages it receives to all the queues it knows.
		Channel channel = pair.getValue();
		// the producer doesn't even know if a message will be delivered to any queue at all.
		// Instead, the producer can only send messages to an exchange.
		channel.exchangeDeclare(LogConst.EXCHANGE_NAME, "fanout");

		String log = "[info] test log " + Arrays.toString(args);
		channel.basicPublish(LogConst.EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, log.getBytes());
		System.out.println("[x] Sent '" + log + "'");

		helper.close(pair);
	}
}
