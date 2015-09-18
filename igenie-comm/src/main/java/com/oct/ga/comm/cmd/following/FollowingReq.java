package com.oct.ga.comm.cmd.following;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvObjectExt;
import com.oct.ga.comm.tlv.TlvParser;

public class FollowingReq
		extends ReqCommand
{
	public FollowingReq()
	{
		super();

		this.setTag(Command.FOLLOWING_REQ);
	}

	public FollowingReq(String friendRegisterId)
	{
		this();

		this.friendRegisterId = friendRegisterId;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObjectExt root = new TlvObjectExt(Command.FOLLOWING_REQ);

		int timestamp = new Long(System.currentTimeMillis() / 1000).intValue();
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(timestamp));
		TlvObject tFriendRegisterId = new TlvObject(2, this.friendRegisterId);

		root.plus(tSequence).plus(tFriendRegisterId);

		return root;
	}

	@Override
	public FollowingReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.FOLLOWING_REQ + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tFriendRegisterId = tlv.getChild(1);
		friendRegisterId = new String(tFriendRegisterId.getValue(), "UTF-8");
		logger.info("friendRegisterId: " + friendRegisterId);

		return this;
	}

	/**
	 * email or telephone
	 */
	private String friendRegisterId;

	public String getFriendRegisterId()
	{
		return friendRegisterId;
	}

	public void setFriendRegisterId(String friendRegisterId)
	{
		this.friendRegisterId = friendRegisterId;
	}

	private final static Logger logger = LoggerFactory.getLogger(FollowingReq.class);

}
