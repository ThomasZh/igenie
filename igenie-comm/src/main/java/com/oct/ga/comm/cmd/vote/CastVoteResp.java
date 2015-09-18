package com.oct.ga.comm.cmd.vote;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class CastVoteResp
		extends RespCommand
{
	public CastVoteResp()
	{
		this.setTag(Command.CAST_VOTE_RESP);
	}

	public CastVoteResp(short respState, String voteId)
	{
		this();

		this.setRespState(respState);
		this.voteId = voteId;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tVoteId = new TlvObject(3, voteId);

		TlvObject tlv = new TlvObject(Command.CAST_VOTE_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tVoteId);

		logger.info("from command to tlv package:(tag=" + Command.CAST_VOTE_RESP + ", child=3, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String voteId;

	private final static Logger logger = LoggerFactory.getLogger(CastVoteResp.class);

}
