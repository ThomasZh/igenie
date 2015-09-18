package com.oct.ga.msgflow.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.msgflow.QueryMsgFlowPaginationReq;
import com.oct.ga.comm.cmd.msgflow.QueryMsgFlowPaginationResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ActivitySubscribeInfo;
import com.oct.ga.comm.domain.moment.GaMomentCommentObject;
import com.oct.ga.comm.domain.moment.GaMomentFavoriteObject;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryMsgFlowPaginationAdapter
		extends StpReqCommand
{
	public QueryMsgFlowPaginationAdapter()
	{
		super();

		this.setTag(Command.MESSAGE_FLOW_QUERY_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryMsgFlowPaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		QueryMsgFlowPaginationResp respCmd = null;
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaMomentService momentService = (GaMomentService) context.getBean("gaMomentService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			List<MsgFlowBasicInfo> array = new ArrayList<MsgFlowBasicInfo>();
			List<String> logIds = taskService.queryLogIdsPagination(this.getMyAccountId(), pageNum, pageSize);
			for (String logId : logIds) {
				GaTaskLog log = taskService.queryLog(logId);
				String channelName = groupService.queryGroupName(log.getChannelId());
				MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();

				switch (log.getActionTag()) {
				case GlobalArgs.TASK_ACTION_ADD: // create
				case GlobalArgs.TASK_ACTION_RECOMMEND: // recommend
					ActivitySubscribeInfo activity = activityService.queryActivitySubscribeInfo(log.getChannelId());

					activity.setName(channelName);

					short memberRank = groupService.queryMemberRank(activity.getId(), this.getMyAccountId());
					activity.setMemberRank(memberRank);
					short memberState = groupService.queryMemberState(activity.getId(), this.getMyAccountId());
					activity.setMemberState(memberState);
					short memberAvailableNum = groupService.queryMemberAvailableNum(activity.getId());
					activity.setMemberAvailableNum(memberAvailableNum);

					String leaderId = groupService.queryLeaderId(activity.getId());
					AccountBasic leaderInfo = accountService.queryAccount(leaderId);
					activity.setLeaderName(leaderInfo.getNickname());
					activity.setLeaderAvatarUrl(leaderInfo.getAvatarUrl());

					msgFlowBasicInfo.setActivity(activity);
					break;
				case GlobalArgs.TASK_ACTION_ADD_ATTACH: {// moment
					String momentId = log.getToActionId();
					GaMomentObject moment = momentService.queryMoment(momentId);

					moment.setChannelId(log.getChannelId());
					moment.setChannelName(channelName);

					List<String> photos = momentService.queryMomentPhotos(momentId);
					moment.setPhotos(photos);

					AccountBasic userInfo = accountService.queryAccount(log.getFromAccountId());
					moment.setUserName(userInfo.getNickname());
					moment.setUserPhotoUrl(userInfo.getAvatarUrl());

					boolean isFavorite = momentService.isFavorte(moment.getMomentId(), this.getMyAccountId());
					moment.setFavorite(isFavorite);

					short firstPageNum = 1;
					short favoritePageSize = 5;
					short commentPageSize = 5;
					List<GaMomentFavoriteObject> favorites = momentService.queryMomentFavoritePagination(
							moment.getMomentId(), firstPageNum, favoritePageSize);
					for (GaMomentFavoriteObject favorite : favorites) {
						AccountBasic account = accountService.queryAccount(favorite.getFromAccountId());
						favorite.setFromAccountName(account.getNickname());
						favorite.setFromAccountAvatarUrl(account.getAvatarUrl());
					}
					moment.setFavorites(favorites);

					List<GaMomentCommentObject> comments = momentService.queryMomentCommentPagination(
							moment.getMomentId(), firstPageNum, commentPageSize);
					for (GaMomentCommentObject comment : comments) {
						AccountBasic account = accountService.queryAccount(comment.getFromAccountId());
						comment.setFromAccountName(account.getNickname());
						comment.setFromAccountAvatarUrl(account.getAvatarUrl());
					}
					moment.setComments(comments);

					msgFlowBasicInfo.setMoment(moment);
					break;
				}
				case GlobalArgs.TASK_ACTION_MOMENT_FAVORITE:
				case GlobalArgs.TASK_ACTION_MOMENT_COMMENT: {
					try {
						String momentId = log.getToActionId();
						String momentOwnerId = momentService.queryMomentOwner(momentId);
						AccountBasic account = accountService.queryAccount(momentOwnerId);
						msgFlowBasicInfo.setToActionId(momentOwnerId);
						msgFlowBasicInfo.setToActionAccountName(account.getNickname());
						msgFlowBasicInfo.setToActionAccountAvatarUrl(account.getAvatarUrl());
					} catch (Exception e) {
						logger.error(LogErrorMessage.getFullInfo(e));
					}
				}
					break;
				case GlobalArgs.TASK_ACTION_CHANGE_TIME:
				case GlobalArgs.TASK_ACTION_CANCELED:
				case GlobalArgs.TASK_ACTION_COMPLETED:
				case GlobalArgs.TASK_ACTION_UNCOMPLETED:
				case GlobalArgs.TASK_ACTION_JOIN:
				case GlobalArgs.TASK_ACTION_QUIT:
				case GlobalArgs.TASK_ACTION_APPLY:
					break;
				case GlobalArgs.TASK_ACTION_ACCEPT:
				case GlobalArgs.TASK_ACTION_REJECT:
				case GlobalArgs.TASK_ACTION_REFILL:
				case GlobalArgs.TASK_ACTION_KICKOUT_MEMBER: {
					try {
						AccountBasic account = accountService.queryAccount(log.getToActionId());
						msgFlowBasicInfo.setToActionAccountName(account.getNickname());
						msgFlowBasicInfo.setToActionAccountAvatarUrl(account.getAvatarUrl());
					} catch (Exception e) {
						logger.error(LogErrorMessage.getFullInfo(e));
					}
				}
					break;
				}

				msgFlowBasicInfo.setLogId(log.getLogId());
				msgFlowBasicInfo.setChannelId(log.getChannelId());
				msgFlowBasicInfo.setFromAccountId(log.getFromAccountId());
				msgFlowBasicInfo.setActionTag(log.getActionTag());
				msgFlowBasicInfo.setToActionId(log.getToActionId());

				AccountBasic account = accountService.queryAccount(log.getFromAccountId());
				msgFlowBasicInfo.setFromAccountName(account.getNickname());
				msgFlowBasicInfo.setFromAccountAvatarUrl(account.getAvatarUrl());

				msgFlowBasicInfo.setChannelName(channelName);
				msgFlowBasicInfo.setTimestamp(log.getTimestamp());

				array.add(msgFlowBasicInfo);
			}

			respCmd = new QueryMsgFlowPaginationResp(sequence, ErrorCode.SUCCESS, array);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new QueryMsgFlowPaginationResp(sequence, ErrorCode.SUCCESS, null);
			return respCmd;
		}
	}

	private QueryMsgFlowPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryMsgFlowPaginationAdapter.class);

}
