package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityQueryDetailResp
		extends RespCommand
{

	public ActivityQueryDetailResp()
	{
		this.setTag(Command.ACTIVITY_QUERY_DETAIL_RESP);
	}

	public ActivityQueryDetailResp(short respState, ActivityDetailInfo activity)
	{
		this();

		this.setRespState(respState);
		this.setActivity(activity);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tJson = null;
		Gson gson = new Gson();
		if (activity != null) {
			String json = gson.toJson(activity);
			tJson = new TlvObject(i++, json);
		} else {
			tJson = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityQueryDetailResp decode(TlvObject tlv)
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

		Gson gson = new Gson();
		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		if (json != null) {
			activity = gson.fromJson(json, ActivityDetailInfo.class);
		}

		return this;
	}

	private ActivityDetailInfo activity;

	public ActivityDetailInfo getActivity()
	{
		return activity;
	}

	public void setActivity(ActivityDetailInfo activity)
	{
		this.activity = activity;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryDetailResp.class);

}
