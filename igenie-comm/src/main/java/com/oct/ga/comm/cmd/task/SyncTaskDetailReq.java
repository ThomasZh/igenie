package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvObjectExt;
import com.oct.ga.comm.tlv.TlvParser;

// return TaskProDetailInfo & childTaskBasicInfo
public class SyncTaskDetailReq
		extends ReqCommand
{
	public SyncTaskDetailReq()
	{
		super();

		this.setTag(Command.SYNC_TASKPRO_DETAIL_REQ);
	}

	public SyncTaskDetailReq(String taskId, short version)
	{
		this();

		this.taskId = taskId;
		this.version = version;
	}

    @Override
    public TlvObject encode()
            throws UnsupportedEncodingException
    {
        TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
        TlvObject tTaskId = new TlvObject(2, taskId);
        TlvObject tVersion = new TlvObject(3, 4, TlvByteUtil.int2Byte(version));

        TlvObject tlv = new TlvObject(Command.SYNC_TASKPRO_DETAIL_REQ);
        tlv.push(tSequence);
        tlv.push(tTaskId);
        tlv.push(tVersion);

        logger.info("from command to tlv package:(tag=" + Command.SYNC_TASKPRO_DETAIL_REQ + ", child=3, length="
                + tlv.getLength() + ")");

        return tlv;
    }

	@Override
	public SyncTaskDetailReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.SYNC_TASKPRO_DETAIL_REQ + ", child=3) to command");

		TlvParser.decodeChildren(tlv, 3);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tTaskId = tlv.getChild(1);
		taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tVersion = tlv.getChild(2);
		version = TlvByteUtil.byte2Int(tVersion.getValue());
		logger.debug("version: " + version);

		return this;
	}


	private String taskId;
	private int version;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskDetailReq.class);

}
