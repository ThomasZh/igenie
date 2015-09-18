package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class ActivityQueryMyResp
		extends RespCommand
{
	public ActivityQueryMyResp()
	{
		this.setTag(Command.ACTIVITY_QUERY_MY_RESP);
	}

	public ActivityQueryMyResp(short respState, String json)
	{
		this();

		this.setRespState(respState);
		this.json = json;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tJson = new TlvObject(3, json);

		TlvObject tlv = new TlvObject(Command.ACTIVITY_QUERY_MY_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.info("from command to tlv package:(tag=" + Command.ACTIVITY_QUERY_MY_RESP + ", child=3, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String json;

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryMyResp.class);

}
