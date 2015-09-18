package com.oct.ga.comm.cmd.appver;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class CheckVersionUpdateReq
		extends ReqCommand
{
	public CheckVersionUpdateReq()
	{
		super();

		this.setTag(Command.CHECK_VERSION_UPGRADE_REQ);
	}

	public CheckVersionUpdateReq(String clientVersion)
	{
		this();

		this.setClientVersion(clientVersion);
	}

	@Override
	public CheckVersionUpdateReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 2;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tClientVersion = tlv.getChild(i++);
		clientVersion = new String(tClientVersion.getValue(), "UTF-8");
		logger.debug("clientVersion: " + clientVersion);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tClientVersion = new TlvObject(i++, clientVersion);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tClientVersion);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String clientVersion;

	public String getClientVersion()
	{
		return clientVersion;
	}

	public void setClientVersion(String clientVersion)
	{
		this.clientVersion = clientVersion;
	}

	private final static Logger logger = LoggerFactory.getLogger(CheckVersionUpdateReq.class);

}
