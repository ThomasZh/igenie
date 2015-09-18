package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.club.ActivityUpdateInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityUpdateReq
		extends ReqCommand
{
	public ActivityUpdateReq()
	{
		super();

		this.setTag(Command.ACTIVITY_UPDATE_REQ);
	}

	public ActivityUpdateReq(int sequence, ActivityUpdateInfo activity)
	{
		this();

		this.setSequence(sequence);
		this.setActivity(activity);
	}

	@Override
	public ActivityUpdateReq decode(TlvObject tlv)
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

		Gson gson = new Gson();

		TlvObject tActivityInfo = tlv.getChild(i++);
		String jsonActivityInfo = new String(tActivityInfo.getValue(), "UTF-8");
		logger.debug("jsonActivityInfo: " + jsonActivityInfo);
		if (jsonActivityInfo != null) {
			activity = gson.fromJson(jsonActivityInfo, ActivityUpdateInfo.class);
		}

		return this;
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

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tActivityInfo);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private ActivityUpdateInfo activity;

	public ActivityUpdateInfo getActivity()
	{
		return activity;
	}

	public void setActivity(ActivityUpdateInfo activity)
	{
		this.activity = activity;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityUpdateReq.class);

}
