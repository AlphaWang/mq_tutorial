package com.alphawang.mq.ch04.exchangetype;

import com.alphawang.mq.ChannelHelper;
import com.alphawang.mq.ch03.exchanges.LogConst;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Alpha on 7/11/16.
 */
public class LogLevelPublisher {

	public static void main(String[] args) throws IOException {
		ChannelHelper helper = new ChannelHelper();
		Pair<Connection, Channel> pair = helper.create();

		Channel channel = pair.getValue();
		channel.exchangeDeclare(LogConst.EXCHANGE_NAME, "fanout");

		String log = "[info] test log " + Arrays.toString(args);
		channel.basicPublish(LogConst.EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, log.getBytes());
		System.out.println("[x] Sent '" + log + "'");

		helper.close(pair);
	}
}
