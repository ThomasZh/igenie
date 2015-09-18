package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ClubSubscribersAddReq
		extends ReqCommand
{
	public ClubSubscribersAddReq()
	{
		super();

		this.setTag(Command.CLUB_SUBSCRIBER_ADD_REQ);
	}

	public ClubSubscribersAddReq(String clubId)
	{
		this();

		this.setClubId(clubId);
	}

	public ClubSubscribersAddReq(String clubId, String[] ids)
	{
		this(clubId);

		this.setUserIds(ids);
	}

	public ClubSubscribersAddReq(int sequence, String clubId, String[] ids)
	{
		this(clubId, ids);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tClubId = new TlvObject(i++, clubId);

		Gson gson = new Gson();
		TlvObject tJson = null;
		String json = gson.toJson(userIds);
		if (userIds != null && userIds.length > 0) {
			tJson = new TlvObject(i++, json);
		} else {
			tJson = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tClubId);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ClubSubscribersAddReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tClubId = tlv.getChild(i++);
		clubId = new String(tClubId.getValue(), "UTF-8");
		logger.debug("clubId: " + clubId);

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			userIds = gson.fromJson(json, String[].class);
		}

		return this;
	}

	private String clubId;

	private String[] userIds;

	public String getClubId()
	{
		return clubId;
	}

	public void setClubId(String clubId)
	{
		this.clubId = clubId;
	}

	public String[] getUserIds()
	{
		return userIds;
	}

	public void setUserIds(String[] userIds)
	{
		this.userIds = userIds;
	}

	private final static Logger logger = LoggerFactory.getLogger(ClubSubscribersAddReq.class);

}
