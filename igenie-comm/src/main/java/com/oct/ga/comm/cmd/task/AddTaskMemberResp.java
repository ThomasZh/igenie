package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class AddTaskMemberResp
		extends RespCommand
{
	public AddTaskMemberResp()
	{
		this.setTag(Command.ADD_TASK_MEMBER_RESP);
	}

	public AddTaskMemberResp(short respState, String taskId, String friendAccountId)
	{
		this();

		this.setRespState(respState);
		this.setTaskId(taskId);
		this.setFriendAccountId(friendAccountId);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tTaskId = new TlvObject(3, this.getTaskId());
		TlvObject tFriendId = new TlvObject(4, this.getFriendAccountId());

		TlvObject tlv = new TlvObject(Command.ADD_TASK_MEMBER_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tTaskId);
		tlv.push(tFriendId);

		logger.info("from command to tlv package:(tag=" + Command.ADD_TASK_MEMBER_RESP + ", child=4, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String taskId;
	private String friendAccountId;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getFriendAccountId()
	{
		return friendAccountId;
	}

	public void setFriendAccountId(String friendAccountId)
	{
		this.friendAccountId = friendAccountId;
	}

	private final static Logger logger = LoggerFactory.getLogger(AddTaskMemberResp.class);
}
