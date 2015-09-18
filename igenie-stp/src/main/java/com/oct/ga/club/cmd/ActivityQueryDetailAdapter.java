package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ActivityQueryDetailReq;
import com.oct.ga.comm.cmd.club.ActivityQueryDetailResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.oct.ga.comm.domain.club.ActivityRecommend;
import com.oct.ga.comm.domain.club.ClubDetailInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaClubService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class ActivityQueryDetailAdapter
		extends StpReqCommand
{
	public ActivityQueryDetailAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_DETAIL_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityQueryDetailReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityQueryDetailResp respCmd = null;
		String activityId = reqCmd.getActivityId();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");

			ActivityDetailInfo detail = activityService.query(activityId, this.getMyAccountId());

			// Logic: add club title background image url into activity info
			ClubDetailInfo clubDetail = clubService.queryDetail(detail.getPid());
			int num = groupService.queryChildNum(detail.getPid());
			clubDetail.setActivityNum(num);

			logger.debug("title background image url: " + detail.getTitleBkImage());

			// add leader info into activity info
			String leaderId = groupService.queryLeaderId(activityId);

			// id, firstName, imageFileTransId
			AccountBasic leaderInfo = accountService.queryAccount(leaderId);

			logger.debug("leader face photo url: " + leaderInfo.getAvatarUrl());
			leaderInfo.setAvatarUrl(leaderInfo.getAvatarUrl());

			List<AccountBasic> members = new ArrayList<AccountBasic>();
			members.add(leaderInfo);
			detail.setMembers(members);

			short memberRank = groupService.queryMemberRank(activityId, this.getMyAccountId());
			detail.setMemberRank(memberRank);
			short memberState = groupService.queryMemberState(activityId, this.getMyAccountId());
			detail.setMemberState(memberState);
			int memberAvailableNum = groupService.queryMemberAvailableNum(activityId);
			detail.setMemberAvailableNum((short) memberAvailableNum);

			List<ActivityRecommend> recommends = detail.getRecommends();
			for (ActivityRecommend recommend : recommends) {
				AccountBasic user = accountService.queryAccount(recommend.getFromUserId());
				recommend.setFromUserName(user.getNickname());
				recommend.setFromUserImageUrl(user.getAvatarUrl());
			}
			detail.setRecommends(recommends);

			respCmd = new ActivityQueryDetailResp(ErrorCode.SUCCESS, detail);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityQueryDetailResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityQueryDetailReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryDetailAdapter.class);

}
