package com.oct.ga.comm.cmd.account;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncAccountBaseInfoResp
		extends RespCommand
{
	public SyncAccountBaseInfoResp()
	{
		this.setTag(SYNC_ACCOUNT_BASE_INFO_RESP);
	}

	public SyncAccountBaseInfoResp(String accountId, String name, String avatarUrl)
	{
		this();

		this.setAccountId(accountId);
		this.setName(name);
		this.setAvatarUrl(avatarUrl);
	}

	public SyncAccountBaseInfoResp(int sequence, short state, String accountId, String name, String avatarUrl)
	{
		this(accountId, name, avatarUrl);

		this.setSequence(sequence);
		this.setRespState(state);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tAccountId = new TlvObject(i++, accountId);
		TlvObject tName = new TlvObject(i++, name);
		TlvObject tAvatarUrl = new TlvObject(i++, avatarUrl);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tAccountId);
		tlv.push(tName);
		tlv.push(tAvatarUrl);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public SyncAccountBaseInfoResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 5;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tState.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tAccountId = tlv.getChild(i++);
		accountId = new String(tAccountId.getValue(), "UTF-8");
		logger.debug("accountId: " + accountId);

		TlvObject tName = tlv.getChild(i++);
		name = new String(tName.getValue(), "UTF-8");
		logger.debug("name: " + name);

		TlvObject tAvatarUrl = tlv.getChild(i++);
		avatarUrl = new String(tAvatarUrl.getValue(), "UTF-8");
		logger.debug("avatarUrl: " + avatarUrl);

		return this;
	}

	private String accountId;
	private String name;
	private String avatarUrl;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAvatarUrl()
	{
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl)
	{
		this.avatarUrl = avatarUrl;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncAccountBaseInfoResp.class);

}
