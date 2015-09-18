package com.redoct.ga.sup.mail.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class SendForgotPwdEmailReq
		extends SupReqCommand
{
	public SendForgotPwdEmailReq()
	{
		super();

		this.setTag(SupCommandTag.SEND_FORGOT_PWD_EMAIL_REQ);
	}

	public SendForgotPwdEmailReq(String toEmail, String toName, String ekey)
	{
		this();

		this.setToEmail(toEmail);
		this.setToName(toName);
		this.setEkey(ekey);
	}

	public SendForgotPwdEmailReq(long sequence, String toEmail, String toName, String ekey)
	{
		this(toEmail, toName, ekey);

		this.setSequence(sequence);
	}

	@Override
	public SendForgotPwdEmailReq decode(TlvObject tlv)
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

		TlvObject tToEmail = tlv.getChild(i++);
		toEmail = new String(tToEmail.getValue(), "UTF-8");
		logger.debug("toEmail: " + toEmail);

		TlvObject tToName = tlv.getChild(i++);
		toName = new String(tToName.getValue(), "UTF-8");
		logger.debug("toName: " + toName);

		TlvObject tEkey = tlv.getChild(i++);
		ekey = new String(tEkey.getValue(), "UTF-8");
		logger.debug("ekey: " + ekey);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tToEmail = new TlvObject(i++, toEmail);
		TlvObject tToName = new TlvObject(i++, toName);
		TlvObject tEkey = new TlvObject(i++, ekey);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tToEmail);
		tlv.push(tToName);
		tlv.push(tEkey);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String toEmail;
	private String toName;
	private String ekey;

	public String getToEmail()
	{
		return toEmail;
	}

	public void setToEmail(String toEmail)
	{
		this.toEmail = toEmail;
	}

	public String getToName()
	{
		return toName;
	}

	public void setToName(String toName)
	{
		this.toName = toName;
	}

	public String getEkey()
	{
		return ekey;
	}

	public void setEkey(String ekey)
	{
		this.ekey = ekey;
	}

	private final static Logger logger = LoggerFactory.getLogger(SendForgotPwdEmailReq.class);

}
