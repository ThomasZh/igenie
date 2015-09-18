package com.oct.ga.comm.cmd.account;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ResetPwdReq
		extends ReqCommand
{
	public ResetPwdReq()
	{
		super();

		this.setTag(Command.RESET_PASSWORD_REQ);
	}

	public ResetPwdReq(String ekey, String email, String newPassword)
	{
		this();

		this.setEkey(ekey);
		this.setEmail(email);
		this.setNewPassword(newPassword);
	}

	@Override
	public ResetPwdReq decode(TlvObject tlv)
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

		TlvObject tEkey = tlv.getChild(i++);
		ekey = new String(tEkey.getValue(), "UTF-8");
		logger.info("ekey: " + ekey);

		TlvObject tEmail = tlv.getChild(i++);
		email = new String(tEmail.getValue(), "UTF-8");
		logger.info("email: " + email);

		TlvObject tNewPassword = tlv.getChild(i++);
		newPassword = new String(tNewPassword.getValue(), "UTF-8");

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tKey = new TlvObject(i++, this.getEkey());
		TlvObject tEmail = new TlvObject(i++, this.getEmail());
		TlvObject tNewPwd = new TlvObject(i++, this.getNewPassword());

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tKey);
		tlv.push(tEmail);
		tlv.push(tNewPwd);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String email;
	private String ekey;
	private String newPassword;

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getEkey()
	{
		return ekey;
	}

	public void setEkey(String ekey)
	{
		this.ekey = ekey;
	}

	public String getNewPassword()
	{
		return newPassword;
	}

	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

	private final static Logger logger = LoggerFactory.getLogger(ResetPwdReq.class);
}
