package com.oct.ga.comm.cmd.badgenum;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.AccountBadgeNumJsonBean;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class BadgeNumQueryResp
		extends RespCommand
{
	public BadgeNumQueryResp()
	{
		this.setTag(Command.QUERY_BADGE_NUMBER_RESP);
	}

	public BadgeNumQueryResp(short respState, AccountBadgeNumJsonBean badgeNum)
	{
		this();

		this.setRespState(respState);
		this.setBadgeNum(badgeNum);
	}

	public BadgeNumQueryResp(int sequence, short respState, AccountBadgeNumJsonBean badgeNum)
	{
		this(respState, badgeNum);

		this.setBadgeNum(badgeNum);
	}

	@Override
	public BadgeNumQueryResp decode(TlvObject tlv)
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

		TlvObject tResultState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultState.getValue()));
		logger.debug("resultState: " + this.getRespState());

		TlvObject tBadgeNum = tlv.getChild(i++);
		String json = new String(tBadgeNum.getValue(), "UTF-8");
		logger.debug("json: " + json);
		Gson gson = new Gson();
		if (json != null && json.length() > 0) {
			badgeNum = gson.fromJson(json, AccountBadgeNumJsonBean.class);
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));

		Gson gson = new Gson();
		TlvObject tBadgeNum = null;
		String json = gson.toJson(badgeNum);
		logger.debug("json: " + json);
		tBadgeNum = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tBadgeNum);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private AccountBadgeNumJsonBean badgeNum;

	public AccountBadgeNumJsonBean getBadgeNum()
	{
		return badgeNum;
	}

	public void setBadgeNum(AccountBadgeNumJsonBean badgeNum)
	{
		this.badgeNum = badgeNum;
	}

	private final static Logger logger = LoggerFactory.getLogger(BadgeNumQueryResp.class);
}
