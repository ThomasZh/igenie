package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ClubSubscribersAddReq;
import com.oct.ga.comm.cmd.club.ClubSubscribersAddResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaClubService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ClubSubscribersAddAdapter
		extends StpReqCommand
{
	public ClubSubscribersAddAdapter()
	{
		super();

		this.setTag(Command.CLUB_SUBSCRIBER_ADD_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ClubSubscribersAddReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ClubSubscribersAddResp respCmd = null;
		String clubId = reqCmd.getClubId();
		String[] userIds = reqCmd.getUserIds();

		try {
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");
			//GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");

			if (userIds != null & userIds.length > 0)
				for (String userId : userIds) {
					logger.debug("userId: " + userId);

					if (clubService.isExistSubscriber(clubId, userId)) {
						if (!clubService.isSubscriber(clubId, userId))
							clubService.updateSubscriberState(clubId, userId, GlobalArgs.INVITE_STATE_APPLY,
									currentTimestamp);
					} else {
						clubService.addSubscriber(clubId, userId, GlobalArgs.INVITE_STATE_APPLY, currentTimestamp);

						//followingService.follow(this.getMyAccountId(), userId, currentTimestamp);
					}
				}

			int num = clubService.querySubscriberNum(clubId);
			clubService.updateSubscriberNum(clubId, num, currentTimestamp);

			respCmd = new ClubSubscribersAddResp(ErrorCode.SUCCESS, clubId);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ClubSubscribersAddResp(ErrorCode.UNKNOWN_FAILURE, clubId);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ClubSubscribersAddReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ClubSubscribersAddAdapter.class);

}
