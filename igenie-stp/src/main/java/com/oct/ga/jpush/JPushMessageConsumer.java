package com.oct.ga.jpush;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;

import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.msg.JPushMessage;
import com.oct.ga.mq.Consumer;

public class JPushMessageConsumer
		extends Consumer
{
	private static final String APP_KEY = "6d5877fa9215c5e7fc447581";
	private static final String MASTER_SECRET = "286ceaf8372f50b80949ebed";
	private JPushClient jpushClient = null;

	/**
	 * @param mq
	 */
	public JPushMessageConsumer(BlockingQueue<Object> mq)
	{
		super(mq);

		if (jpushClient == null)
			jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
	}

	/*
	 * send message to jpush
	 */
	@Override
	public void consume(Object x)
	{
		JPushMessage msg = (JPushMessage) x;

		try {
			if (jpushClient == null)
				jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);

			if (msg.isOnline()) {
				jpushClient.sendAndroidMessageWithAlias(msg.getTitle(), msg.getMsgContent(), msg.getAlias());
				logger.info("An online message sent to (" + msg.getAlias() + ") through by JPush.");
			} else {
				jpushClient.sendAndroidNotificationWithAlias(msg.getTitle(), msg.getMsgContent(), msg.getExtras(),
						msg.getAlias());
				logger.info("An offline message sent to (" + msg.getAlias() + ") through by JPush.");
			}
		} catch (APIConnectionException e) {
			logger.error(LogErrorMessage.getFullInfo(e));

			jpushClient = null;
		} catch (APIRequestException e) {
			logger.error(LogErrorMessage.getFullInfo(e));

			jpushClient = null;
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(JPushMessageConsumer.class);

}
