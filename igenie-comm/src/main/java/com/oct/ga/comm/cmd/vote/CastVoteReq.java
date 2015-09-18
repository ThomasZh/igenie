package com.oct.ga.comm.cmd.vote;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class CastVoteReq
		extends ReqCommand
{
	public CastVoteReq()
	{
		super();

		this.setTag(Command.CAST_VOTE_REQ);
	}

	@Override
	public CastVoteReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.CAST_VOTE_REQ + ", child=3) to command");

		TlvParser.decodeChildren(tlv, 3);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tVoteId = tlv.getChild(1);
		voteId = new String(tVoteId.getValue(), "UTF-8");
		logger.info("voteId: " + voteId);

		TlvObject tDecide = tlv.getChild(2);
		decide = TlvByteUtil.byte2Short(tDecide.getValue());
		logger.debug("decide: " + decide);

		return this;
	}

	private String voteId;
	private short decide;

	public String getVoteId()
	{
		return voteId;
	}

	public void setVoteId(String voteId)
	{
		this.voteId = voteId;
	}

	public short getDecide()
	{
		return decide;
	}

	public void setDecide(short decide)
	{
		this.decide = decide;
	}

	private final static Logger logger = LoggerFactory.getLogger(CastVoteReq.class);

}
