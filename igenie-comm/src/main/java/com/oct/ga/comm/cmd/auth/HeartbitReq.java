package com.oct.ga.comm.cmd.auth;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvObject;

// serverIp,port,sessionNumber
public class HeartbitReq
		extends ReqCommand
{
	public HeartbitReq()
	{
		super();

		this.setTag(Command.HEARTBIT_REQ);
	}

	@Override
	public HeartbitReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 0;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;
		TlvObject tlv = new TlvObject(this.getTag());

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private final static Logger logger = LoggerFactory.getLogger(HeartbitReq.class);
}
