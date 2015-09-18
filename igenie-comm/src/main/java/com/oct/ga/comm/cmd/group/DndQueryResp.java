package com.oct.ga.comm.cmd.group;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class DndQueryResp
		extends RespCommand
{
	public DndQueryResp()
	{
		this.setTag(Command.DND_SET_RESP);
	}

	public DndQueryResp(int sequence, short respState, short mode)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setMode(mode);
	}

	@Override
	public DndQueryResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tState.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tMode = tlv.getChild(i++);
		mode = TlvByteUtil.byte2Short(tMode.getValue());
		logger.debug("mode: " + mode);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tMode = new TlvObject(i++, 2, TlvByteUtil.short2Byte(mode));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tMode);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private short mode;

	public short getMode()
	{
		return mode;
	}

	public void setMode(short mode)
	{
		this.mode = mode;
	}

	private final static Logger logger = LoggerFactory.getLogger(DndQueryResp.class);

}
