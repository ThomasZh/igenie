package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.club.ActivitySubscribeDetailInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityQueryFutureFilterByLocPaginationResp
		extends RespCommand
{
	public ActivityQueryFutureFilterByLocPaginationResp()
	{
		this.setTag(Command.ACTIVITY_QUERY_FUTURE_FILTERBY_LOC_PAGINATION_RESP);
	}

	public ActivityQueryFutureFilterByLocPaginationResp(short respState, List<ActivitySubscribeDetailInfo> activities)
	{
		this();

		this.setRespState(respState);
		this.setActivities(activities);
	}

	public ActivityQueryFutureFilterByLocPaginationResp(int sequence, short respState,
			List<ActivitySubscribeDetailInfo> activities)
	{
		this(respState, activities);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		Gson gson = new Gson();
		String json = gson.toJson(activities);
		logger.debug("json: " + json);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityQueryFutureFilterByLocPaginationResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			activities = gson.fromJson(json, new TypeToken<List<ActivitySubscribeDetailInfo>>()
			{
			}.getType());
		}

		return this;
	}

	List<ActivitySubscribeDetailInfo> activities;

	public List<ActivitySubscribeDetailInfo> getActivities()
	{
		return activities;
	}

	public void setActivities(List<ActivitySubscribeDetailInfo> activities)
	{
		this.activities = activities;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryFutureFilterByLocPaginationResp.class);

}
