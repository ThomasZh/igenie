package com.oct.ga.comm.cmd.vote;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.vote.Vote;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class CreateVoteReq
		extends ReqCommand
{
	public CreateVoteReq()
	{
		super();

		this.setTag(Command.CREATE_VOTE_REQ);
	}

	@Override
	public CreateVoteReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.CREATE_VOTE_REQ + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tJson = tlv.getChild(1);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.info("json: " + json);

		vote = new Vote().decode(json);

		return this;
	}

	private Vote vote;

	public Vote getVote()
	{
		return vote;
	}

	public void setVote(Vote vote)
	{
		this.vote = vote;
	}

	private final static Logger logger = LoggerFactory.getLogger(CreateVoteReq.class);

}
