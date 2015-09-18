package com.redoct.ga.sup.message.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class MultcastActivityJoinReq
		extends SupReqCommand
{
	public MultcastActivityJoinReq()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_ACTIVITY_JOIN_REQ);
	}

	public MultcastActivityJoinReq(String groupName, String leaderId, String fromAccountName, int timestamp)
	{
		this();

		this.setGroupName(groupName);
		this.setLeaderId(leaderId);
		this.setFromAccountName(fromAccountName);
		this.setTimestamp(timestamp);
	}

	@Override
	public MultcastActivityJoinReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 5;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

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

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tGroupName = new TlvObject(i++, groupName);
		TlvObject tLeaderId = new TlvObject(i++, leaderId);
		TlvObject tFromAccountName = new TlvObject(i++, fromAccountName);
		TlvObject tTimestamp = new TlvObject(i++, 4, TlvByteUtil.int2Byte(timestamp));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tGroupName);
		tlv.push(tLeaderId);
		tlv.push(tFromAccountName);
		tlv.push(tTimestamp);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String groupName;
	private String leaderId;
	private String fromAccountName;
	private int timestamp;

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

	private final static Logger logger = LoggerFactory.getLogger(MultcastActivityJoinReq.class);
}
