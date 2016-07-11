package com.alphawang.mq.ch02.workqueue;

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
 *
 * refer to http://www.rabbitmq.com/tutorials/tutorial-two-java.html
 */
public class Receiver {

	private final static String QUEUE_NAME = "durable_queue";

	public static void main(String[] argv)
		throws IOException,
		InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		boolean durable = true;
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		/**
		 * A worker process running in the background will pop the tasks and eventually execute the job.
		 * When you run many workers the tasks will be shared between them.
		 *
		 * By default, RabbitMQ will send each message to the next consumer, in sequence.
		 * On average every consumer will get the same number of messages.
		 * This way of distributing messages is called round-robin.
		 */
		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");

				System.out.println(" [x] Received '" + message + "'");
				try {
					doWork(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println(" [x] Done");

					// With our current code, once RabbitMQ delivers a message to the customer it immediately removes it from memory.
					// In this case, if you kill a worker we will lose the message it was just processing.
					// We'll also lose all the messages that were dispatched to this particular worker but were not yet handled.
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};

		channel.basicConsume(QUEUE_NAME, true, consumer);
	}

	private static void doWork(String task) throws InterruptedException {
		for (char ch: task.toCharArray()) {
			if (ch == '.') Thread.sleep(1000);
		}
	}

}
