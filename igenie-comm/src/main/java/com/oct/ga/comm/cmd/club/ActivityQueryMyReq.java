package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityQueryMyReq
		extends ReqCommand
{
	public ActivityQueryMyReq()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_MY_REQ);
	}

	@Override
	public ActivityQueryMyReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.ACTIVITY_QUERY_MY_REQ + ", child=1) to command");

		TlvParser.decodeChildren(tlv, 1);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		return this;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryMyReq.class);

}
