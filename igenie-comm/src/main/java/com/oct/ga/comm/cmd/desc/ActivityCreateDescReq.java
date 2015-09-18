package com.oct.ga.comm.cmd.desc;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityCreateDescReq
		extends ReqCommand
{
	public ActivityCreateDescReq()
	{
		super();

		this.setTag(Command.ACTIVITY_DESC_CREATE_REQ);
	}

	public ActivityCreateDescReq(int sequence, String activityId, GaDescChapter chapter)
	{
		this();

		this.setSequence(sequence);
		this.setActivityId(activityId);
		this.setChapter(chapter);
	}

	@Override
	public ActivityCreateDescReq decode(TlvObject tlv)
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

		TlvObject tDesc = tlv.getChild(i++);
		String json = new String(tDesc.getValue(), "UTF-8");
		logger.debug("json: " + json);
		Gson gson = new Gson();
		if (json != null && json.length() > 0) {
			chapter = gson.fromJson(json, GaDescChapter.class);
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tActivityId = new TlvObject(i++, activityId);
		Gson gson = new Gson();
		String json = gson.toJson(chapter);
		TlvObject tDesc = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tActivityId);
		tlv.push(tDesc);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String activityId;
	private GaDescChapter chapter;

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	public GaDescChapter getChapter()
	{
		return chapter;
	}

	public void setChapter(GaDescChapter chapter)
	{
		this.chapter = chapter;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityCreateDescReq.class);

}
