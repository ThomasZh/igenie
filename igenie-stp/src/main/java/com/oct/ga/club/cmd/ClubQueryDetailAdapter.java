package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ClubQueryDetailReq;
import com.oct.ga.comm.cmd.club.ClubQueryDetailResp;
import com.oct.ga.comm.domain.club.ClubDetailInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaClubService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ClubQueryDetailAdapter
		extends StpReqCommand
{
	public ClubQueryDetailAdapter()
	{
		super();

		this.setTag(Command.CLUB_QUERY_DETAIL_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ClubQueryDetailReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ClubQueryDetailResp respCmd = null;
		String clubId = reqCmd.getClubId();

		try {
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");

			ClubDetailInfo detail = clubService.queryDetail(clubId);

			int num = groupService.queryChildNum(clubId);
			detail.setActivityNum(num);

			num = activityService.countTotalJoinNum(clubId);
			detail.setTotalJoinNum(num);

			// change fileTransId => url
//			GaFtpService ftpService = (GaFtpService) context.getBean("gaFtpService");
//			String titleBkImageUrl = ftpService.queryFileUrl(detail.getTitleBkImage());
//			detail.setTitleBkImage(titleBkImageUrl);
			logger.debug("title background image url: " + detail.getTitleBkImage());

			JSONObject jsonObject = JSONObject.fromObject(detail);
			String json = jsonObject.toString();

			respCmd = new ClubQueryDetailResp(ErrorCode.SUCCESS, json);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ClubQueryDetailResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ClubQueryDetailReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ClubQueryDetailAdapter.class);

}
