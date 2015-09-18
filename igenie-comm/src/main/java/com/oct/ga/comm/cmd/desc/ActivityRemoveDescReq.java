package com.oct.ga.comm.cmd.desc;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityRemoveDescReq
		extends ReqCommand
{
	public ActivityRemoveDescReq()
	{
		super();

		this.setTag(Command.ACTIVITY_DESC_REMOVE_REQ);
	}

	public ActivityRemoveDescReq(int sequence, String activityId, short seq)
	{
		this();
		
		this.setSequence(sequence);
		this.setActivityId(activityId);
		this.setSeq(seq);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tActivityId = new TlvObject(i++, activityId);
		TlvObject tSeq = new TlvObject(i++, 2, TlvByteUtil.short2Byte(seq));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tActivityId);
		tlv.push(tSeq);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityRemoveDescReq decode(TlvObject tlv)
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

		TlvObject tActivityId = tlv.getChild(i++);
		activityId = new String(tActivityId.getValue(), "UTF-8");
		logger.debug("activityId: " + activityId);

		TlvObject tSeq = tlv.getChild(i++);
		seq = TlvByteUtil.byte2Short(tSeq.getValue());
		logger.debug("seq: " + seq);

		return this;
	}

	private String activityId;
	private short seq;

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	public short getSeq()
	{
		return seq;
	}

	public void setSeq(short seq)
	{
		this.seq = seq;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityRemoveDescReq.class);

}
