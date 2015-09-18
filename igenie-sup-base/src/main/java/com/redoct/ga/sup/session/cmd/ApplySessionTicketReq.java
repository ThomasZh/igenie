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
import com.redoct.ga.sup.session.domain.StpSession;

public class ApplySessionTicketReq
		extends SupReqCommand
{
	public ApplySessionTicketReq()
	{
		super();

		this.setTag(SupCommandTag.APPLY_SESSION_TICKET_REQ);
	}

	public ApplySessionTicketReq(StpSession stpSession)
	{
		this();

		this.setStpSession(stpSession);
	}

	public ApplySessionTicketReq(long sequence, StpSession stpSession)
	{
		this(stpSession);

		this.setSequence(sequence);
	}

	@Override
	public ApplySessionTicketReq decode(TlvObject tlv)
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
			stpSession = gson.fromJson(json, StpSession.class);

			logger.debug("deviceId: " + stpSession.getDeviceId());
			logger.debug("deviceOsVersion: " + stpSession.getDeviceOsVersion());
			logger.debug("accountId: " + stpSession.getAccountId());
			logger.debug("gateToken: " + stpSession.getGateToken());
			logger.debug("notifyToken: " + stpSession.getNotifyToken());
			logger.debug("ioSessionId: " + stpSession.getIoSessionId());
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
		String json = gson.toJson(stpSession, StpSession.class);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
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

	private final static Logger logger = LoggerFactory.getLogger(ApplySessionTicketReq.class);

}
