package com.oct.ga.comm.cmd.group;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class DndSetReq
		extends ReqCommand
{
	public DndSetReq()
	{
		super();

		this.setTag(Command.DND_SET_REQ);
	}

	public DndSetReq(String groupId, short mode)
	{
		this();

		this.setGroupId(groupId);
		this.setMode(mode);
	}

	public DndSetReq(int sequence, String groupId, short mode)
	{
		this(groupId, mode);

		this.setSequence(sequence);
	}

	@Override
	public DndSetReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tGroupId = tlv.getChild(i++);
		groupId = new String(tGroupId.getValue(), "UTF-8");
		logger.debug("groupId: " + groupId);

		TlvObject tMode = tlv.getChild(i++);
		mode = TlvByteUtil.byte2Short(tMode.getValue());
		logger.debug("mode: " + mode);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tGroupId = new TlvObject(i++, groupId);
		TlvObject tMode = new TlvObject(i++, 2, TlvByteUtil.short2Byte(mode));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tGroupId);
		tlv.push(tMode);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String groupId;
	private short mode;

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public short getMode()
	{
		return mode;
	}

	public void setMode(short mode)
	{
		this.mode = mode;
	}

	private final static Logger logger = LoggerFactory.getLogger(DndSetReq.class);

}
