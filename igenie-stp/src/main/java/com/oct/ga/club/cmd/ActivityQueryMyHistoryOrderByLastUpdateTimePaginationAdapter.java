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
import com.oct.ga.comm.cmd.club.ActivityQueryMyHistoryOrderByLastUpdateTimePaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQueryMyHistoryOrderByLastUpdateTimePaginationResp;
import com.oct.ga.comm.domain.club.ActivitySubscribeDetailInfo;
import com.oct.ga.comm.domain.club.ActivitySubscribeInfo;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.moment.GaMomentPhotoObject;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityQueryMyHistoryOrderByLastUpdateTimePaginationAdapter
		extends StpReqCommand
{
	public ActivityQueryMyHistoryOrderByLastUpdateTimePaginationAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_MY_HISTORY_ORDERBY_LAST_UPDATE_TIME_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityQueryMyHistoryOrderByLastUpdateTimePaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityQueryMyHistoryOrderByLastUpdateTimePaginationResp respCmd = null;
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaMomentService momentService = (GaMomentService) context.getBean("gaMomentService");

			int today = currentTimestamp - currentTimestamp % (86400);// wholeDays
			List<ActivitySubscribeInfo> array = activityService.queryHistoryOrderByLastUpdateTimePagination(
					getMyAccountId(), today, pageNum, pageSize);

			for (ActivitySubscribeInfo activity : array) {
				short memberRank = groupService.queryMemberRank(activity.getId(), this.getMyAccountId());
				activity.setMemberRank(memberRank);

				activity.setRecommendNum(activityService.queryRecommendNum(activity.getId(), this.getMyAccountId()));

				GroupMemberDetailInfo leaderInfo = groupService.queryLeader(activity.getId());
				activity.setLeaderName(leaderInfo.getNickname());
				activity.setLeaderAvatarUrl(leaderInfo.getAvatarUrl());

				List<GaMomentPhotoObject> momentPhotos = momentService.queryMomentPhotoFlowPagination(activity.getId(),
						(short) 1, (short) 4);

				List<String> images = new ArrayList<String>();
				for (GaMomentPhotoObject momentPhoto : momentPhotos) {
					images.add(momentPhoto.getPhotoUrl());
				}
				activity.setImages(images);
				
				short memberAvailableNum = groupService.queryMemberAvailableNum(activity.getId());
				activity.setMemberAvailableNum(memberAvailableNum);
			}

			respCmd = new ActivityQueryMyHistoryOrderByLastUpdateTimePaginationResp(ErrorCode.SUCCESS, array);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityQueryMyHistoryOrderByLastUpdateTimePaginationResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityQueryMyHistoryOrderByLastUpdateTimePaginationReq reqCmd;

	private final static Logger logger = LoggerFactory
			.getLogger(ActivityQueryMyHistoryOrderByLastUpdateTimePaginationAdapter.class);

}
