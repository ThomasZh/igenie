package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class UploadTaskResp
		extends RespCommand
{
	public UploadTaskResp()
	{
		this.setTag(Command.UPLOAD_TASK_RESP);
	}

	public UploadTaskResp(short respState, String taskId, int infoVer)
	{
		this();

		this.setRespState(respState);
		this.setTaskId(taskId);
		this.setInfoVer(infoVer);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		logger.debug("taskId: " + taskId);
		TlvObject tTaskId = new TlvObject(3, this.getTaskId());
		logger.debug("version: " + infoVer);
		TlvObject tVersion = new TlvObject(4, 4, TlvByteUtil.int2Byte(infoVer));

		TlvObject tlv = new TlvObject(Command.UPLOAD_TASK_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tTaskId);
		tlv.push(tVersion);

		logger.info("from command to tlv package:(tag=" + Command.UPLOAD_TASK_RESP + ", child=4, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public UploadTaskResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		TlvParser.decodeChildren(tlv, 4);

		TlvObject tSequence = tlv.getChild(0);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));

		TlvObject tResultFlag = tlv.getChild(1);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		TlvObject tTaskId = tlv.getChild(2);
		taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tVersion = tlv.getChild(3);
		infoVer = TlvByteUtil.byte2Int(tVersion.getValue());
		logger.debug("version: " + infoVer);

		return this;
	}

	private String taskId;
	private int infoVer;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public int getInfoVer()
	{
		return infoVer;
	}

	public void setInfoVer(int infoVer)
	{
		this.infoVer = infoVer;
	}

	private final static Logger logger = LoggerFactory.getLogger(UploadTaskResp.class);

}
