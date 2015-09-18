package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupRespCommand;

public class QueryLoginNameResp
		extends SupRespCommand
{
	public QueryLoginNameResp()
	{
		this.setTag(SupCommandTag.QUERY_LOGIN_NAME_RESP);
	}

	public QueryLoginNameResp(long sequence, short respState)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
	}

	public QueryLoginNameResp(long sequence, short respState, String loginName)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setLoginName(loginName);
	}

	@Override
	public QueryLoginNameResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		TlvObject tLoginName = tlv.getChild(i++);
		loginName = new String(tLoginName.getValue(), "UTF-8");

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tLoginName = new TlvObject(i++, loginName);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tLoginName);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String loginName;

	public String getLoginName()
	{
		return loginName;
	}

	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryLoginNameResp.class);

}
