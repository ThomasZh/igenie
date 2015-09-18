package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.account.domain.LostPwdEkey;

public class QueryLostPwdEkeyInfoResp
		extends SupRespCommand
{
	public QueryLostPwdEkeyInfoResp()
	{
		super();

		this.setTag(SupCommandTag.QUERY_LOST_PWD_EKEY_INFO_RESP);
	}

	public QueryLostPwdEkeyInfoResp(long sequence, short state)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(state);
	}

	public QueryLostPwdEkeyInfoResp(long sequence, short state, LostPwdEkey ekeyInfo)
	{
		this(sequence, state);

		this.setEkeyInfo(ekeyInfo);
	}

	@Override
	public QueryLostPwdEkeyInfoResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tState.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			ekeyInfo = gson.fromJson(json, LostPwdEkey.class);

			logger.debug("accountId: " + ekeyInfo.getAccountId());
			logger.debug("loginType: " + ekeyInfo.getLoginType());
			logger.debug("loginName: " + ekeyInfo.getLoginName());
			logger.debug("ttl: " + ekeyInfo.getTtl());
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		Gson gson = new Gson();
		String json = gson.toJson(ekeyInfo, LostPwdEkey.class);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private LostPwdEkey ekeyInfo;

	public LostPwdEkey getEkeyInfo()
	{
		return ekeyInfo;
	}

	public void setEkeyInfo(LostPwdEkey ekeyInfo)
	{
		this.ekeyInfo = ekeyInfo;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryLostPwdEkeyInfoResp.class);

}
