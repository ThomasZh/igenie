package com.oct.ga.moment.cmd;

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
import com.oct.ga.comm.cmd.moment.QueryAllMomentsPaginationReq;
import com.oct.ga.comm.cmd.moment.QueryAllMomentsPaginationResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.moment.GaMomentCommentObject;
import com.oct.ga.comm.domain.moment.GaMomentFavoriteObject;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryAllMomentsPaginationAdapter
		extends StpReqCommand
{
	public QueryAllMomentsPaginationAdapter()
	{
		super();

		this.setTag(Command.QUERY_ALL_MOMENT_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryAllMomentsPaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();
		String accountId = this.getMyAccountId();

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaMomentService momentService = (GaMomentService) context.getBean("gaMomentService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			List<GaMomentObject> moments = new ArrayList<GaMomentObject>();
			List<String> logIds = taskService.queryLogIdsPaginationFilter4Moment(accountId, pageNum, pageSize);
			for (String logId : logIds) {
				GaTaskLog log = taskService.queryLog(logId);
				String momentId = log.getToActionId();
				GaMomentObject moment = momentService.queryMoment(momentId);

				moment.setChannelId(log.getChannelId());
				String channelName = groupService.queryGroupName(log.getChannelId());
				moment.setChannelName(channelName);

				List<String> photos = momentService.queryMomentPhotos(momentId);
				moment.setPhotos(photos);

				AccountBasic userInfo = accountService.queryAccount(log.getFromAccountId());
				moment.setUserName(userInfo.getNickname());
				moment.setUserPhotoUrl(userInfo.getAvatarUrl());

				short memberRank = groupService.queryMemberRank(log.getChannelId(), this.getMyAccountId());
				moment.setMemberRank(memberRank);
				
				boolean isFavorite = momentService.isFavorte(moment.getMomentId(), this.getMyAccountId());
				moment.setFavorite(isFavorite);

				short firstPageNum = 1;
				short favoritePageSize = 5;
				short commentPageSize = 5;
				List<GaMomentFavoriteObject> favorites = momentService.queryMomentFavoritePagination(
						moment.getMomentId(), firstPageNum, favoritePageSize);
				for (GaMomentFavoriteObject favorite:favorites) {
					AccountBasic account = accountService.queryAccount(favorite.getFromAccountId());
					favorite.setFromAccountName(account.getNickname());
					favorite.setFromAccountAvatarUrl(account.getAvatarUrl());
				}
				moment.setFavorites(favorites);

				List<GaMomentCommentObject> comments = momentService.queryMomentCommentPagination(moment.getMomentId(),
						firstPageNum, commentPageSize);
				for (GaMomentCommentObject comment:comments) {
					AccountBasic account = accountService.queryAccount(comment.getFromAccountId());
					comment.setFromAccountName(account.getNickname());
					comment.setFromAccountAvatarUrl(account.getAvatarUrl());
				}
				moment.setComments(comments);
				
				moments.add(moment);
			}

			QueryAllMomentsPaginationResp respCmd = new QueryAllMomentsPaginationResp(sequence, ErrorCode.SUCCESS,
					moments);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryAllMomentsPaginationResp respCmd = new QueryAllMomentsPaginationResp(sequence,
					ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private QueryAllMomentsPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryAllMomentsPaginationAdapter.class);

}
