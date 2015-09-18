package com.oct.ga.comm.cmd.inlinecast;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class InlinecastActivityJoinReq
		extends ReqCommand
{
	public InlinecastActivityJoinReq()
	{
		super();

		this.setTag(Command.INLINECAST_ACTIVITY_JOIN_REQ);
	}

	public InlinecastActivityJoinReq(long ioSessionId, String groupName, String leaderId, String fromAccountName,
			int timestamp)
	{
		this();

		this.setIoSessionId(ioSessionId);
		this.setGroupName(groupName);
		this.setLeaderId(leaderId);
		this.setFromAccountName(fromAccountName);
		this.setTimestamp(timestamp);
	}

	@Override
	public InlinecastActivityJoinReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 6;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tIoSessionId = tlv.getChild(i++);
		ioSessionId = TlvByteUtil.byte2Long(tIoSessionId.getValue());
		logger.debug("ioSessionId: " + ioSessionId);

		TlvObject tGroupName = tlv.getChild(i++);
		groupName = new String(tGroupName.getValue(), "UTF-8");
		logger.debug("groupName: " + groupName);

		TlvObject tLeaderId = tlv.getChild(i++);
		leaderId = new String(tLeaderId.getValue(), "UTF-8");
		logger.debug("leaderId: " + leaderId);

		TlvObject tFromAccountName = tlv.getChild(i++);
		fromAccountName = new String(tFromAccountName.getValue(), "UTF-8");
		logger.debug("fromAccountName: " + fromAccountName);

		TlvObject tTimestamp = tlv.getChild(i++);
		timestamp = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("timestamp: " + timestamp);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tIoSessionId = new TlvObject(i++, 8, TlvByteUtil.long2Byte(ioSessionId));
		TlvObject tGroupName = new TlvObject(i++, groupName);
		TlvObject tLeaderId = new TlvObject(i++, leaderId);
		TlvObject tFromAccountName = new TlvObject(i++, fromAccountName);
		TlvObject tTimestamp = new TlvObject(i++, 4, TlvByteUtil.int2Byte(timestamp));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tIoSessionId);
		tlv.push(tGroupName);
		tlv.push(tLeaderId);
		tlv.push(tFromAccountName);
		tlv.push(tTimestamp);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private long ioSessionId;
	private String groupName;
	private String leaderId;
	private String fromAccountName;
	private int timestamp;

	public long getIoSessionId()
	{
		return ioSessionId;
	}

	public void setIoSessionId(long ioSessionId)
	{
		this.ioSessionId = ioSessionId;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getLeaderId()
	{
		return leaderId;
	}

	public void setLeaderId(String leaderId)
	{
		this.leaderId = leaderId;
	}

	public String getFromAccountName()
	{
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName)
	{
		this.fromAccountName = fromAccountName;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	private final static Logger logger = LoggerFactory.getLogger(InlinecastActivityJoinReq.class);

}
