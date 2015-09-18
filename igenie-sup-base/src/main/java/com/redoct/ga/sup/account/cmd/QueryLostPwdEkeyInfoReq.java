package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class QueryLostPwdEkeyInfoReq
		extends SupReqCommand
{
	public QueryLostPwdEkeyInfoReq()
	{
		super();

		this.setTag(SupCommandTag.QUERY_LOST_PWD_EKEY_INFO_REQ);
	}

	public QueryLostPwdEkeyInfoReq(String ekey)
	{
		this();

		this.setEkey(ekey);
	}

	public QueryLostPwdEkeyInfoReq(long sequence, String ekey)
	{
		this(ekey);

		this.setSequence(sequence);
	}

	@Override
	public QueryLostPwdEkeyInfoReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

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
		TlvObject tEkey = new TlvObject(i++, ekey);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tEkey);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String ekey;

	public String getEkey()
	{
		return ekey;
	}

	public void setEkey(String ekey)
	{
		this.ekey = ekey;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryLostPwdEkeyInfoReq.class);

}
