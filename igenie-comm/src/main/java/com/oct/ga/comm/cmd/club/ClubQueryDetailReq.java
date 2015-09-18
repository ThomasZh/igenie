package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ClubQueryDetailReq
		extends ReqCommand
{
	public ClubQueryDetailReq()
	{
		super();

		this.setTag(Command.CLUB_QUERY_DETAIL_REQ);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject root = new TlvObject(Command.CLUB_QUERY_DETAIL_REQ);

		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tClubId = new TlvObject(2, clubId);
		root.push(tSequence);
		root.push(tClubId);

		return root;
	}

	@Override
	public ClubQueryDetailReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.CLUB_QUERY_DETAIL_REQ + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tClubId = tlv.getChild(1);
		clubId = new String(tClubId.getValue(), "UTF-8");
		logger.debug("clubId: " + clubId);

		return this;
	}

	private String clubId;

	public String getClubId()
	{
		return clubId;
	}

	public void setClubId(String clubId)
	{
		this.clubId = clubId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ClubQueryDetailReq.class);

}
