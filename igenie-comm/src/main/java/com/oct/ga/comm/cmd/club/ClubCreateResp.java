package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ClubCreateResp
		extends RespCommand
{
	public ClubCreateResp()
	{
		this.setTag(Command.CLUB_CREATE_RESP);
	}

	public ClubCreateResp(short respState, String clubId)
	{
		this();

		this.setRespState(respState);
		this.clubId = clubId;
	}

	public ClubCreateResp(int sequence, short respState, String clubId)
	{
		this(respState, clubId);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tClubId = new TlvObject(i++, clubId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tClubId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ClubCreateResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));
		logger.debug("resp state: " + this.getRespState());

		TlvObject tClubId = tlv.getChild(i++);
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

	private final static Logger logger = LoggerFactory.getLogger(ClubCreateResp.class);
}
