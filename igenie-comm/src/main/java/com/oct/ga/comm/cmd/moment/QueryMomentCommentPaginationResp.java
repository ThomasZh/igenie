package com.oct.ga.comm.cmd.moment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.moment.GaMomentCommentObject;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMomentCommentPaginationResp
		extends RespCommand
{
	public QueryMomentCommentPaginationResp()
	{
		this.setTag(Command.QUERY_MOMENT_COMMENT_PAGINATION_RESP);
	}

	public QueryMomentCommentPaginationResp(short respState, List<GaMomentCommentObject> comments)
	{
		this();

		this.setRespState(respState);
		this.setComments(comments);
	}

	public QueryMomentCommentPaginationResp(int sequence, short respState, List<GaMomentCommentObject> comments)
	{
		this(respState, comments);

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
		String json = gson.toJson(comments);
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
	public QueryMomentCommentPaginationResp decode(TlvObject tlv)
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
			comments = gson.fromJson(json, new TypeToken<List<GaMomentCommentObject>>()
			{
			}.getType());
		}

		return this;
	}

	List<GaMomentCommentObject> comments;

	public List<GaMomentCommentObject> getComments()
	{
		return comments;
	}

	public void setComments(List<GaMomentCommentObject> comments)
	{
		this.comments = comments;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryMomentCommentPaginationResp.class);

}
