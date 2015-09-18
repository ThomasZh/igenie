package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvObjectExt;
import com.oct.ga.comm.tlv.TlvParser;

public class ClubUpdateReq
		extends ReqCommand
{

	private String json;

	public ClubUpdateReq()
	{
		super();

		this.setTag(Command.CLUB_UPDATE_REQ);
	}

	public ClubUpdateReq(String json)
	{
		this();
		this.json = json;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObjectExt root = new TlvObjectExt(Command.CLUB_UPDATE_REQ);

		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tJson = new TlvObject(2, this.json);

		root.plus(tSequence).plus(tJson);

		return root;
	}

	@Override
	public ClubUpdateReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.CLUB_UPDATE_REQ + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tJson = tlv.getChild(1);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);

		club = new ClubMasterInfo().decode(json);
		if (club.getSharingUserIds() != null && club.getSharingUserIds().length > 0)
			for (String userId : club.getSharingUserIds()) {
				logger.debug("userId: " + userId);
			}

		return this;
	}

	private ClubMasterInfo club;

	public ClubMasterInfo getClub()
	{
		return club;
	}

	public void setClub(ClubMasterInfo club)
	{
		this.club = club;
	}

	private final static Logger logger = LoggerFactory.getLogger(ClubUpdateReq.class);
}
