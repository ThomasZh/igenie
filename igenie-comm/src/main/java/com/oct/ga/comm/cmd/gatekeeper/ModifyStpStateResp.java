package com.oct.ga.comm.cmd.gatekeeper;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class ModifyStpStateResp
		extends RespCommand
{
	public ModifyStpStateResp()
	{
		this.setTag(Command.GK_MODIFY_STP_STATE_RESP);
	}

	public ModifyStpStateResp(short respState)
	{
		this();

		this.setRespState(respState);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));

		TlvObject tlv = new TlvObject(Command.GK_MODIFY_STP_STATE_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);

		logger.info("from command to tlv package:(tag=" + Command.GK_MODIFY_STP_STATE_RESP + ", child=2, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private final static Logger logger = LoggerFactory.getLogger(ModifyStpStateResp.class);

}
