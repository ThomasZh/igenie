package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class DeleteTaskResp
		extends RespCommand
{
	public DeleteTaskResp()
	{
		this.setTag(Command.DELETE_TASK_RESP);
	}

	public DeleteTaskResp(short respState, String taskId)
	{
		this();

		this.setRespState(respState);
		this.setTaskId(taskId);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tResultFlag = new TlvObject(1, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tTaskId = new TlvObject(2, taskId);

		// 6 + 2 + 6 + taskId
		int pkgLen = 14 + tTaskId.getLength();
		logger.info("from command to tlv package:(tag=" + Command.DELETE_TASK_RESP + ", child=2, length=" + pkgLen
				+ ")");
		TlvObject tlv = new TlvObject(Command.DELETE_TASK_RESP, pkgLen);
		tlv.add(tResultFlag);
		tlv.add(tTaskId);
		return tlv;
	}

	private String taskId;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	private final static Logger logger = LoggerFactory.getLogger(DeleteTaskResp.class);

}
