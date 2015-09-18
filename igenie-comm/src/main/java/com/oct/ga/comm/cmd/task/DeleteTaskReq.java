package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class DeleteTaskReq
		extends ReqCommand
{
	public DeleteTaskReq()
	{
		super();

		this.setTag(Command.DELETE_TASK_REQ);
	}

	public DeleteTaskReq(String taskId)
	{
		this();

		this.setTaskId(taskId);
	}

	@Override
	public DeleteTaskReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.DELETE_TASK_REQ + ", child=1) to command");

		TlvParser.decodeChildren(tlv, 1);

		TlvObject tTaskId = tlv.getChild(0);
		taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		return this;
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

	private final static Logger logger = LoggerFactory.getLogger(DeleteTaskReq.class);

}
