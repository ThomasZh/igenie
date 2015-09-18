package com.oct.ga.comm.cmd.auth;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class STP_ACF
		extends RespCommand
{
	public STP_ACF()
	{
		this.setTag(Command.STP_ACF);
	}

	public STP_ACF(short respState)
	{
		this();

		this.setRespState(respState);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tRespState = new TlvObject(1, 2, TlvByteUtil.short2Byte(this.getRespState()));

		TlvObject tlv = new TlvObject(Command.STP_ACF);
		tlv.push(tRespState);

		logger.info("from command to tlv package:(tag=" + Command.STP_ACF + ", child=1, length=" + tlv.getLength()
				+ ")");

		return tlv;
	}

	@Override
	public STP_ACF decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 1;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		return this;
	}

	private final static Logger logger = LoggerFactory.getLogger(STP_ACF.class);
}
