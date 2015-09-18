package com.redoct.ga.sup.mail.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class SendFriendInviteEmailReq
		extends SupReqCommand
{
	public SendFriendInviteEmailReq()
	{
		super();

		this.setTag(SupCommandTag.SEND_FRIEND_INVTE_EMAIL_REQ);
	}

	public SendFriendInviteEmailReq(String fromEmail, String fromName, String toEmail, String toName, String ekey)
	{
		this();

		this.setFromEmail(fromEmail);
		this.setFromName(fromName);
		this.setToEmail(toEmail);
		this.setToName(toName);
		this.setEkey(ekey);
	}

	public SendFriendInviteEmailReq(long sequence, String fromEmail, String fromName, String toEmail, String toName,
			String ekey)
	{
		this(fromEmail, fromName, toEmail, toName, ekey);

		this.setSequence(sequence);
	}

	@Override
	public SendFriendInviteEmailReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 6;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tFromEmail = tlv.getChild(i++);
		fromEmail = new String(tFromEmail.getValue(), "UTF-8");
		logger.debug("fromEmail: " + fromEmail);

		TlvObject tFromName = tlv.getChild(i++);
		fromName = new String(tFromName.getValue(), "UTF-8");
		logger.debug("fromName: " + fromName);

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
		TlvObject tFromEmail = new TlvObject(i++, fromEmail);
		TlvObject tFromName = new TlvObject(i++, fromName);
		TlvObject tToEmail = new TlvObject(i++, toEmail);
		TlvObject tToName = new TlvObject(i++, toName);
		TlvObject tEkey = new TlvObject(i++, ekey);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tFromEmail);
		tlv.push(tFromName);
		tlv.push(tToEmail);
		tlv.push(tToName);
		tlv.push(tEkey);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String fromEmail;
	private String fromName;
	private String toEmail;
	private String toName;
	private String ekey;

	public String getFromEmail()
	{
		return fromEmail;
	}

	public void setFromEmail(String fromEmail)
	{
		this.fromEmail = fromEmail;
	}

	public String getFromName()
	{
		return fromName;
	}

	public void setFromName(String fromName)
	{
		this.fromName = fromName;
	}

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

	private final static Logger logger = LoggerFactory.getLogger(SendFriendInviteEmailReq.class);

}
