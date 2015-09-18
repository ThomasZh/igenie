package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.task.TaskNote;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class UploadTaskNoteReq
		extends ReqCommand
{
	public UploadTaskNoteReq()
	{
		super();

		this.setTag(Command.UPLOAD_TASK_NOTE_REQ);
	}

	public UploadTaskNoteReq(TaskNote note)
	{
		this();

		this.setNote(note);
	}

	@Override
	public UploadTaskNoteReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.UPLOAD_TASK_NOTE_REQ + ", child=4) to command");

		TlvParser.decodeChildren(tlv, 4);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tNoteId = tlv.getChild(1);
		String noteId = new String(tNoteId.getValue(), "UTF-8");
		logger.debug("noteId: " + noteId);

		TlvObject tTaskId = tlv.getChild(2);
		String taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tTxt = tlv.getChild(3);
		String txt = new String(tTxt.getValue(), "UTF-8");
		logger.debug("txt: " + txt);

		note = new TaskNote(noteId, taskId, txt);

		return this;
	}

	/**
	 * just implement to unit test
	 */
	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject noteId = new TlvObject(2, note.getNoteId());
		TlvObject taskId = new TlvObject(3, note.getTaskId());
		TlvObject txt = new TlvObject(4, note.getTxt());

		TlvObject tlv = new TlvObject(Command.UPLOAD_TASK_NOTE_REQ);
		tlv.push(tSequence);
		tlv.push(noteId);
		tlv.push(taskId);
		tlv.push(txt);

		logger.info("from command to tlv package:(tag=" + Command.UPLOAD_TASK_NOTE_REQ + ", child=4, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private TaskNote note;

	public TaskNote getNote()
	{
		return note;
	}

	public void setNote(TaskNote note)
	{
		this.note = note;
	}

	private final static Logger logger = LoggerFactory.getLogger(UploadTaskNoteReq.class);
}
