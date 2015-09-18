package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class SyncTaskInfoResp
		extends RespCommand
{
	public SyncTaskInfoResp()
	{
		super();
		this.setTag(Command.SYNC_TASK_INFO_RESP);
	}

	public SyncTaskInfoResp(TaskProExtInfo task)
	{
		this();
		this.setTask(task);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TaskProExtInfo task = this.getTask();
		TlvObject tTaskId = new TlvObject(1, task.getId());
		TlvObject tTaskParentId = new TlvObject(2, task.getPid());
		TlvObject tTaskName = new TlvObject(3, task.getName());
		TlvObject tTaskDesc = new TlvObject(4, task.getDesc());
		TlvObject tCreateAccountId = new TlvObject(5, task.getCreateAccountId());
		TlvObject tStartTime = new TlvObject(6, 4, TlvByteUtil.int2Byte(task.getStartTime()));
		TlvObject tEndTime = new TlvObject(7, 4, TlvByteUtil.int2Byte(task.getEndTime()));
		TlvObject tMemberNum = new TlvObject(8, 2, TlvByteUtil.short2Byte(task.getMemberNum()));
		TlvObject tFileNum = new TlvObject(9, 2, TlvByteUtil.short2Byte(task.getFileNum()));
		TlvObject tChildNum = new TlvObject(10, 2, TlvByteUtil.short2Byte(task.getChildNum()));
		TlvObject tState = new TlvObject(11, 2, TlvByteUtil.short2Byte(task.getState()));
		TlvObject tColor = new TlvObject(12, 2, TlvByteUtil.short2Byte(task.getColor()));
		TlvObject tExtAttribute = new TlvObject(13, task.getExtAttribute());
		TlvObject tTemplateId = new TlvObject(14, task.getTemplateId());
		TlvObject tImagePath = new TlvObject(15, task.getTitleBkImage());
		TlvObject tDepth = new TlvObject(16, 2, TlvByteUtil.short2Byte(task.getDepth()));
		TlvObject tlv = new TlvObject(Command.SYNC_TASK_INFO_RESP);
		tlv.push(tTaskId);
		tlv.push(tTaskParentId);
		tlv.push(tTaskName);
		tlv.push(tTaskDesc);
		tlv.push(tCreateAccountId);
		tlv.push(tStartTime);
		tlv.push(tEndTime);
		tlv.push(tMemberNum);
		tlv.push(tFileNum);
		tlv.push(tChildNum);
		tlv.push(tState);
		tlv.push(tColor);
		tlv.push(tExtAttribute);
		tlv.push(tTemplateId);
		tlv.push(tImagePath);
		tlv.push(tDepth);
		logger.info("from command to tlv package:(tag=" + Command.SYNC_TASK_INFO_RESP + ", child=16, length="
				+ tlv.getLength() + ")");
		return tlv;
	}

	private TaskProExtInfo task;

	public TaskProExtInfo getTask()
	{
		return task;
	}

	public void setTask(TaskProExtInfo task)
	{
		this.task = task;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskInfoResp.class);

}
