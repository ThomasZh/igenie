package com.oct.ga.comm.cmd.vote;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ConfirmVoteReceivedReq
		extends ReqCommand
{
	public ConfirmVoteReceivedReq()
	{
		super();

		this.setTag(Command.CONFIRM_VOTE_RECEIVED_REQ);
	}

	@Override
	public ConfirmVoteReceivedReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.CONFIRM_VOTE_RECEIVED_REQ + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tVoteId = tlv.getChild(1);
		voteId = new String(tVoteId.getValue(), "UTF-8");
		logger.info("voteId: " + voteId);

		return this;
	}

	private String voteId;

	public String getVoteId()
	{
		return voteId;
	}

	public void setVoteId(String voteId)
	{
		this.voteId = voteId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ConfirmVoteReceivedReq.class);

}
