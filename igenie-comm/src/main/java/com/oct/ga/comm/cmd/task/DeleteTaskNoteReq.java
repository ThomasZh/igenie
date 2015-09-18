package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class DeleteTaskNoteReq
		extends ReqCommand
{
	public DeleteTaskNoteReq()
	{
		super();

		this.setTag(Command.DELETE_TASK_NOTE_REQ);
	}

	public DeleteTaskNoteReq(String taskId, String noteId)
	{
		this();

		this.setTaskId(taskId);
		this.setNoteId(noteId);
	}

	public DeleteTaskNoteReq(int sequence, String taskId, String noteId)
	{
		this(taskId, noteId);

		this.setSequence(sequence);
	}

	@Override
	public DeleteTaskNoteReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tTaskId = tlv.getChild(i++);
		taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tNoteId = tlv.getChild(i++);
		noteId = new String(tNoteId.getValue(), "UTF-8");
		logger.debug("noteId: " + noteId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tTaskId = new TlvObject(i++, taskId);
		TlvObject tNoteId = new TlvObject(i++, noteId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tTaskId);
		tlv.push(tNoteId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String taskId;
	private String noteId;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getNoteId()
	{
		return noteId;
	}

	public void setNoteId(String noteId)
	{
		this.noteId = noteId;
	}

	private final static Logger logger = LoggerFactory.getLogger(DeleteTaskNoteReq.class);

}
