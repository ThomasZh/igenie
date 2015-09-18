package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.auth.HeartbitResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;

// serverIp,port,sessionNumber
public class HeartbitAdapter
		extends StpReqCommand
{
	public HeartbitAdapter()
	{
		super();

		this.setTag(Command.HEARTBIT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		HeartbitResp respCmd = new HeartbitResp();
		return respCmd;
	}

}
