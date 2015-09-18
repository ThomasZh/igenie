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
import com.oct.ga.comm.cmd.club.ClubSubscribersUpdateReq;
import com.oct.ga.comm.cmd.club.ClubSubscribersUpdateResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaClubService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ClubSubscribersUpdateAdapter
		extends StpReqCommand
{
	public ClubSubscribersUpdateAdapter()
	{
		super();

		this.setTag(Command.CLUB_SUBSCRIBER_UPDATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ClubSubscribersUpdateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ClubSubscribersUpdateResp respCmd = null;
		String clubId = reqCmd.getClubId();
		String[] userIds = reqCmd.getUserIds();

		try {
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");

			if (clubId != null && userIds != null) {
				clubService.updateSubscribers(clubId, userIds, currentTimestamp);
				
				syncVerService.increase(clubId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
						this.getMyAccountId(), this.getTag());
			}

			respCmd = new ClubSubscribersUpdateResp(ErrorCode.SUCCESS, clubId);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ClubSubscribersUpdateResp(ErrorCode.UNKNOWN_FAILURE, clubId);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ClubSubscribersUpdateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ClubSubscribersUpdateAdapter.class);

}
