package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ClubCreateReq
		extends ReqCommand
{
	public ClubCreateReq()
	{
		super();

		this.setTag(Command.CLUB_CREATE_REQ);
	}

	public ClubCreateReq(ClubMasterInfo club)
	{
		this();

		this.setClub(club);
	}

	public ClubCreateReq(int sequence, ClubMasterInfo club)
	{
		this(club);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));

		Gson gson = new Gson();
		TlvObject tJson = null;
		if (club != null) {
			String json = gson.toJson(club);
			tJson = new TlvObject(i++, json);
		} else {
			tJson = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ClubCreateReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			club = gson.fromJson(json, ClubMasterInfo.class);
//			for (String userId : club.getSharingUserIds()) {
//				logger.debug("userId: " + userId);
//			}
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

	private final static Logger logger = LoggerFactory.getLogger(ClubCreateReq.class);
}
