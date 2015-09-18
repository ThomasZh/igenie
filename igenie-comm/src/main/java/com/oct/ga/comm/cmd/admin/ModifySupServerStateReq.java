package com.oct.ga.comm.cmd.admin;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ModifySupServerStateReq
		extends ReqCommand
{
	public ModifySupServerStateReq()
	{
		super();

		this.setTag(Command.MODIFY_SUP_STATE_REQ);
	}

	public ModifySupServerStateReq(String supId, short state)
	{
		this();

		this.setSupId(supId);
		this.setState(state);
	}

	public ModifySupServerStateReq(int sequence, String supId, short state)
	{
		this(supId, state);

		this.setSequence(sequence);
	}

	@Override
	public ModifySupServerStateReq decode(TlvObject tlv)
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

		TlvObject tSupId = tlv.getChild(i++);
		supId = new String(tSupId.getValue(), "UTF-8");
		logger.debug("supId: " + supId);

		TlvObject tState = tlv.getChild(i++);
		state = TlvByteUtil.byte2Short(tState.getValue());
		logger.debug("state: " + state);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tSupId = new TlvObject(i++, supId);
		TlvObject tState = new TlvObject(i++, 2, TlvByteUtil.short2Byte(state));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tSupId);
		tlv.push(tState);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String supId;
	private short state;

	public String getSupId()
	{
		return supId;
	}

	public void setSupId(String supId)
	{
		this.supId = supId;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

	private final static Logger logger = LoggerFactory.getLogger(ModifySupServerStateReq.class);
}
