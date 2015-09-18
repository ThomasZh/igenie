package com.oct.ga.comm.cmd.moment;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class DeleteMomentReq
		extends ReqCommand
{
	public DeleteMomentReq()
	{
		super();

		this.setTag(Command.DELETE_MOMENT_REQ);
	}

	public DeleteMomentReq(String channelId, String momentId)
	{
		this();

		this.channelId = channelId;
		this.momentId = momentId;
	}

	public DeleteMomentReq(int sequence, String channelId, String momentId)
	{
		this(channelId, momentId);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tChannelId = new TlvObject(i++, channelId);
		TlvObject tMomentId = new TlvObject(i++, momentId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tChannelId);
		tlv.push(tMomentId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public DeleteMomentReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tChannelId = tlv.getChild(i++);
		channelId = new String(tChannelId.getValue(), "UTF-8");
		logger.debug("channelId: " + channelId);

		TlvObject tMomentId = tlv.getChild(i++);
		momentId = new String(tMomentId.getValue(), "UTF-8");
		logger.debug("momentId: " + momentId);

		return this;
	}

	private String momentId;
	private String channelId;

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getMomentId()
	{
		return momentId;
	}

	public void setMomentId(String momentId)
	{
		this.momentId = momentId;
	}

	private final static Logger logger = LoggerFactory.getLogger(DeleteMomentReq.class);

}
