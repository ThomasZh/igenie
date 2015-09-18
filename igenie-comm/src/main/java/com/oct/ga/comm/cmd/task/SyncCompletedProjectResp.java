package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.taskext.ProjectMaster;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncCompletedProjectResp
		extends RespCommand
{
	public SyncCompletedProjectResp()
	{
		this.setTag(Command.SYNC_PROJECT_COMPLETED_RESP);
	}

	public SyncCompletedProjectResp(int sequence, short state, List<ProjectMaster> projects, int timestamp)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(state);
		this.setProjects(projects);
		this.setTimestamp(timestamp);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tState = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		Gson gson = new Gson();
		String json = gson.toJson(projects);
		TlvObject tJson = new TlvObject(i++, json);
		TlvObject tCurrentTimestamp = new TlvObject(i++, 4, TlvByteUtil.int2Byte(timestamp));

		TlvObject tlv = new TlvObject(this.getTag());

		tlv.push(tSequence);
		tlv.push(tState);
		tlv.push(tJson);
		tlv.push(tCurrentTimestamp);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public SyncCompletedProjectResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tRespState = tlv.getChild(i++);
		short state = TlvByteUtil.byte2Short(tRespState.getValue());
		logger.debug("state: " + state);
		this.setRespState(state);

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			projects = gson.fromJson(json, new TypeToken<List<ProjectMaster>>()
			{
			}.getType());
		}

		TlvObject tTimestamp = tlv.getChild(i++);
		timestamp = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("timestamp: " + timestamp);

		return this;
	}

	private List<ProjectMaster> projects;
	private int timestamp;

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public List<ProjectMaster> getProjects()
	{
		return projects;
	}

	public void setProjects(List<ProjectMaster> projects)
	{
		this.projects = projects;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncCompletedProjectResp.class);

}
