package com.oct.ga.comm.cmd.moment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.moment.GaMomentPhotoObject;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMomentPhotoFlowPaginationResp
		extends RespCommand
{
	public QueryMomentPhotoFlowPaginationResp()
	{
		this.setTag(Command.QUERY_MOMENT_PHOTOFLOW_PAGINATION_RESP);
	}

	public QueryMomentPhotoFlowPaginationResp(List<GaMomentPhotoObject> momentPhotos)
	{
		this();

		this.setMomentPhotos(momentPhotos);
	}

	public QueryMomentPhotoFlowPaginationResp(int sequence, short respState, List<GaMomentPhotoObject> momentPhotos)
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
		if (momentPhotos != null) {
			Gson gson = new Gson();
			String json = gson.toJson(momentPhotos);
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
	public QueryMomentPhotoFlowPaginationResp decode(TlvObject tlv)
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
		if (json != null) {
			Gson gson = new Gson();
			momentPhotos = gson.fromJson(json, new TypeToken<List<GaMomentPhotoObject>>()
			{
			}.getType());
		}

		return this;
	}

	List<GaMomentPhotoObject> momentPhotos;

	public List<GaMomentPhotoObject> getMomentPhotos()
	{
		return momentPhotos;
	}

	public void setMomentPhotos(List<GaMomentPhotoObject> momentPhotos)
	{
		this.momentPhotos = momentPhotos;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryMomentPhotoFlowPaginationResp.class);

}
