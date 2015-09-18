package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.taskpro.TaskProBaseInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncTaskChildResp
		extends RespCommand
{
	public SyncTaskChildResp()
	{
		this.setTag(Command.SYNC_TASKPRO_CHILD_RESP);
	}

	public SyncTaskChildResp(int sequence, short state,String taskId, int version, List<TaskProBaseInfo> childTasks)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(state);
		this.setTaskId(taskId);
		this.setVersion(version);
		this.setChildTasks(childTasks);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tTaskId = new TlvObject(i++, taskId);
		TlvObject tVersion = new TlvObject(i++, 4, TlvByteUtil.int2Byte(version));
		Gson gson = new Gson();
		String json = gson.toJson(childTasks);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tTaskId);
		tlv.push(tVersion);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public SyncTaskChildResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		int childCount = 5;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));

		TlvObject tState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tState.getValue()));
		logger.debug("state: " + this.getRespState());
		
		TlvObject tTaskId = tlv.getChild(i++);
		this.setTaskId(new String(tTaskId.getValue(), "UTF-8"));

		TlvObject tVersion = tlv.getChild(i++);
		this.setVersion(TlvByteUtil.byte2Int(tVersion.getValue()));

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			childTasks = gson.fromJson(json, new TypeToken<List<TaskProBaseInfo>>()
			{
			}.getType());
		}

		return this;
	}

	private String taskId;
	private int version;
	private List<TaskProBaseInfo> childTasks;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	public List<TaskProBaseInfo> getChildTasks()
	{
		return childTasks;
	}

	public void setChildTasks(List<TaskProBaseInfo> childTasks)
	{
		this.childTasks = childTasks;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskChildResp.class);

}
