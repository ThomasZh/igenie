package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.StringUtil;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ActivityQueryFutureFilterByLocPaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQueryFutureFilterByLocPaginationResp;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.oct.ga.comm.domain.club.ActivitySubscribeDetailInfo;
import com.oct.ga.comm.domain.club.ActivitySubscribeInfo;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.publish.IdAndTimestamp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaPublishService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityQueryFutureFilterByLocPaginationAdapter
		extends StpReqCommand
{
	public ActivityQueryFutureFilterByLocPaginationAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_FUTURE_FILTERBY_LOC_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityQueryFutureFilterByLocPaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityQueryFutureFilterByLocPaginationResp respCmd = null;
		String locX = reqCmd.getLocX();
		String locY = reqCmd.getLocY();
		String locMask = StringUtil.locMask(locX, locY);
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaPublishService publishService = (GaPublishService) context.getBean("gaPublishService");

			List<ActivitySubscribeDetailInfo> array = new ArrayList<ActivitySubscribeDetailInfo>();
			List<IdAndTimestamp> its = publishService.queryPushlishChannelIds(locMask, pageNum, pageSize);
			for (IdAndTimestamp it : its) {
				ActivityDetailInfo activity = activityService.query(it.getId(), this.getMyAccountId());
				ActivitySubscribeDetailInfo info = new ActivitySubscribeDetailInfo();

				info.setId(it.getId());
				info.setName(activity.getName());
				info.setPublishType(activity.getPublishType());
				info.setStartTime(activity.getStartTime());
				info.setState(activity.getState());
				GroupMemberDetailInfo leaderInfo = groupService.queryLeader(activity.getId());
				info.setLeaderName(leaderInfo.getNickname());
				info.setLeaderAvatarUrl(leaderInfo.getAvatarUrl());
				info.setLocDesc(activity.getLocDesc());
				info.setLocX(activity.getLocX());
				info.setLocY(activity.getLocY());
				short memberAvailableNum = groupService.queryMemberAvailableNum(activity.getId());
				info.setMemberAvailableNum(memberAvailableNum);
				info.setMemberNum(activity.getMemberNum());
				int recommendNum = activityService.queryRecommendNum(activity.getId(), this.getMyAccountId());
				info.setRecommendNum(recommendNum);
				short memberState = groupService.queryMemberState(activity.getId(), this.getMyAccountId());
				info.setMemberState(memberState);
				short memberRank = groupService.queryMemberRank(activity.getId(), this.getMyAccountId());
				info.setMemberRank(memberRank);
				info.setCreateTime(it.getTimstamp());

				array.add(info);
			}

			respCmd = new ActivityQueryFutureFilterByLocPaginationResp(sequence, ErrorCode.SUCCESS, array);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityQueryFutureFilterByLocPaginationResp(sequence, ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private ActivityQueryFutureFilterByLocPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryFutureFilterByLocPaginationAdapter.class);

}
