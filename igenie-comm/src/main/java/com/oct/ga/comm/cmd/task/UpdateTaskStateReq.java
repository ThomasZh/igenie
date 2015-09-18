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

public class UpdateTaskStateReq extends ReqCommand
{
	public UpdateTaskStateReq()
	{
		super();

		this.setTag(Command.UPDATE_TASK_STATE_REQ);
	}

	public UpdateTaskStateReq(String taskId, short state)
	{
		this();

		this.setTaskId(taskId);
		this.setState(state);
	}

    @Override
    public TlvObject encode() throws UnsupportedEncodingException{
        TlvObjectExt root = new TlvObjectExt(Command.UPDATE_TASK_STATE_REQ);

        TlvObject tSequence = new TlvObject(1, TlvByteUtil.int2Byte(this.sequence));
        TlvObject tTaskId = new TlvObject(2, this.getTaskId());
        TlvObject tState = new TlvObject(3, TlvByteUtil.short2Byte(this.state));

        root.plus(tSequence).plus(tTaskId).plus(tState);

        return root;
    }


    @Override
	public UpdateTaskStateReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.UPDATE_TASK_STATE_REQ + ", child=3) to command");

		TlvParser.decodeChildren(tlv, 3);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);
		
		TlvObject tTaskId = tlv.getChild(1);
		taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tState = tlv.getChild(2);
		state = TlvByteUtil.byte2Short(tState.getValue());
		logger.debug("state: " + state);

		return this;
	}

	private String taskId;
	private short state;

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

	private final static Logger logger = LoggerFactory.getLogger(UpdateTaskStateReq.class);

}
