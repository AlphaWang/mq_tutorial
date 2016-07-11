package com.alphawang.mq.ch03.exchanges;

import com.alphawang.mq.ChannelHelper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import javafx.util.Pair;

import java.io.IOException;

/**
 * Created by Alpha on 7/11/16.
 */
public class LogSubscriber {

	public static void main(String[] args) throws IOException {
		ChannelHelper helper = new ChannelHelper();
		Pair<Connection, Channel> pair = helper.create();

		Channel channel = pair.getValue();
		channel.exchangeDeclare(LogConst.EXCHANGE_NAME, "fanout");

		String queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, LogConst.EXCHANGE_NAME, "");
		System.out.println(" [*] Subscriber Queue '" + queueName + "'");
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
				AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
	}
}
