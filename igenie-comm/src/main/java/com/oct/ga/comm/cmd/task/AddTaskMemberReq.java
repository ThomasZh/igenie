package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class AddTaskMemberReq
		extends ReqCommand
{
	public final static short RESP_NOT_GA_ACCOUNT = 2;
	public final static short RESP_ALREADY_MEMBER = 3;

	public AddTaskMemberReq()
	{
		super();

		this.setTag(Command.ADD_TASK_MEMBER_REQ);
	}

	public AddTaskMemberReq(String taskId, String taskName, String toAccountId)
	{
		this();

		this.setTaskId(taskId);
		this.setTaskName(taskName);
		this.setToAccountId(toAccountId);
	}

	@Override
	public AddTaskMemberReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.ADD_TASK_MEMBER_REQ + ", child=4) to command");

		TlvParser.decodeChildren(tlv, 4);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tTaskId = tlv.getChild(1);
		String taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tTaskName = tlv.getChild(2);
		String taskName = new String(tTaskName.getValue(), "UTF-8");
		logger.debug("taskName: " + taskName);

		TlvObject tToAccountId = tlv.getChild(3);
		String toAccountId = new String(tToAccountId.getValue(), "UTF-8");
		logger.debug("toAccountId: " + toAccountId);

		this.setTaskId(taskId);
		this.setTaskName(taskName);
		this.setToAccountId(toAccountId);

		return this;
	}

	// //////////////////////////////////////////////////////////////////////////
	private String taskId;
	private String taskName;
	private String toAccountId;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getToAccountId()
	{
		return toAccountId;
	}

	public void setToAccountId(String toAccountId)
	{
		this.toAccountId = toAccountId;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	private static final Logger logger = LoggerFactory.getLogger(AddTaskMemberReq.class);

}
