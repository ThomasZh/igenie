package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityQuerySubscribersReq
		extends ReqCommand
{
	public ActivityQuerySubscribersReq()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_SUBSCRIBERS_REQ);
	}

	public ActivityQuerySubscribersReq(String activityId)
	{
		this();

		this.setActivityId(activityId);
	}

	public ActivityQuerySubscribersReq(int sequence, String activityId)
	{
		this(activityId);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tActivityId = new TlvObject(i++, activityId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tActivityId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityQuerySubscribersReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tActivityId = tlv.getChild(i++);
		activityId = new String(tActivityId.getValue(), "UTF-8");
		logger.debug("activityId: " + activityId);

		return this;
	}

	private String activityId;

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityQuerySubscribersReq.class);

}
