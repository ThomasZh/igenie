package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncChildTaskReq
		extends ReqCommand
{
	public SyncChildTaskReq()
	{
		super();

		this.setTag(Command.SYNC_CHILD_TASK_REQ);
	}

	public SyncChildTaskReq(String id, short version)
	{
		this();

		this.projectId = id;
		this.version = version;
	}

	@Override
	public SyncChildTaskReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.SYNC_CHILD_TASK_REQ + ", child=3) to command");

		TlvParser.decodeChildren(tlv, 3);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tTaskId = tlv.getChild(1);
		projectId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("projectId: " + projectId);

		TlvObject tVersion = tlv.getChild(2);
		version = TlvByteUtil.byte2Int(tVersion.getValue());
		logger.debug("version: " + version);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tProjectId = new TlvObject(2, projectId);
		TlvObject tVersion = new TlvObject(3, 4, TlvByteUtil.int2Byte(version));

		TlvObject tlv = new TlvObject(Command.SYNC_CHILD_TASK_REQ);
		tlv.push(tSequence);
		tlv.push(tProjectId);
		tlv.push(tVersion);

		logger.info("from command to tlv package:(tag=" + Command.SYNC_CHILD_TASK_REQ + ", child=3, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String projectId;
	private int version;

	public String getProjectId()
	{
		return projectId;
	}

	public void setProjectId(String id)
	{
		this.projectId = id;
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncChildTaskReq.class);

}
