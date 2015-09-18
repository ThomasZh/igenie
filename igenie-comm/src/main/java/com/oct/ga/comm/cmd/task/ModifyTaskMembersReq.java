package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ModifyTaskMembersReq
		extends ReqCommand
{
	public ModifyTaskMembersReq()
	{
		super();

		this.setTag(Command.TASKPRO_MODIFY_MEMBERS_REQ);
	}

	public ModifyTaskMembersReq(String taskId, String[] addIds, String[] removeIds)
	{
		this();

		this.taskId = taskId;
		this.setAddUserIds(addIds);
		this.setRemoveUserIds(removeIds);
	}

	@Override
	public ModifyTaskMembersReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tTaskId = tlv.getChild(i++);
		taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tAddIds = tlv.getChild(i++);
		String addIdsJson = new String(tAddIds.getValue(), "UTF-8");
		logger.debug("addIdsJson: " + addIdsJson);
		if (addIdsJson != null) {
			Gson gson = new Gson();
			addUserIds = gson.fromJson(addIdsJson, String[].class);
		}

		TlvObject tRemoveIds = tlv.getChild(i++);
		String removeIdsJson = new String(tRemoveIds.getValue(), "UTF-8");
		logger.debug("removeIdsJson: " + removeIdsJson);
		if (removeIdsJson != null) {
			Gson gson = new Gson();
			removeUserIds = gson.fromJson(removeIdsJson, String[].class);
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tTaskId = new TlvObject(i++, taskId);

		Gson gson = new Gson();
		TlvObject tAddIds = null;
		String addIdsJson = gson.toJson(addUserIds);
		if (addUserIds != null && addUserIds.length > 0) {
			tAddIds = new TlvObject(i++, addIdsJson);
		} else {
			tAddIds = new TlvObject(i++, "");
		}

		TlvObject tRemoveIds = null;
		String removeIdsJson = gson.toJson(removeUserIds);
		if (removeUserIds != null && removeUserIds.length > 0) {
			tRemoveIds = new TlvObject(i++, removeIdsJson);
		} else {
			tRemoveIds = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tTaskId);
		tlv.push(tAddIds);
		tlv.push(tRemoveIds);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String taskId;
	private String[] addUserIds;
	private String[] removeUserIds;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String[] getAddUserIds()
	{
		return addUserIds;
	}

	public void setAddUserIds(String[] addUserIds)
	{
		this.addUserIds = addUserIds;
	}

	public String[] getRemoveUserIds()
	{
		return removeUserIds;
	}

	public void setRemoveUserIds(String[] removeUserIds)
	{
		this.removeUserIds = removeUserIds;
	}

	private final static Logger logger = LoggerFactory.getLogger(ModifyTaskMembersReq.class);

}
