package com.oct.ga.comm.cmd.group;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class DndQueryReq
		extends ReqCommand
{
	public DndQueryReq()
	{
		super();

		this.setTag(Command.DND_QUERY_REQ);
	}

	public DndQueryReq(String groupId)
	{
		this();

		this.setGroupId(groupId);
	}

	public DndQueryReq(int sequence, String groupId)
	{
		this(groupId);

		this.setSequence(sequence);
	}

	@Override
	public DndQueryReq decode(TlvObject tlv)
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

		TlvObject tGroupId = tlv.getChild(i++);
		groupId = new String(tGroupId.getValue(), "UTF-8");
		logger.debug("groupId: " + groupId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tGroupId = new TlvObject(i++, groupId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tGroupId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String groupId;

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	private final static Logger logger = LoggerFactory.getLogger(DndQueryReq.class);

}
