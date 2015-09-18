package com.oct.ga.jpush;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.msg.JPushMessage;
import com.oct.ga.mq.Consumer;

public class JPushMessageQueue
{
	private BlockingQueue<Object> mq;

	private Consumer consumer;

	public JPushMessageQueue()
	{
		mq = new PriorityBlockingQueue<Object>();
		consumer = new JPushMessageConsumer(mq);
	}

	/**
	 * start push service
	 */
	public void start()
	{
		new Thread(consumer).start();
	}

	/**
	 * call JPush client of android
	 */
	public void push(final JPushMessage msg)
	{
		try {
			logger.debug(">>> message is in queue...");
			synchronized (mq) {
				mq.put(msg);
			}
		} catch (InterruptedException e) {
			LogErrorMessage.getFullInfo(e);
		}

		logger.debug("send message in process...");
	}

	private final static Logger logger = LoggerFactory.getLogger(JPushMessageQueue.class);

}
