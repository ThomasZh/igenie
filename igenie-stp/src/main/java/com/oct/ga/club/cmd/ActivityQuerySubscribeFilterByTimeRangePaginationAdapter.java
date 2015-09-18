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
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribeFilterByTimeRangePaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribeFilterByTimeRangePaginationResp;
import com.oct.ga.comm.domain.club.ActivitySubscribeInfo;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;

/**
 * Order By StartTime Filter By StartTime And EndTime
 * 
 * @author thomas
 */
public class ActivityQuerySubscribeFilterByTimeRangePaginationAdapter
		extends StpReqCommand
{
	public ActivityQuerySubscribeFilterByTimeRangePaginationAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_SUBSCRIBE_FILTER_BY_TIME_RANGE_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityQuerySubscribeFilterByTimeRangePaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityQuerySubscribeFilterByTimeRangePaginationResp respCmd = null;
		int startTime = reqCmd.getStartTime();
		int endTime = reqCmd.getEndTime();
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			List<ActivitySubscribeInfo> array = activityService.querySubscribePagination(this.getMyAccountId(),
					startTime, endTime, pageNum, pageSize);

			for (ActivitySubscribeInfo activity : array) {
				String clubName = groupService.queryGroupName(activity.getPid());
				activity.setClubName(clubName);

				short memberRank = groupService.queryMemberRank(activity.getId(), this.getMyAccountId());
				activity.setMemberRank(memberRank);
				short memberState = groupService.queryMemberState(activity.getId(), this.getMyAccountId());
				activity.setMemberState(memberState);

				activity.setRecommendNum(activityService.queryRecommendNum(activity.getId(), this.getMyAccountId()));

				GroupMemberDetailInfo leaderInfo = groupService.queryLeader(activity.getId());
				activity.setLeaderName(leaderInfo.getNickname());
				activity.setLeaderAvatarUrl(leaderInfo.getAvatarUrl());
				
				short memberAvailableNum = groupService.queryMemberAvailableNum(activity.getId());
				activity.setMemberAvailableNum(memberAvailableNum);
			}

			respCmd = new ActivityQuerySubscribeFilterByTimeRangePaginationResp(sequence, ErrorCode.SUCCESS, array);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityQuerySubscribeFilterByTimeRangePaginationResp(sequence, ErrorCode.UNKNOWN_FAILURE,
					null);
			return respCmd;
		}
	}

	private ActivityQuerySubscribeFilterByTimeRangePaginationReq reqCmd;

	private final static Logger logger = LoggerFactory
			.getLogger(ActivityQuerySubscribeFilterByTimeRangePaginationAdapter.class);

}
