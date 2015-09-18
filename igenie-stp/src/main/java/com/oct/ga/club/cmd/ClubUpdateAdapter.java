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
import com.oct.ga.comm.cmd.club.ClubUpdateReq;
import com.oct.ga.comm.cmd.club.ClubUpdateResp;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaClubService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ClubUpdateAdapter
		extends StpReqCommand
{
	public ClubUpdateAdapter()
	{
		super();

		this.setTag(Command.CLUB_UPDATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ClubUpdateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ClubUpdateResp respCmd = null;
		ClubMasterInfo club = reqCmd.getClub();

		try {
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			
			clubService.update(club, currentTimestamp);
			groupService.modifyGroupName(club.getId(), club.getName(), currentTimestamp);
			syncVerService.increase(club.getId(), GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			if (club.getSharingUserIds() != null && club.getSharingUserIds().length > 0)
				clubService.updateSubscribers(club.getId(), club.getSharingUserIds(), currentTimestamp);

			respCmd = new ClubUpdateResp(ErrorCode.SUCCESS);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
			
			respCmd = new ClubUpdateResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ClubUpdateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ClubUpdateAdapter.class);
}
