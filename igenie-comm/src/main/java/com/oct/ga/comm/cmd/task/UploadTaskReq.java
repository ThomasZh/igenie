package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class UploadTaskReq
		extends ReqCommand
{
	public UploadTaskReq()
	{
		super();

		this.setTag(Command.UPLOAD_TASK_REQ);
	}

	public UploadTaskReq(TaskProExtInfo task)
	{
		this();

		this.setTask(task);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tVersion = new TlvObject(i++, TlvByteUtil.int2Byte(task.getVer()));
		TlvObject tTaskId = new TlvObject(i++, task.getId());
		TlvObject tTaskParentId = new TlvObject(i++, task.getPid());
		TlvObject tTaskName = new TlvObject(i++, task.getName());
		TlvObject tTaskDesc = new TlvObject(i++, task.getDesc());
		TlvObject tStartTime = new TlvObject(i++, TlvByteUtil.int2Byte(task.getStartTime()));
		TlvObject tEndTime = new TlvObject(i++, TlvByteUtil.int2Byte(task.getEndTime()));
		TlvObject tColor = new TlvObject(i++, TlvByteUtil.short2Byte(task.getColor()));
		TlvObject tExtAttribute = new TlvObject(i++, task.getExtAttribute());
		TlvObject tTemplateId = new TlvObject(i++, task.getTemplateId());
		TlvObject tLocDesc = new TlvObject(i++, task.getLocDesc());
		TlvObject tLocX = new TlvObject(i++, task.getLocX());
		TlvObject tLocY = new TlvObject(i++, task.getLocY());

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tVersion);
		tlv.push(tTaskId);
		tlv.push(tTaskParentId);
		tlv.push(tTaskName);
		tlv.push(tTaskDesc);
		tlv.push(tStartTime);
		tlv.push(tEndTime);
		tlv.push(tColor);
		tlv.push(tExtAttribute);
		tlv.push(tTemplateId);
		tlv.push(tLocDesc);
		tlv.push(tLocX);
		tlv.push(tLocY);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public UploadTaskReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 14;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tVersion = tlv.getChild(i++);
		int version = TlvByteUtil.byte2Int(tVersion.getValue());
		logger.debug("version: " + version);

		TlvObject tTaskId = tlv.getChild(i++);
		String taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tTaskParentId = tlv.getChild(i++);
		String taskParentId = new String(tTaskParentId.getValue(), "UTF-8");
		logger.debug("taskParentId: " + taskParentId);

		TlvObject tTaskName = tlv.getChild(i++);
		String taskName = new String(tTaskName.getValue(), "UTF-8");
		logger.debug("taskName: " + taskName);

		TlvObject tTaskDesc = tlv.getChild(i++);
		String taskDesc = new String(tTaskDesc.getValue(), "UTF-8");
		logger.debug("taskDesc: " + taskDesc);

		TlvObject tStartTime = tlv.getChild(i++);
		int startTime = TlvByteUtil.byte2Int(tStartTime.getValue());
		logger.debug("startTime: " + startTime);

		TlvObject tEndTime = tlv.getChild(i++);
		int endTime = TlvByteUtil.byte2Int(tEndTime.getValue());
		logger.debug("endTime: " + endTime);

		TlvObject tColor = tlv.getChild(i++);
		short color = TlvByteUtil.byte2Short(tColor.getValue());
		logger.debug("color: " + color);

		TlvObject tExtAttribute = tlv.getChild(i++);
		String extAttribute = new String(tExtAttribute.getValue(), "UTF-8");
		logger.debug("extAttribute: " + extAttribute);

		TlvObject tTemplateId = tlv.getChild(i++);
		String templateId = new String(tTemplateId.getValue(), "UTF-8");
		logger.debug("templateId: " + templateId);

		TlvObject tLocDesc = tlv.getChild(i++);
		String locDesc = new String(tLocDesc.getValue(), "UTF-8");
		logger.debug("locDesc: " + locDesc);

		TlvObject tLocX = tlv.getChild(i++);
		String locX = new String(tLocX.getValue(), "UTF-8");
		logger.debug("locX: " + locX);

		TlvObject tLocY = tlv.getChild(i++);
		String locY = new String(tLocY.getValue(), "UTF-8");
		logger.debug("locY: " + locY);

		task = new TaskProExtInfo();

		task.setVer(version);
		task.setId(taskId);
		task.setPid(taskParentId);
		task.setName(taskName);
		task.setDesc(taskDesc);
		task.setColor(color);
		task.setExtAttribute(extAttribute);
		task.setTemplateId(templateId);
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setLocDesc(locDesc);
		task.setLocX(locX);
		task.setLocY(locY);

		return this;
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

	private final static Logger logger = LoggerFactory.getLogger(UploadTaskReq.class);

}
