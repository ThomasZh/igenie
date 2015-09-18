package com.oct.ga.monitor.agent.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.oct.ga.monitor.agent.cmd.MonitorContextResp;
import com.oct.ga.stp.cmd.StpReqCommand;

public class MonitorContextResp
		extends StpReqCommand
{
	public MonitorContextResp()
	{
		this.setTag(Command.MONITOR_CONTEXT_RESP);
	}

	public MonitorContextResp(boolean isEof, String context)
	{
		this();

		this.setEof(isEof);
		this.setContext(context);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		short eof = this.isEof() ? (short) 0 : (short) 1;
		TlvObject tEof = new TlvObject(1, 2, TlvByteUtil.short2Byte(eof));
		TlvObject tContext = new TlvObject(2, this.getContext());

		// 6 + 2 + 6 + context
		int pkgLen = 14 + tContext.getLength();
		logger.info("from command to tlv package:(tag=" + Command.MONITOR_CONTEXT_RESP + ", child=2, length=" + pkgLen
				+ ")");
		TlvObject tlv = new TlvObject(Command.MONITOR_CONTEXT_RESP, pkgLen);
		tlv.add(tEof);
		tlv.add(tContext);
		return tlv;
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.MONITOR_CONTEXT_RESP + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tEof = tlv.getChild(0);
		short eof = TlvByteUtil.byte2Short(tEof.getValue());
		logger.debug("eof: " + eof);

		TlvObject tContext = tlv.getChild(1);
		String context = new String(tContext.getValue(), "UTF-8");
		logger.debug("context: " + context);

		this.setEof(eof == 0 ? true : false);
		this.setContext(context);

		return this;
	}

	private String context;
	private boolean isEof;

	public String getContext()
	{
		return context;
	}

	public void setContext(String context)
	{
		this.context = context;
	}

	public boolean isEof()
	{
		return isEof;
	}

	public void setEof(boolean isEof)
	{
		this.isEof = isEof;
	}

	private final static Logger logger = LoggerFactory.getLogger(MonitorContextResp.class);
}
