package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

/**
 * for administrator
 * 
 * @author thomas
 * 
 */
public class QueryAllAccountBasicReq
		extends SupReqCommand
{
	public QueryAllAccountBasicReq()
	{
		super();

		this.setTag(SupCommandTag.QUERY_ALL_ACCOUNT_BASIC_REQ);
	}

	public QueryAllAccountBasicReq(long sequence)
	{
		this();

		this.setSequence(sequence);
	}

	@Override
	public QueryAllAccountBasicReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 1;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryAllAccountBasicReq.class);

}
