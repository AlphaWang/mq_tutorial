package com.alphawang.mq.spring;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Alpha on 7/12/16.
 */
public class Receiver {

	private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(String message) {
		System.out.println("Received <" + message + ">");
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}

}
