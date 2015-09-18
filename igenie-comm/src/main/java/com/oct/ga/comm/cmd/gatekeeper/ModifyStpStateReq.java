package com.oct.ga.comm.cmd.gatekeeper;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ModifyStpStateReq
		extends StpCommand
{
	public ModifyStpStateReq()
	{
		super();

		this.setTag(Command.GK_MODIFY_STP_STATE_REQ);
	}

	public ModifyStpStateReq(int sequence, String stpId, short state)
	{
		this();

		this.sequence = sequence;
		this.stpId = stpId;
		this.state = state;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tlv = new TlvObject(Command.GK_MODIFY_STP_STATE_REQ);

		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tStpId = new TlvObject(2, stpId);
		TlvObject tState = new TlvObject(3, 2, TlvByteUtil.short2Byte(state));

		tlv.push(tSequence);
		tlv.push(tStpId);
		tlv.push(tState);

		return tlv;
	}

	@Override
	public ModifyStpStateReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.GK_MODIFY_STP_STATE_REQ + ", child=3) to command");

		TlvParser.decodeChildren(tlv, 3);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tStpId = tlv.getChild(1);
		stpId = new String(tStpId.getValue(), "UTF-8");
		logger.debug("stpId: " + stpId);

		TlvObject tState = tlv.getChild(2);
		state = TlvByteUtil.byte2Short(tState.getValue());
		logger.debug("state: " + state);

		return this;
	}

	private String stpId;
	private short state;

	public String getStpId()
	{
		return stpId;
	}

	public void setStpId(String stpId)
	{
		this.stpId = stpId;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

	private final static Logger logger = LoggerFactory.getLogger(ModifyStpStateReq.class);

}
