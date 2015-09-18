package com.oct.ga.comm.cmd.moment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryAllMomentsPaginationResp
		extends RespCommand
{
	public QueryAllMomentsPaginationResp()
	{
		this.setTag(Command.QUERY_ALL_MOMENT_PAGINATION_RESP);
	}

	public QueryAllMomentsPaginationResp(List<GaMomentObject> moments)
	{
		this();

		this.setMoments(moments);
	}

	public QueryAllMomentsPaginationResp(int sequence, short respState, List<GaMomentObject> momentPhotos)
	{
		this(momentPhotos);

		this.setSequence(sequence);
		this.setRespState(respState);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;
		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tJson = null;
		if (moments != null) {
			Gson gson = new Gson();
			String json = gson.toJson(moments);
			logger.debug("json: " + json);
			tJson = new TlvObject(i++, json);
		} else {
			tJson = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.info("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public QueryAllMomentsPaginationResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.info("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		if (json != null) {
			Gson gson = new Gson();
			moments = gson.fromJson(json, new TypeToken<List<GaMomentObject>>()
			{
			}.getType());
		}
		logger.debug("json: " + json);

		return this;
	}

	private List<GaMomentObject> moments;

	public List<GaMomentObject> getMoments()
	{
		return moments;
	}

	public void setMoments(List<GaMomentObject> moments)
	{
		this.moments = moments;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryAllMomentsPaginationResp.class);

}
