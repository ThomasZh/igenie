package com.oct.ga.comm.cmd.addrbook;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvObject;

public class SyncContactResp
		extends RespCommand
{
	public SyncContactResp()
	{
		this.setTag(Command.SYNC_CONTACT_RESP);
	}

	public SyncContactResp(AccountMaster account)
	{
		this();

		this.setAccount(account);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tAccountId = new TlvObject(1, account.getAccountId());
		TlvObject tFirstname = new TlvObject(2, account.getNickname());
		TlvObject tFacePhoto = new TlvObject(3, account.getAvatarUrl());
		TlvObject tEmail = new TlvObject(4, account.getEmail());
		TlvObject tTelephone = new TlvObject(5, account.getPhone());

		TlvObject tlv = new TlvObject(Command.SYNC_CONTACT_RESP);
		tlv.push(tAccountId);
		tlv.push(tFirstname);
		tlv.push(tFacePhoto);
		tlv.push(tEmail);
		tlv.push(tTelephone);

		logger.info("from command to tlv package:(tag=" + Command.SYNC_CONTACT_RESP + ", child=5, length="
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

	private final static Logger logger = LoggerFactory.getLogger(SyncContactResp.class);
}
