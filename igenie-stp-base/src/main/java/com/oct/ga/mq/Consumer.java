package com.oct.ga.mq;

import java.util.concurrent.BlockingQueue;

/**
 * message sender to APN service
 * 
 * @author liwenzhi
 */
public abstract class Consumer implements Runnable {
	
	private final BlockingQueue<Object> queue;

	public Consumer(BlockingQueue<Object> q) {
		queue = q;
	}

	public void run() {
		try {
			while (true) {
				consume(queue.take());
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public abstract void consume(Object x);

}
