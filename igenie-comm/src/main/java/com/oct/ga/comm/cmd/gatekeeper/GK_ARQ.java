package com.oct.ga.comm.cmd.gatekeeper;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

//deviceId,appId,vendorId
public class GK_ARQ
		extends ReqCommand
{
	public GK_ARQ()
	{
		super();

		this.setTag(Command.GK_ARQ);
	}

	@Override
	public TlvObject encode()
	{
		TlvObject tDeviceId = new TlvObject(1, this.getDeviceId());
		TlvObject tAppId = new TlvObject(2, this.getAppId());
		TlvObject tVendorId = new TlvObject(3, this.getVendorId());
		TlvObject tVersion = new TlvObject(4, this.getVersion());

		TlvObject tlv = new TlvObject(Command.GK_ARQ);
		tlv.push(tDeviceId);
		tlv.push(tAppId);
		tlv.push(tVendorId);
		tlv.push(tVersion);

		return tlv;
	}

	@Override
	public GK_ARQ decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.GK_ARQ + ", child=4) to command");

		TlvParser.decodeChildren(tlv, 4);

		TlvObject tDeviceId = tlv.getChild(0);
		deviceId = new String(tDeviceId.getValue(), "UTF-8");
		logger.debug("deviceId: " + deviceId);

		// tools: cebf05a7-94af-4a44-b684-ff800dd4513d
		// monitor: 7cfc6cbf-e127-45ef-8092-24d7a0ca9e89
		// iGenie: 8ed18148-5d33-43f1-90f1-98b8bcf323d7
		// tripc2c: eab5e3b7-6261-4464-8636-cb02645a559d
		TlvObject tAppId = tlv.getChild(1);
		appId = new String(tAppId.getValue(), "UTF-8");
		logger.debug("appId: " + appId);

		// 4k: 46098b55-27c7-45f3-afed-4794fb0ccd4d
		TlvObject tVendorId = tlv.getChild(2);
		vendorId = new String(tVendorId.getValue(), "UTF-8");
		logger.debug("vendorId: " + vendorId);

		TlvObject tVersion = tlv.getChild(3);
		version = new String(tVersion.getValue(), "UTF-8");
		logger.debug("version: " + version);

		return this;
	}

	private String deviceId;
	private String appId;
	private String vendorId;
	private String version;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	private final static Logger logger = LoggerFactory.getLogger(GK_ARQ.class);
}
