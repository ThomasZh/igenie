package com.oct.ga.comm.cmd.following;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class ImportFollowingResp
		extends RespCommand
{
	public ImportFollowingResp()
	{
		this.setTag(Command.IMPORT_FOLLOWING_RESP);
	}

	public ImportFollowingResp(short state, AccountMaster account)
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

		TlvObject tlv = new TlvObject(Command.IMPORT_FOLLOWING_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tAccountId);
		tlv.push(tFirstname);
		tlv.push(tFacePhoto);
		tlv.push(tEmail);
		tlv.push(tTelephone);

		logger.info("from command to tlv package:(tag=" + Command.IMPORT_FOLLOWING_RESP + ", child=7, length="
				+ tlv.getLength() + ")");

		return tlv;
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

	private final static Logger logger = LoggerFactory.getLogger(ImportFollowingResp.class);

}
