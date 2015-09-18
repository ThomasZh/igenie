package com.oct.ga.service;

import java.io.IOException;

import org.apache.mina.core.service.IoService;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;

public abstract class InlinecastMessageServiceIf
{
	public void multicast(ApplicationContext context, MessageInlinecast msg)
			throws IOException, InterruptedException, SupSocketException
	{
	}

	public void multicast(ApplicationContext context, GaInvite invite)
			throws IOException, InterruptedException, SupSocketException
	{
	}

	public void multicast(ApplicationContext context, GaApplyStateNotify notify)
			throws IOException, InterruptedException, SupSocketException
	{
	}

	public void multicast(ApplicationContext context, GaInviteFeedback feedback)
			throws IOException, InterruptedException, SupSocketException
	{
	}

	public void multicast(ApplicationContext context, String activityName, String leaderId, String memberName,
			int timestamp)
			throws IOException, InterruptedException, SupSocketException
	{
	}

	public void multicast(ApplicationContext context, MsgFlowBasicInfo msg)
			throws IOException, InterruptedException, SupSocketException
	{
	}

	protected IoService ioService;

	public IoService getIoService()
	{
		return ioService;
	}

	public void setIoService(IoService ioService)
	{
		this.ioService = ioService;
	}

}
