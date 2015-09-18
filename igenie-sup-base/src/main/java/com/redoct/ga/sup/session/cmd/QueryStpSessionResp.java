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
import com.redoct.ga.sup.session.domain.StpSession;

public class QueryStpSessionResp
		extends SupRespCommand
{
	public QueryStpSessionResp()
	{
		this.setTag(SupCommandTag.QUERY_STP_SESSION_RESP);
	}

	public QueryStpSessionResp(long sequence, short respState)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
	}

	public QueryStpSessionResp(long sequence, short respState, StpSession stpSession)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setStpSession(stpSession);
	}

	@Override
	public QueryStpSessionResp decode(TlvObject tlv)
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
			stpSession = gson.fromJson(json, StpSession.class);

			logger.debug("deviceId: " + stpSession.getDeviceId());
			logger.debug("deviceOsVersion: " + stpSession.getDeviceOsVersion());
			logger.debug("accountId: " + stpSession.getAccountId());
			logger.debug("gateToken: " + stpSession.getGateToken());
			logger.debug("notifyToken: " + stpSession.getNotifyToken());
			logger.debug("sessionId: " + stpSession.getIoSessionId());
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
		TlvObject tJson = null;
		if (stpSession == null) {
			tJson = new TlvObject(i++, "");
		} else {
			Gson gson = new Gson();
			String json = gson.toJson(stpSession, StpSession.class);
			tJson = new TlvObject(i++, json);
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private StpSession stpSession;

	public StpSession getStpSession()
	{
		return stpSession;
	}

	public void setStpSession(StpSession stpSession)
	{
		this.stpSession = stpSession;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryStpSessionResp.class);

}
