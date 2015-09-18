package com.oct.ga.comm.cmd.account;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class UploadAccountReq
		extends ReqCommand
{
	public UploadAccountReq()
	{
		super();

		this.setTag(Command.UPLOAD_MY_ACCOUNT_REQ);
	}

	public UploadAccountReq(AccountMaster account)
	{
		this();

		this.account = account;
	}

	public UploadAccountReq(int sequence, AccountMaster account)
	{
		this(account);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		Gson gson = new Gson();
		String json = gson.toJson(account, AccountMaster.class);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public UploadAccountReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

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

		return this;
	}

	private AccountMaster account;

	public AccountMaster getAccount()
	{
		return account;
	}

	public void setAccount(AccountMaster account)
	{
		this.account = account;
	}

	private final static Logger logger = LoggerFactory.getLogger(UploadAccountReq.class);
}
