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
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribePaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribePaginationResp;
import com.oct.ga.comm.domain.club.ActivitySubscribeInfo;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityQuerySubscribePaginationAdapter
		extends StpReqCommand
{
	public ActivityQuerySubscribePaginationAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_SUBSCRIBE_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityQuerySubscribePaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityQuerySubscribePaginationResp respCmd = null;
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			int today = currentTimestamp - currentTimestamp % (86400);// wholeDays

			// endTime>today
			List<ActivitySubscribeInfo> array = activityService.querySubscribePagination(this.getMyAccountId(), today,
					pageNum, pageSize);

			for (ActivitySubscribeInfo activity : array) {
				String clubName = groupService.queryGroupName(activity.getPid());
				activity.setClubName(clubName);

				short memberRank = groupService.queryMemberRank(activity.getId(), this.getMyAccountId());
				activity.setMemberRank(memberRank);
				short memberState = groupService.queryMemberState(activity.getId(), this.getMyAccountId());
				activity.setMemberState(memberState);
				short memberAvailableNum = groupService.queryMemberAvailableNum(activity.getId());
				activity.setMemberAvailableNum(memberAvailableNum);

				activity.setRecommendNum(activityService.queryRecommendNum(activity.getId(), this.getMyAccountId()));

				GroupMemberDetailInfo leaderInfo = groupService.queryLeader(activity.getId());
				activity.setLeaderName(leaderInfo.getNickname());
				activity.setLeaderAvatarUrl(leaderInfo.getAvatarUrl());
			}

			respCmd = new ActivityQuerySubscribePaginationResp(sequence, ErrorCode.SUCCESS, array);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityQuerySubscribePaginationResp(sequence, ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private ActivityQuerySubscribePaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityQuerySubscribePaginationAdapter.class);

}
