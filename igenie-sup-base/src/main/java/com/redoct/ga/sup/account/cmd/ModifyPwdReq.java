package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class ModifyPwdReq
		extends SupReqCommand
{
	public ModifyPwdReq()
	{
		super();

		this.setTag(SupCommandTag.MODIFY_PWD_REQ);
	}

	public ModifyPwdReq(short loginType, String loginName, String md5Pwd)
	{
		this();

		this.setLoginType(loginType);
		this.setLoginName(loginName);
		this.setMd5Pwd(md5Pwd);
	}

	public ModifyPwdReq(long sequence, short loginType, String loginName, String md5Pwd)
	{
		this(loginType, loginName, md5Pwd);

		this.setSequence(sequence);
	}

	@Override
	public ModifyPwdReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tLoginType = tlv.getChild(i++);
		loginType = TlvByteUtil.byte2Short(tLoginType.getValue());
		logger.debug("loginType: " + loginType);

		TlvObject tLoginName = tlv.getChild(i++);
		loginName = new String(tLoginName.getValue(), "UTF-8");
		logger.debug("loginName: " + loginName);

		TlvObject tMd5Pwd = tlv.getChild(i++);
		md5Pwd = new String(tMd5Pwd.getValue(), "UTF-8");

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tLoginType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(loginType));
		TlvObject tLoginName = new TlvObject(i++, loginName);
		TlvObject tMd5Pwd = new TlvObject(i++, md5Pwd);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tLoginType);
		tlv.push(tLoginName);
		tlv.push(tMd5Pwd);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private short loginType;
	private String loginName;
	private String md5Pwd;

	public short getLoginType()
	{
		return loginType;
	}

	public void setLoginType(short loginType)
	{
		this.loginType = loginType;
	}

	public String getLoginName()
	{
		return loginName;
	}

	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	public String getMd5Pwd()
	{
		return md5Pwd;
	}

	public void setMd5Pwd(String md5Pwd)
	{
		this.md5Pwd = md5Pwd;
	}

	private final static Logger logger = LoggerFactory.getLogger(ModifyPwdReq.class);

}
