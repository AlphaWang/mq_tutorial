package com.alphawang.mq.spring;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * @SpringBootApplication is a convenience annotation that adds all of the following:
 *
 * - @Configuration tags the class as a source of bean definitions for the application context.
 * - @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 * - Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath.
 *   This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
 * - @ComponentScan tells Spring to look for other components, configurations, and services in the hello package.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

	private final static String QUEUE_NAME = "spring-boot";

	@Autowired
	AnnotationConfigApplicationContext context;

	/**
	 * You’ll use RabbitTemplate to send messages, and you will register a Receiver with the message listener container to receive messages.
	 * The connection factory drives both, allowing them to connect to the RabbitMQ server.
	 */
	@Autowired
	RabbitTemplate rabbitTemplate;

	@Bean
	Receiver receiver() {
		return new Receiver();
	}

	/**
	 * The bean defined in the listenerAdapter() method is registered as a message listener in the container defined in container().
	 * It will listen for messages on the "spring-boot" queue.
	 *
	 * Because the Receiver class is a POJO, it needs to be wrapped in the MessageListenerAdapter, where you specify it to invoke receiveMessage.
	 */
	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(QUEUE_NAME);
		container.setMessageListener(listenerAdapter);
		return container;
	}


	//////////////////////////////////////

	/**
	 * The queue() method creates an AMQP queue.
	 */
	@Bean
	Queue queue() {
		return new Queue(QUEUE_NAME, false);
	}

	/**
	 * The exchange() method creates a topic exchange.
	 */
	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	/**
	 * The binding() method binds these two together, defining the behavior that occurs when RabbitTemplate publishes to an exchange.
	 */
	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_NAME);
	}


	/////////////////////////////////////

	/**
	 * The main() method starts that process by creating a Spring application context.
	 * This starts the message listener container, which will start listening for messages.
	 */
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * There is a run() method which is then automatically executed:   --SEND
	 * it then retrieves the RabbitTemplate from the application context, waits five seconds, and sends a "Hello from RabbitMQ!" message on the "spring-boot" queue.
	 * Finally, it closes the Spring application context and the application ends.
	 */
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Waiting five seconds...");
		Thread.sleep(5000);
		System.out.println("Sending message...");
		rabbitTemplate.convertAndSend(QUEUE_NAME, "Hello from RabbitMQ!");
		receiver().getLatch().await(10000, TimeUnit.MILLISECONDS);
		context.close();
	}
}
