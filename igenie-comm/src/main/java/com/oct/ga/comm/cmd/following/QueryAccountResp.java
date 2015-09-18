package com.oct.ga.comm.cmd.following;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryAccountResp
		extends RespCommand
{
	public QueryAccountResp()
	{
		this.setTag(Command.QUERY_ACCOUNT_RESP);
	}

	public QueryAccountResp(short state, AccountMaster account)
	{
		this();

		this.setRespState(state);
		this.setAccount(account);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tAccountId = new TlvObject(3, account.getAccountId());
		TlvObject tFirstname = new TlvObject(4, account.getNickname());
		TlvObject tFacePhoto = new TlvObject(5, account.getAvatarUrl());
		TlvObject tEmail = new TlvObject(6, account.getEmail());
		TlvObject tTelephone = new TlvObject(7, account.getPhone());
		TlvObject tDesc = new TlvObject(8, account.getDesc());

		TlvObject tlv = new TlvObject(Command.QUERY_ACCOUNT_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tAccountId);
		tlv.push(tFirstname);
		tlv.push(tFacePhoto);
		tlv.push(tEmail);
		tlv.push(tTelephone);
		tlv.push(tDesc);

		logger.info("from command to tlv package:(tag=" + Command.QUERY_ACCOUNT_RESP + ", child=8, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public QueryAccountResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		TlvParser.decodeChildren(tlv, 8);

		TlvObject tSequence = tlv.getChild(0);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));

		TlvObject tResultFlag = tlv.getChild(1);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		account = new AccountMaster();

		TlvObject tAccountId = tlv.getChild(2);
		account.setAccountId(new String(tAccountId.getValue(), "UTF-8"));

		TlvObject tFirstname = tlv.getChild(3);
		account.setNickname(new String(tFirstname.getValue(), "UTF-8"));

		TlvObject tFacePhoto = tlv.getChild(4);
		account.setAvatarUrl(new String(tFacePhoto.getValue(), "UTF-8"));

		TlvObject tEmail = tlv.getChild(5);
		account.setEmail(new String(tEmail.getValue(), "UTF-8"));

		TlvObject tTelephone = tlv.getChild(6);
		account.setPhone(new String(tTelephone.getValue(), "UTF-8"));

		TlvObject tDesc = tlv.getChild(7);
		account.setDesc(new String(tDesc.getValue(), "UTF-8"));

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

	private final static Logger logger = LoggerFactory.getLogger(QueryAccountResp.class);

}
