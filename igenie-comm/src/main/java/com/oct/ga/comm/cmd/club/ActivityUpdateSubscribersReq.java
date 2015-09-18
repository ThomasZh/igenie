package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityUpdateSubscribersReq
		extends ReqCommand
{
	public ActivityUpdateSubscribersReq()
	{
		super();

		this.setTag(Command.ACTIVITY_UPDATE_SUBSCRIBERS_REQ);
	}

	public ActivityUpdateSubscribersReq(String activityId, String[] subscriberIds)
	{
		this();

		this.setActivityId(activityId);
		this.setSubscriberIds(subscriberIds);
	}

	public ActivityUpdateSubscribersReq(int sequence, String activityId, String[] subscriberIds)
	{
		this(activityId, subscriberIds);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tActivityId = new TlvObject(i++, activityId);

		Gson gson = new Gson();
		TlvObject tSubscriberIds = null;
		if (subscriberIds != null && subscriberIds.length > 0) {
			String json = gson.toJson(subscriberIds);
			tSubscriberIds = new TlvObject(i++, json);
		} else {
			tSubscriberIds = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tActivityId);
		tlv.push(tSubscriberIds);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityUpdateSubscribersReq decode(TlvObject tlv)
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

		Gson gson = new Gson();
		TlvObject tSubscriberIds = tlv.getChild(i++);
		String jsonSubscriberIds = new String(tSubscriberIds.getValue(), "UTF-8");
		logger.debug("jsonSubscriberIds: " + jsonSubscriberIds);
		if (jsonSubscriberIds != null) {
			subscriberIds = gson.fromJson(jsonSubscriberIds, String[].class);
		}

		return this;
	}

	private String activityId;
	private String[] subscriberIds;

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	public String[] getSubscriberIds()
	{
		return subscriberIds;
	}

	public void setSubscriberIds(String[] subscriberIds)
	{
		this.subscriberIds = subscriberIds;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityUpdateSubscribersReq.class);

}
