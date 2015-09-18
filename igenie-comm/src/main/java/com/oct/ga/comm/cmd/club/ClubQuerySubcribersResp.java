package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ClubQuerySubcribersResp
		extends RespCommand
{
	public ClubQuerySubcribersResp()
	{
		this.setTag(Command.CLUB_QUERY_SUBSCRIBER_RESP);
	}

	public ClubQuerySubcribersResp(short respState, List<AccountBasic> memberList)
	{
		this();

		this.setRespState(respState);
		this.setMemberList(memberList);
	}

	public ClubQuerySubcribersResp(int sequence, short respState, List<AccountBasic> memberList)
	{
		this(respState, memberList);

		this.setSequence(sequence);
	}

	@Override
	public ClubQuerySubcribersResp decode(TlvObject tlv)
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

		TlvObject tRespState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tRespState.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);

		if (json != null) {
			Gson gson = new Gson();
			memberList = gson.fromJson(json, new TypeToken<List<AccountBasic>>()
			{
			}.getType());
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));

		Gson gson = new Gson();
		TlvObject tJson = null;
		String json = gson.toJson(memberList);
		if (memberList != null && memberList.size() > 0) {
			tJson = new TlvObject(i++, json);
		} else {
			tJson = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private List<AccountBasic> memberList;

	public List<AccountBasic> getMemberList()
	{
		return memberList;
	}

	public void setMemberList(List<AccountBasic> memberList)
	{
		this.memberList = memberList;
	}

	private final static Logger logger = LoggerFactory.getLogger(ClubQuerySubcribersResp.class);

}
