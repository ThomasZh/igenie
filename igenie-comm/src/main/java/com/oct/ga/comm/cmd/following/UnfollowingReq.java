package com.oct.ga.comm.cmd.following;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class UnfollowingReq
		extends ReqCommand
{
	public UnfollowingReq()
	{
		super();

		this.setTag(Command.UNFOLLOW_REQ);
	}

	public UnfollowingReq(String friendId)
	{
		this();

		this.setFriendId(friendId);
	}

	public UnfollowingReq(int sequence, String friendId)
	{
		this(friendId);

		this.setSequence(sequence);
	}

	@Override
	public UnfollowingReq decode(TlvObject tlv)
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

		TlvObject tFriendId = tlv.getChild(i++);
		friendId = new String(tFriendId.getValue(), "UTF-8");
		logger.info("friendId: " + friendId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tFriendId = new TlvObject(i++, friendId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tFriendId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String friendId;

	public String getFriendId()
	{
		return friendId;
	}

	public void setFriendId(String friendId)
	{
		this.friendId = friendId;
	}

	private final static Logger logger = LoggerFactory.getLogger(UnfollowingReq.class);
}
