package com.oct.ga.comm.cmd.desc;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityModifyAllDescReq
		extends ReqCommand
{
	public ActivityModifyAllDescReq()
	{
		super();

		this.setTag(Command.ACTIVITY_DESC_MODIFY_ALL_REQ);
	}

	public ActivityModifyAllDescReq(int sequence, String activityId, List<GaDescChapter> chapters)
	{
		this();

		this.setSequence(sequence);
		this.setActivityId(activityId);
		this.setDescChapters(chapters);
	}

	@Override
	public ActivityModifyAllDescReq decode(TlvObject tlv)
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
			descChapters = gson.fromJson(json, new TypeToken<List<GaDescChapter>>()
			{
			}.getType());
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
		String json = gson.toJson(descChapters);
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
	private List<GaDescChapter> descChapters;

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	public List<GaDescChapter> getDescChapters()
	{
		return descChapters;
	}

	public void setDescChapters(List<GaDescChapter> descChapters)
	{
		this.descChapters = descChapters;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityModifyAllDescReq.class);

}
