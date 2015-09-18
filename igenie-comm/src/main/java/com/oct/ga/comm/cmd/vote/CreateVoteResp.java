package com.oct.ga.comm.cmd.vote;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class CreateVoteResp
		extends RespCommand
{
	public CreateVoteResp()
	{
		this.setTag(Command.CREATE_VOTE_RESP);
	}

	public CreateVoteResp(short respState, String voteId)
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

		TlvObject tlv = new TlvObject(Command.CREATE_VOTE_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tVoteId);

		logger.info("from command to tlv package:(tag=" + Command.CREATE_VOTE_RESP + ", child=3, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String voteId;

	private final static Logger logger = LoggerFactory.getLogger(CreateVoteResp.class);

}
