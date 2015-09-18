package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class UploadTaskNoteResp
		extends RespCommand
{
	public UploadTaskNoteResp()
	{
		this.setTag(Command.UPLOAD_TASK_NOTE_RESP);
	}

	public UploadTaskNoteResp(int sequence, short respState)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));

		TlvObject tlv = new TlvObject(Command.UPLOAD_TASK_NOTE_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);

		logger.info("from command to tlv package:(tag=" + Command.UPLOAD_TASK_NOTE_RESP + ", child=2, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	/**
	 * client use, to handle the server response
	 */
	@Override
	public StpCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.UPLOAD_TASK_NOTE_RESP + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tState = tlv.getChild(1);
		this.setRespState(TlvByteUtil.byte2Short(tState.getValue()));
		logger.debug("state: " + this.getRespState());

		return this;
	}

	private final static Logger logger = LoggerFactory.getLogger(UploadTaskNoteResp.class);

}
