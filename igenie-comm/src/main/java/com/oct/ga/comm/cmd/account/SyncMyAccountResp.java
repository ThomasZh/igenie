package com.oct.ga.comm.cmd.account;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncMyAccountResp
		extends RespCommand
{
	public SyncMyAccountResp()
	{
		this.setTag(SYNC_MY_ACCOUNT_RESP);
	}

	public SyncMyAccountResp(AccountMaster account, int timestamp)
	{
		this();

		this.setAccount(account);
		this.setCurrentTimestamp(timestamp);
	}

	public SyncMyAccountResp(int sequence, AccountMaster account, int timestamp)
	{
		this(account, timestamp);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		Gson gson = new Gson();
		String json = gson.toJson(account, AccountMaster.class);
		TlvObject tJson = new TlvObject(i++, json);
		TlvObject tTimestamp = new TlvObject(i++, 4, TlvByteUtil.int2Byte(currentTimestamp));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);
		tlv.push(tTimestamp);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public SyncMyAccountResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tState.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			account = gson.fromJson(json, AccountMaster.class);

			logger.debug("accountId: " + account.getAccountId());
			logger.debug("nickname: " + account.getNickname());
			logger.debug("avatarUrl: " + account.getAvatarUrl());
			logger.debug("desc: " + account.getDesc());
			logger.debug("email: " + account.getEmail());
			logger.debug("phone: " + account.getPhone());
			logger.debug("lang: " + account.getLang());
		}

		TlvObject tTimestamp = tlv.getChild(i++);
		currentTimestamp = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("currentTimestamp: " + currentTimestamp);

		return this;
	}

	private AccountMaster account;
	private int currentTimestamp;

	public void setAccount(AccountMaster account)
	{
		this.account = account;
	}

	public AccountMaster getAccount()
	{
		return account;
	}

	public int getCurrentTimestamp()
	{
		return currentTimestamp;
	}

	public void setCurrentTimestamp(int currentTimestamp)
	{
		this.currentTimestamp = currentTimestamp;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncMyAccountResp.class);
}
