package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.club.ActivityCreateInfo;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.comm.domain.publish.GaPublishLoc;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityCreateReq
		extends ReqCommand
{
	public ActivityCreateReq()
	{
		super();

		this.setTag(Command.ACTIVITY_CREATE_REQ);
	}

	public ActivityCreateReq(ActivityCreateInfo activity, String[] subscriberIds)
	{
		this();

		this.setActivity(activity);
		this.setSubscriberIds(subscriberIds);
	}

	public ActivityCreateReq(int sequence, ActivityCreateInfo activity, String[] subscriberIds)
	{
		this(activity, subscriberIds);
		this.setSequence(sequence);
	}

	public ActivityCreateReq(ActivityCreateInfo activity, String[] subscriberIds, List<GaPublishLoc> locations)
	{
		this();

		this.setActivity(activity);
		this.setSubscriberIds(subscriberIds);
		this.setLocations(locations);
	}

	public ActivityCreateReq(int sequence, ActivityCreateInfo activity, String[] subscriberIds,
			List<GaPublishLoc> locations)
	{
		this(activity, subscriberIds, locations);
		this.setSequence(sequence);
	}

	public ActivityCreateReq(int sequence, ActivityCreateInfo activity, String[] subscriberIds,
			List<GaPublishLoc> locations, List<GaDescChapter> descChapters)
	{
		this(sequence, activity, subscriberIds, locations);
		this.setDescChapters(descChapters);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));

		Gson gson = new Gson();
		String jsonActivityInfo = gson.toJson(activity);
		TlvObject tActivityInfo = new TlvObject(i++, jsonActivityInfo);

		TlvObject tSubscriberIds = null;
		if (subscriberIds != null && subscriberIds.length > 0) {
			String json = gson.toJson(subscriberIds);
			tSubscriberIds = new TlvObject(i++, json);
		} else {
			tSubscriberIds = new TlvObject(i++, "");
		}

		String jsonLocations = gson.toJson(locations);
		TlvObject tLocations = new TlvObject(i++, jsonLocations);

		String jsonDescChapters = gson.toJson(descChapters);
		TlvObject tDescChapters = new TlvObject(i++, jsonDescChapters);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tActivityInfo);
		tlv.push(tSubscriberIds);
		tlv.push(tLocations);
		tlv.push(tDescChapters);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityCreateReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 5;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		Gson gson = new Gson();

		TlvObject tActivityInfo = tlv.getChild(i++);
		String jsonActivityInfo = new String(tActivityInfo.getValue(), "UTF-8");
		logger.debug("jsonActivityInfo: " + jsonActivityInfo);
		if (jsonActivityInfo != null) {
			activity = gson.fromJson(jsonActivityInfo, ActivityCreateInfo.class);
		}

		TlvObject tSubscriberIds = tlv.getChild(i++);
		String jsonSubscriberIds = new String(tSubscriberIds.getValue(), "UTF-8");
		logger.debug("jsonSubscriberIds: " + jsonSubscriberIds);
		if (jsonSubscriberIds != null) {
			subscriberIds = gson.fromJson(jsonSubscriberIds, String[].class);
		}

		TlvObject tLocations = tlv.getChild(i++);
		String locationsJson = new String(tLocations.getValue(), "UTF-8");
		logger.debug("locationsJson: " + locationsJson);
		if (locationsJson != null && locationsJson.length() > 0) {
			locations = gson.fromJson(locationsJson, new TypeToken<List<GaPublishLoc>>()
			{
			}.getType());
		}

		TlvObject tDescChapters = tlv.getChild(i++);
		String jsonDescChapters = new String(tDescChapters.getValue(), "UTF-8");
		logger.debug("jsonDescChapters: " + jsonDescChapters);
		if (jsonDescChapters != null && jsonDescChapters.length() > 0) {
			descChapters = gson.fromJson(jsonDescChapters, new TypeToken<List<GaDescChapter>>()
			{
			}.getType());
		}

		return this;
	}

	private ActivityCreateInfo activity;
	private String[] subscriberIds;
	private List<GaPublishLoc> locations;
	private List<GaDescChapter> descChapters;

	public ActivityCreateInfo getActivity()
	{
		return activity;
	}

	public void setActivity(ActivityCreateInfo activity)
	{
		this.activity = activity;
	}

	public String[] getSubscriberIds()
	{
		return subscriberIds;
	}

	public void setSubscriberIds(String[] subscriberIds)
	{
		this.subscriberIds = subscriberIds;
	}

	public List<GaPublishLoc> getLocations()
	{
		return locations;
	}

	public void setLocations(List<GaPublishLoc> locations)
	{
		this.locations = locations;
	}

	public List<GaDescChapter> getDescChapters()
	{
		return descChapters;
	}

	public void setDescChapters(List<GaDescChapter> descChapters)
	{
		this.descChapters = descChapters;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityCreateReq.class);

}
