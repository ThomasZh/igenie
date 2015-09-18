package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class TaskMoveToReq
		extends ReqCommand
{
	public TaskMoveToReq()
	{
		super();

		this.setTag(Command.TASK_MOVE_TO_REQ);
	}

	public TaskMoveToReq(int sequence, String taskId, String projectId)
	{
		this();

		this.setSequence(sequence);
		this.setTaskId(taskId);
		this.setProjectId(projectId);
	}

	@Override
	public TaskMoveToReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tTaskId = tlv.getChild(i++);
		taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tProjectId = tlv.getChild(i++);
		projectId = new String(tProjectId.getValue(), "UTF-8");
		logger.debug("projectId: " + projectId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tTaskId = new TlvObject(i++, taskId);
		TlvObject tProjectId = new TlvObject(i++, projectId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tTaskId);
		tlv.push(tProjectId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String taskId;
	private String projectId;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getProjectId()
	{
		return projectId;
	}

	public void setProjectId(String projectId)
	{
		this.projectId = projectId;
	}

	private final static Logger logger = LoggerFactory.getLogger(TaskMoveToReq.class);

}
