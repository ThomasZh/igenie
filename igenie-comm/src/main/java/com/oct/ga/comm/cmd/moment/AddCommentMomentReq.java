package com.oct.ga.comm.cmd.moment;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class AddCommentMomentReq
		extends ReqCommand
{
	public AddCommentMomentReq()
	{
		super();

		this.setTag(Command.ADD_MOMENT_FAVORITE_REQ);
	}

	public AddCommentMomentReq(String momentId, String txt)
	{
		this();

		this.setMomentId(momentId);
		this.setTxt(txt);
	}

	public AddCommentMomentReq(int sequence, String momentId, String txt)
	{
		this(momentId, txt);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tMomentId = new TlvObject(i++, momentId);
		TlvObject tTxt = new TlvObject(i++, txt);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tMomentId);
		tlv.push(tTxt);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public AddCommentMomentReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tMomentId = tlv.getChild(i++);
		momentId = new String(tMomentId.getValue(), "UTF-8");
		logger.debug("momentId: " + momentId);

		TlvObject tTxt = tlv.getChild(i++);
		txt = new String(tTxt.getValue(), "UTF-8");
		logger.debug("txt: " + txt);

		return this;
	}

	private String momentId;
	private String txt;

	public String getMomentId()
	{
		return momentId;
	}

	public void setMomentId(String momentId)
	{
		this.momentId = momentId;
	}

	public String getTxt()
	{
		return txt;
	}

	public void setTxt(String txt)
	{
		this.txt = txt;
	}

	private final static Logger logger = LoggerFactory.getLogger(AddCommentMomentReq.class);

}
