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
import com.oct.ga.comm.cmd.club.ClubSubscribersRemoveReq;
import com.oct.ga.comm.cmd.club.ClubSubscribersRemoveResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaClubService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ClubSubscribersRemoveAdapter
		extends StpReqCommand
{
	public ClubSubscribersRemoveAdapter()
	{
		super();

		this.setTag(Command.CLUB_SUBSCRIBER_REMOVE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ClubSubscribersRemoveReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ClubSubscribersRemoveResp respCmd = null;
		String clubId = reqCmd.getClubId();
		String[] userIds = reqCmd.getUserIds();

		try {
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");

			if (userIds != null & userIds.length > 0) {
				for (String userId : userIds) {
					logger.debug("userId: " + userId);

					if (clubService.isSubscriber(clubId, userId))
						clubService.kickoutSubscriber(clubId, userId, currentTimestamp);

					if (groupService.isMember(clubId, userId))
						groupService.kickout(clubId, userId, currentTimestamp, this.getMyAccountId());
				}

				syncVerService.increase(clubId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
						this.getMyAccountId(), this.getTag());
			}

			int num = clubService.querySubscriberNum(clubId);
			clubService.updateSubscriberNum(clubId, num, currentTimestamp);

			respCmd = new ClubSubscribersRemoveResp(ErrorCode.SUCCESS, clubId);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ClubSubscribersRemoveResp(ErrorCode.UNKNOWN_FAILURE, clubId);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ClubSubscribersRemoveReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ClubSubscribersRemoveAdapter.class);

}
