package com.redoct.ga.sup.session.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.session.domain.GateSession;

public class ApplyGateTokenReq
		extends SupReqCommand
{
	public ApplyGateTokenReq()
	{
		super();

		this.setTag(SupCommandTag.APPLY_GATE_TOKEN_REQ);
	}

	public ApplyGateTokenReq(GateSession gateSession)
	{
		this();

		this.setGateSession(gateSession);
	}

	public ApplyGateTokenReq(long sequence, GateSession gateSession)
	{
		this(gateSession);

		this.setSequence(sequence);
	}

	@Override
	public ApplyGateTokenReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			gateSession = gson.fromJson(json, GateSession.class);

			logger.debug("deviceId: " + gateSession.getDeviceId());
			logger.debug("stpId: " + gateSession.getStpId());
			logger.debug("stpIp: " + gateSession.getStpIp());
			logger.debug("stpPort: " + gateSession.getStpPort());
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		Gson gson = new Gson();
		String json = gson.toJson(gateSession, GateSession.class);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private GateSession gateSession;

	public GateSession getGateSession()
	{
		return gateSession;
	}

	public void setGateSession(GateSession gateSession)
	{
		this.gateSession = gateSession;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplyGateTokenReq.class);

}
