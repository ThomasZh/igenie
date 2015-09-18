package com.oct.ga.comm.cmd.invite;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class InviteReq
		extends ReqCommand
{
	public InviteReq()
	{
		super();

		this.setTag(Command.INVITE_REQ);
	}

	public InviteReq(short inviteType, String loginName, short channelType, String channelId)
	{
		this();

		this.setInviteType(inviteType);
		this.setToUserSemiId(loginName);
		this.setChannelType(channelType);
		this.setChannelId(channelId);
	}

	public InviteReq(int sequence, short inviteType, String loginName, short channelType, String channelId)
	{
		this(inviteType, loginName, channelType, channelId);

		this.setSequence(sequence);
	}

	@Override
	public InviteReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 5;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tInviteType = tlv.getChild(i++);
		inviteType = TlvByteUtil.byte2Short(tInviteType.getValue());
		logger.debug("inviteType: " + inviteType);

		TlvObject tToUserSemiId = tlv.getChild(i++);
		toUserSemiId = new String(tToUserSemiId.getValue(), "UTF-8");
		logger.debug("toUserSemiId: " + toUserSemiId);

		TlvObject tChannelType = tlv.getChild(i++);
		channelType = TlvByteUtil.byte2Short(tChannelType.getValue());
		logger.debug("channelType: " + channelType);

		TlvObject tChannelId = tlv.getChild(i++);
		channelId = new String(tChannelId.getValue(), "UTF-8");
		logger.debug("channelId: " + channelId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tInviteType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(inviteType));
		TlvObject tToUserSemiId = new TlvObject(i++, toUserSemiId);
		TlvObject tChannelType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(channelType));
		TlvObject tChannelId = new TlvObject(i++, channelId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tInviteType);
		tlv.push(tToUserSemiId);
		tlv.push(tChannelType);
		tlv.push(tChannelId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private short inviteType;
	private String toUserSemiId;
	private short channelType;
	private String channelId;

	public short getInviteType()
	{
		return inviteType;
	}

	public void setInviteType(short inviteType)
	{
		this.inviteType = inviteType;
	}

	public String getToUserSemiId()
	{
		return toUserSemiId;
	}

	public void setToUserSemiId(String toUserSemiId)
	{
		this.toUserSemiId = toUserSemiId;
	}

	public short getChannelType()
	{
		return channelType;
	}

	public void setChannelType(short channelType)
	{
		this.channelType = channelType;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	private final static Logger logger = LoggerFactory.getLogger(InviteReq.class);
}
