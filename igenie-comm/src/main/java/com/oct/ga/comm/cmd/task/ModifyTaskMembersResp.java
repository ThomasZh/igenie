package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.GaResultSet;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ModifyTaskMembersResp
		extends RespCommand
{
	public ModifyTaskMembersResp()
	{
		this.setTag(Command.TASKPRO_MODIFY_MEMBERS_RESP);
	}

	public ModifyTaskMembersResp(int sequence, short respState, String taskId, List<GaResultSet> rsArray)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setTaskId(taskId);
		this.setRsArray(rsArray);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tTaskId = new TlvObject(i++, taskId);
		Gson gson = new Gson();
		String json = gson.toJson(rsArray);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tTaskId);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ModifyTaskMembersResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));

		TlvObject tRespState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tRespState.getValue()));

		TlvObject tTaskId = tlv.getChild(i++);
		this.setTaskId(new String(tTaskId.getValue(), "UTF-8"));

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			rsArray = gson.fromJson(json, new TypeToken<List<GaResultSet>>()
			{
			}.getType());
		}

		return this;
	}

	private String taskId;
	private List<GaResultSet> rsArray;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public List<GaResultSet> getRsArray()
	{
		return rsArray;
	}

	public void setRsArray(List<GaResultSet> rsArray)
	{
		this.rsArray = rsArray;
	}

	private final static Logger logger = LoggerFactory.getLogger(ModifyTaskMembersResp.class);

}
