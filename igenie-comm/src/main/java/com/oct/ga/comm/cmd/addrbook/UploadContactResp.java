package com.oct.ga.comm.cmd.addrbook;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class UploadContactResp
		extends RespCommand
{
	public UploadContactResp()
	{
		this.setTag(Command.UPLOAD_CONTACT_RESP);
	}

	public UploadContactResp(short respState, String friendId, short accountState, String accountId,
			String accountName, String accountFacePhoto)
	{
		this();

		this.setRespState(respState);
		this.setFriendId(friendId);
		this.setAccountState(accountState);
		this.setAccountId(accountId);
		this.setAccountName(accountName);
		this.setAccountFacePhoto(accountFacePhoto);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tFriendId = new TlvObject(3, this.getFriendId());
		TlvObject tState = new TlvObject(4, 2, TlvByteUtil.short2Byte(this.getAccountState()));
		TlvObject tAccountId = new TlvObject(5, this.getAccountId());
		TlvObject tAccountName = new TlvObject(6, this.getAccountName());
		TlvObject tAccountFacePhoto = new TlvObject(7, this.getAccountFacePhoto());

		TlvObject tlv = new TlvObject(Command.UPLOAD_CONTACT_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tFriendId);
		tlv.push(tState);
		tlv.push(tAccountId);
		tlv.push(tAccountName);
		tlv.push(tAccountFacePhoto);

		logger.info("from command to tlv package:(tag=" + Command.UPLOAD_CONTACT_RESP + ", child=7, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String friendId;
	private short accountState;
	private String accountId;
	private String accountName;
	private String accountFacePhoto;

	public String getFriendId()
	{
		return friendId;
	}

	public void setFriendId(String friendId)
	{
		this.friendId = friendId;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public String getAccountFacePhoto()
	{
		return accountFacePhoto;
	}

	public void setAccountFacePhoto(String accountFacePhoto)
	{
		this.accountFacePhoto = accountFacePhoto;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public short getAccountState()
	{
		return accountState;
	}

	public void setAccountState(short accountState)
	{
		this.accountState = accountState;
	}

	private final static Logger logger = LoggerFactory.getLogger(UploadContactResp.class);

}
