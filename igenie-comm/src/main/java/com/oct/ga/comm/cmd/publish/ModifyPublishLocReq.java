package com.oct.ga.comm.cmd.publish;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.publish.GaPublishLoc;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ModifyPublishLocReq
		extends ReqCommand
{
	public ModifyPublishLocReq()
	{
		super();

		this.setTag(Command.PUBLISH_MODIFY_LOC_REQ);
	}

	public ModifyPublishLocReq(int sequence, String activityId, List<GaPublishLoc> locations)
	{
		this();

		this.setSequence(sequence);
		this.setActivityId(activityId);
		this.setLocations(locations);
	}

	@Override
	public ModifyPublishLocReq decode(TlvObject tlv)
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

		TlvObject tLocations = tlv.getChild(i++);
		String locationsJson = new String(tLocations.getValue(), "UTF-8");
		logger.debug("locationsJson: " + locationsJson);
		Gson gson = new Gson();
		if (locationsJson != null && locationsJson.length() > 0) {
			locations = gson.fromJson(locationsJson, new TypeToken<List<GaPublishLoc>>()
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
		String jsonLocations = gson.toJson(locations);
		TlvObject tLocations = new TlvObject(i++, jsonLocations);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tActivityId);
		tlv.push(tLocations);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String activityId;
	private List<GaPublishLoc> locations;

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	public List<GaPublishLoc> getLocations()
	{
		return locations;
	}

	public void setLocations(List<GaPublishLoc> locations)
	{
		this.locations = locations;
	}

	private final static Logger logger = LoggerFactory.getLogger(ModifyPublishLocReq.class);
}
