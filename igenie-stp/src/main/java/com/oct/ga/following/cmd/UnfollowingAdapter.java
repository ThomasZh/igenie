package com.oct.ga.following.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.following.UnfollowingReq;
import com.oct.ga.comm.cmd.following.UnfollowingResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class UnfollowingAdapter
		extends StpReqCommand
{
	public UnfollowingAdapter()
	{
		super();

		this.setTag(Command.UNFOLLOW_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new UnfollowingReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		UnfollowingResp respCmd = null;
		String myUserId = this.getMyAccountId();
		String friendId = reqCmd.getFriendId();

		try {
			GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");

			followingService.unfollow(myUserId, friendId, currentTimestamp);

			respCmd = new UnfollowingResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new UnfollowingResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}

	}

	private UnfollowingReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(UnfollowingAdapter.class);
}
