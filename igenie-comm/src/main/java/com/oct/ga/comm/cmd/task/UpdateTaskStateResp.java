package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class UpdateTaskStateResp extends RespCommand
{
	public UpdateTaskStateResp()
	{
		this.setTag(Command.UPDATE_TASK_STATE_RESP);
	}

	public UpdateTaskStateResp(short respState)
	{
		this();

		this.setRespState(respState);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));

		TlvObject tlv = new TlvObject(Command.UPDATE_TASK_STATE_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);

		logger.info("from command to tlv package:(tag=" + Command.UPDATE_TASK_STATE_RESP + ", child=2, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

    @Override
    public UpdateTaskStateResp decode(TlvObject tlv) throws UnsupportedEncodingException{
        TlvParser.decodeChildren(tlv, 2);

        TlvObject tSequence = tlv.getChild(0);
        this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
        TlvObject tResultFlag = tlv.getChild(1);
        this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

        return this;
    }


    private final static Logger logger = LoggerFactory.getLogger(UpdateTaskStateResp.class);

}
