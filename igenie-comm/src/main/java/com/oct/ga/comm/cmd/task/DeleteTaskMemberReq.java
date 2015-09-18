package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class DeleteTaskMemberReq
		extends ReqCommand
{
	public DeleteTaskMemberReq()
	{
		super();

		this.setTag(Command.DELETE_TASK_MEMBER_REQ);
	}

	public DeleteTaskMemberReq(String taskId, String accountId)
	{
		this();

		this.setTaskId(taskId);
		this.setMemberAccountId(accountId);
	}

	@Override
	public DeleteTaskMemberReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.DELETE_TASK_MEMBER_REQ + ", child=3) to command");

		TlvParser.decodeChildren(tlv, 3);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tTaskId = tlv.getChild(1);
		taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tAccountId = tlv.getChild(2);
		memberAccountId = new String(tAccountId.getValue(), "UTF-8");
		logger.debug("myAccountId: " + memberAccountId);

		return this;
	}

	private String taskId;
	private String memberAccountId;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getMemberAccountId()
	{
		return memberAccountId;
	}

	public void setMemberAccountId(String memberAccountId)
	{
		this.memberAccountId = memberAccountId;
	}

	private final static Logger logger = LoggerFactory.getLogger(DeleteTaskMemberReq.class);

}
