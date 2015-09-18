package com.redoct.ga.sup.session.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.session.domain.GateSession;
import com.redoct.ga.sup.session.domain.StpSession;

public class QueryGateSessionResp
		extends SupRespCommand
{
	public QueryGateSessionResp()
	{
		this.setTag(SupCommandTag.QUERY_GATE_SESSION_RESP);
	}

	public QueryGateSessionResp(long sequence, short respState)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
	}

	public QueryGateSessionResp(long sequence, short respState, GateSession gateSession)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setGateSession(gateSession);
	}

	@Override
	public QueryGateSessionResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));

		TlvObject tResultFlag = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			gateSession = gson.fromJson(json, GateSession.class);

			logger.debug("deviceId: " + gateSession.getDeviceId());
			logger.debug("stpId: " + gateSession.getStpId());
			logger.debug("gateToken: " + gateSession.getGateToken());
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
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		Gson gson = new Gson();
		String json = gson.toJson(gateSession, GateSession.class);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
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

	public void setGateSession(GateSession stpSession)
	{
		this.gateSession = stpSession;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryGateSessionResp.class);

}
