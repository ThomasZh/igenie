package com.redoct.ga.sup.sms.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class SendVerificationCodeReq
		extends SupReqCommand
{
	public SendVerificationCodeReq()
	{
		super();

		this.setTag(SupCommandTag.SEND_SMS_VERIFICATION_REQ);
	}

	public SendVerificationCodeReq(String phone, String ekey, String lang)
	{
		this();

		this.setPhone(phone);
		this.setEkey(ekey);
		this.setLang(lang);
	}

	public SendVerificationCodeReq(long sequence, String phone, String ekey, String lang)
	{
		this(phone, ekey, lang);

		this.setSequence(sequence);
	}

	@Override
	public SendVerificationCodeReq decode(TlvObject tlv)
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

		TlvObject tPhone = tlv.getChild(i++);
		phone = new String(tPhone.getValue(), "UTF-8");
		logger.debug("phone: " + phone);

		TlvObject tEkey = tlv.getChild(i++);
		ekey = new String(tEkey.getValue(), "UTF-8");
		logger.debug("ekey: " + ekey);

		TlvObject tLang = tlv.getChild(i++);
		lang = new String(tLang.getValue(), "UTF-8");
		logger.debug("lang: " + lang);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tPhone = new TlvObject(i++, phone);
		TlvObject tEkey = new TlvObject(i++, ekey);
		TlvObject tLang = new TlvObject(i++, lang);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tPhone);
		tlv.push(tEkey);
		tlv.push(tLang);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String phone;
	private String ekey;
	private String lang;

	public String getEkey()
	{
		return ekey;
	}

	public void setEkey(String ekey)
	{
		this.ekey = ekey;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getLang()
	{
		return lang;
	}

	public void setLang(String lang)
	{
		this.lang = lang;
	}

	private final static Logger logger = LoggerFactory.getLogger(SendVerificationCodeReq.class);

}
