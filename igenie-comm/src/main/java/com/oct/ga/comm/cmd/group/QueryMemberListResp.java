package com.oct.ga.comm.cmd.group;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.group.GroupMemberMasterInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMemberListResp
		extends RespCommand
{
	public QueryMemberListResp()
	{
		this.setTag(Command.QUERY_MEMBER_LIST_RESP);
	}

	public QueryMemberListResp(short respState, List<GroupMemberMasterInfo> members)
	{
		this();

		this.setRespState(respState);
		this.setMembers(members);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		Gson gson = new Gson();
		String json = gson.toJson(members);
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
	public QueryMemberListResp decode(TlvObject tlv)
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

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		Gson gson = new Gson();
		if (json != null && json.length() > 0) {
			members = gson.fromJson(json, new TypeToken<List<GroupMemberMasterInfo>>()
			{
			}.getType());
		}

		return this;
	}

	private List<GroupMemberMasterInfo> members;

	public List<GroupMemberMasterInfo> getMembers()
	{
		return members;
	}

	public void setMembers(List<GroupMemberMasterInfo> members)
	{
		this.members = members;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryMemberListResp.class);

}
