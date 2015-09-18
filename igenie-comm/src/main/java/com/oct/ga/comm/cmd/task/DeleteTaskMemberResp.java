package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class DeleteTaskMemberResp
		extends RespCommand
{
	public DeleteTaskMemberResp()
	{
		this.setTag(Command.DELETE_TASK_MEMBER_RESP);
	}

	public DeleteTaskMemberResp(short respState, String taskId, String friendId)
	{
		this();

		this.setRespState(respState);
		this.setFriendId(friendId);
		this.setTaskId(taskId);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tTaskId = new TlvObject(3, this.getTaskId());
		TlvObject tFriendId = new TlvObject(4, this.getFriendId());

		TlvObject tlv = new TlvObject(Command.DELETE_TASK_MEMBER_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tTaskId);
		tlv.push(tFriendId);

		logger.info("from command to tlv package:(tag=" + Command.DELETE_TASK_MEMBER_RESP + ", child=4, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String taskId;
	private String friendId;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getFriendId()
	{
		return friendId;
	}

	public void setFriendId(String friendId)
	{
		this.friendId = friendId;
	}

	private final static Logger logger = LoggerFactory.getLogger(DeleteTaskMemberResp.class);
}
