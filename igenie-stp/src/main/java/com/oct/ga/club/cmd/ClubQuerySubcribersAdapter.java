package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ClubQuerySubcribersReq;
import com.oct.ga.comm.cmd.club.ClubQuerySubcribersResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaClubService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ClubQuerySubcribersAdapter
		extends StpReqCommand
{
	public ClubQuerySubcribersAdapter()
	{
		super();

		this.setTag(Command.CLUB_QUERY_SUBSCRIBER_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ClubQuerySubcribersReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ClubQuerySubcribersResp respCmd = null;
		String clubId = reqCmd.getClubId();

		try {
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");

			List<AccountBasic> memberList = clubService.querySubscribers(clubId);

			respCmd = new ClubQuerySubcribersResp(ErrorCode.SUCCESS, memberList);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ClubQuerySubcribersResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ClubQuerySubcribersReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ClubQuerySubcribersAdapter.class);

}
