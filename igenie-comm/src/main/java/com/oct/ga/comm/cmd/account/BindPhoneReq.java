package com.oct.ga.comm.cmd.account;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class BindPhoneReq
		extends ReqCommand
{
	public BindPhoneReq()
	{
		super();

		this.setTag(Command.BIND_PHONE_REQ);
	}

	public BindPhoneReq(String phone, String md5pwd, String code)
	{
		this();

		this.setPhone(phone);
		this.setMd5pwd(md5pwd);
		this.setVerificationCode(code);
	}

	public BindPhoneReq(int sequence, String phone, String md5pwd, String code)
	{
		this(phone, md5pwd, code);

		this.setSequence(sequence);
	}

	@Override
	public BindPhoneReq decode(TlvObject tlv)
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

		TlvObject tPhone = tlv.getChild(i++);
		phone = new String(tPhone.getValue(), "UTF-8");
		logger.debug("phone: " + phone);

		TlvObject tPwd = tlv.getChild(i++);
		md5pwd = new String(tPwd.getValue(), "UTF-8");

		TlvObject tCode = tlv.getChild(i++);
		verificationCode = new String(tCode.getValue(), "UTF-8");
		logger.debug("verificationCode: " + verificationCode);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tPhone = new TlvObject(i++, phone);
		TlvObject tPwd = new TlvObject(i++, md5pwd);
		TlvObject tCode = new TlvObject(i++, verificationCode);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tPhone);
		tlv.push(tPwd);
		tlv.push(tCode);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String phone;
	private String md5pwd;
	private String verificationCode;

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getMd5pwd()
	{
		return md5pwd;
	}

	public void setMd5pwd(String md5pwd)
	{
		this.md5pwd = md5pwd;
	}

	public String getVerificationCode()
	{
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode)
	{
		this.verificationCode = verificationCode;
	}

	private final static Logger logger = LoggerFactory.getLogger(BindPhoneReq.class);

}
