package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ClubQueryMyListReq
		extends StpCommand
{
	public ClubQueryMyListReq()
	{
		super();

		this.setTag(Command.CLUB_QUERY_MYLIST_REQ);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject root = new TlvObject(Command.CLUB_QUERY_MYLIST_REQ);
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		root.push(tSequence);

		return root;
	}

	@Override
	public ClubQueryMyListReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.CLUB_QUERY_MYLIST_REQ + ", child=1) to command");

		TlvParser.decodeChildren(tlv, 1);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		return this;
	}

	private final static Logger logger = LoggerFactory.getLogger(ClubQueryMyListReq.class);

}
