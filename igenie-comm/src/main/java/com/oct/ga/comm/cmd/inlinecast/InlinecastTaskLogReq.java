package com.oct.ga.comm.cmd.inlinecast;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class InlinecastTaskLogReq
		extends ReqCommand
{
	public InlinecastTaskLogReq()
	{
		this.setTag(Command.INLINECAST_TASK_LOG_REQ);
	}

	public InlinecastTaskLogReq(long ioSessionId, MsgFlowBasicInfo notify)
	{
		this();

		this.setIoSessionId(ioSessionId);
		this.setNotify(notify);
	}

	@Override
	public InlinecastTaskLogReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tIoSessionId = tlv.getChild(i++);
		ioSessionId = TlvByteUtil.byte2Long(tIoSessionId.getValue());
		logger.debug("ioSessionId: " + ioSessionId);

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			notify = gson.fromJson(json, MsgFlowBasicInfo.class);
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tIoSessionId = new TlvObject(i++, 8, TlvByteUtil.long2Byte(ioSessionId));
		Gson gson = new Gson();
		String json = gson.toJson(notify);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tIoSessionId);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private long ioSessionId;
	private MsgFlowBasicInfo notify;

	public MsgFlowBasicInfo getNotify()
	{
		return notify;
	}

	public void setNotify(MsgFlowBasicInfo notify)
	{
		this.notify = notify;
	}

	public long getIoSessionId()
	{
		return ioSessionId;
	}

	public void setIoSessionId(long ioSessionId)
	{
		this.ioSessionId = ioSessionId;
	}

	private final static Logger logger = LoggerFactory.getLogger(InlinecastTaskLogReq.class);

}
