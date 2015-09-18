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
import com.oct.ga.comm.cmd.club.ClubCreateReq;
import com.oct.ga.comm.cmd.club.ClubCreateResp;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaClubService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ClubCreateAdapter
		extends StpReqCommand
{
	public ClubCreateAdapter()
	{
		super();

		this.setTag(Command.CLUB_CREATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ClubCreateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ClubCreateResp respCmd = null;
		ClubMasterInfo club = reqCmd.getClub();

		try {
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");

			club.setCreatorId(this.getMyAccountId());
			String clubId = clubService.create(club, currentTimestamp);
			groupService.createGroup(clubId, club.getName(), GlobalArgs.CHANNEL_TYPE_CLUB, currentTimestamp,
					this.getMyAccountId());
			syncVerService.increase(clubId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			groupService.joinAsLeader(clubId, this.getMyAccountId(), currentTimestamp);
			syncVerService.increase(clubId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			// Add myself as a member
			// -1 as owner create time is at first list
			clubService.addSubscriber(clubId, this.getMyAccountId(), GlobalArgs.INVITE_STATE_ACCPET,
					currentTimestamp - 1);

			if (club.getSharingUserIds() != null && club.getSharingUserIds().length > 0)
				for (String userId : club.getSharingUserIds()) {
					clubService.addSubscriber(club.getId(), userId, GlobalArgs.INVITE_STATE_APPLY, currentTimestamp);
				}

			int num = clubService.querySubscriberNum(clubId);
			clubService.updateSubscriberNum(clubId, num, currentTimestamp);

			respCmd = new ClubCreateResp(ErrorCode.SUCCESS, clubId);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ClubCreateResp(ErrorCode.UNKNOWN_FAILURE, club.getId());
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ClubCreateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ClubCreateAdapter.class);
}
