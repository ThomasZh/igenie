package com.oct.ga.comm.cmd.apply;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ApplyStateNotify
		extends RespCommand
{
	public ApplyStateNotify()
	{
		this.setTag(Command.APPLY_STATE_NOTI);
	}

	public ApplyStateNotify(GaApplyStateNotify notify)
	{
		this();

		this.setNotify(notify);
	}

	@Override
	public ApplyStateNotify decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 1;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tNotify = tlv.getChild(i++);
		String jsonNotify = new String(tNotify.getValue(), "UTF-8");
		logger.debug("notify json: " + jsonNotify);
		Gson gson = new Gson();
		if (jsonNotify != null && jsonNotify.length() > 0) {
			notify = gson.fromJson(jsonNotify, GaApplyStateNotify.class);
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		Gson gson = new Gson();
		TlvObject tNotify = null;
		if (notify != null) {
			String jsonNotify = gson.toJson(notify);
			logger.debug("notifies json: " + jsonNotify);
			tNotify = new TlvObject(i++, jsonNotify);
		} else {
			tNotify = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tNotify);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private GaApplyStateNotify notify;

	public GaApplyStateNotify getNotify()
	{
		return notify;
	}

	public void setNotify(GaApplyStateNotify notify)
	{
		this.notify = notify;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplyStateNotify.class);

}
