package com.alphawang.mq.ch05.exchangetype.topic.direct;

import com.alphawang.mq.ChannelHelper;
import com.alphawang.mq.ch03.exchanges.LogConst;
import com.alphawang.mq.ch04.exchangetype.direct.LogLevel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Alpha on 7/11/16.
 */
public class TopicPublisher {

	public static void main(String[] args) throws IOException {
		ChannelHelper helper = new ChannelHelper();
		Pair<Connection, Channel> pair = helper.create();

		// topic: a message sent with a particular routing key will be delivered to all the queues that are bound with a matching binding key
		Channel channel = pair.getValue();
		channel.exchangeDeclare(LogConst.EXCHANGE_NAME, "topic");

		String source = channel.getClass().getName();
		String routingKey =  LogLevel.INFO.name() + "." + source;

		String log = "[info] test log " + Arrays.toString(args);
		channel.basicPublish(LogConst.EXCHANGE_NAME, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, log.getBytes());
		System.out.println("[x] Sent '" + log + "'");

		helper.close(pair);
	}
}
