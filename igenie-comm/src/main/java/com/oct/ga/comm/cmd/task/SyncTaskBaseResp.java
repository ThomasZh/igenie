package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncTaskBaseResp
		extends RespCommand
{
	public SyncTaskBaseResp()
	{
		this.setTag(Command.SYNC_TASKPRO_BASE_RESP);
	}

	public SyncTaskBaseResp(String json)
	{
		this();

		this.setJson(json);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
        TlvObject tlv = new TlvObject(Command.SYNC_TASKPRO_BASE_RESP);

        TlvObject tJson = new TlvObject(1, this.getJson());
        tlv.push(tJson);

		logger.info("from command to tlv package:(tag=" + Command.SYNC_TASKPRO_BASE_RESP + ", child=1, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

    @Override
    public SyncTaskBaseResp decode(TlvObject tlv) throws UnsupportedEncodingException{
        TlvParser.decodeChildren(tlv, 1);

        TlvObject tJson = tlv.getChild(0);
        this.setJson(new String(tJson.getValue(), "UTF-8"));

        return this;
    }


    private String json;

	public String getJson()
	{
		return json;
	}

	public void setJson(String json)
	{
		this.json = json;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskBaseResp.class);

}
