package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncChildTaskResp
		extends RespCommand
{
	public SyncChildTaskResp()
	{
		this.setTag(Command.SYNC_CHILD_TASK_RESP);
	}

	public SyncChildTaskResp(short state, String pid, int version, String json)
	{
		this();

		this.setRespState(state);
		this.setPid(pid);
		this.setMaxVersion(version);
		this.setJson(json);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tState = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tPid = new TlvObject(4, pid);
		TlvObject tMaxVersion = new TlvObject(3, 4, TlvByteUtil.int2Byte(maxVersion));
		TlvObject tJson = new TlvObject(5, json);

		TlvObject tlv = new TlvObject(Command.SYNC_CHILD_TASK_RESP);

		tlv.push(tSequence);
		tlv.push(tState);
		tlv.push(tPid);
		tlv.push(tMaxVersion);
		tlv.push(tJson);

		logger.info("from command to tlv package:(tag=" + Command.SYNC_CHILD_TASK_RESP + ", child=5, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public SyncChildTaskResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		TlvParser.decodeChildren(tlv, 5);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tRespState = tlv.getChild(1);
		short state = TlvByteUtil.byte2Short(tRespState.getValue());
		logger.debug("state: " + state);
		this.setRespState(state);

        TlvObject tPid = tlv.getChild(2);
        this.setPid(new String(tPid.getValue(),"UTF-8"));

		TlvObject tMaxVersion = tlv.getChild(3);
		maxVersion = TlvByteUtil.byte2Int(tMaxVersion.getValue());
		logger.debug("maxVersion: " + maxVersion);

		TlvObject tJson = tlv.getChild(4);
		this.setJson(new String(tJson.getValue(), "UTF-8"));

		return this;
	}

	private String pid;
	private String json;
	private int maxVersion;

	public String getJson()
	{
		return json;
	}

	public void setJson(String json)
	{
		this.json = json;
	}

	public int getMaxVersion()
	{
		return maxVersion;
	}

	public void setMaxVersion(int maxVersion)
	{
		this.maxVersion = maxVersion;
	}

	public String getPid()
	{
		return pid;
	}

	public void setPid(String pid)
	{
		this.pid = pid;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncChildTaskResp.class);

}
