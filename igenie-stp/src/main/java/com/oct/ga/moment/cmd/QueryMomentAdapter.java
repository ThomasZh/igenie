package com.oct.ga.moment.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.moment.QueryMomentReq;
import com.oct.ga.comm.cmd.moment.QueryMomentResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.moment.GaMomentCommentObject;
import com.oct.ga.comm.domain.moment.GaMomentFavoriteObject;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryMomentAdapter
		extends StpReqCommand
{
	public QueryMomentAdapter()
	{
		super();

		this.setTag(Command.QUERY_MOMENT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryMomentReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String momentId = reqCmd.getMomentId();

		try {
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaMomentService momentService = (GaMomentService) context.getBean("gaMomentService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			GaMomentObject moment = momentService.queryMoment(momentId);

			moment.setChannelId(moment.getChannelId());
			String channelName = groupService.queryGroupName(moment.getChannelId());
			moment.setChannelName(channelName);

			List<String> photos = momentService.queryMomentPhotos(momentId);
			moment.setPhotos(photos);

			AccountBasic userInfo = accountService.queryAccount(moment.getUserId());
			moment.setUserName(userInfo.getNickname());
			moment.setUserPhotoUrl(userInfo.getAvatarUrl());

			short memberRank = groupService.queryMemberRank(moment.getChannelId(), this.getMyAccountId());
			moment.setMemberRank(memberRank);

			boolean isFavorite = momentService.isFavorte(moment.getMomentId(), this.getMyAccountId());
			moment.setFavorite(isFavorite);

			short firstPageNum = 1;
			short favoritePageSize = 100;
			short commentPageSize = 100;
			List<GaMomentFavoriteObject> favorites = momentService.queryMomentFavoritePagination(moment.getMomentId(),
					firstPageNum, favoritePageSize);
			for (GaMomentFavoriteObject favorite : favorites) {
				AccountBasic account = accountService.queryAccount(favorite.getFromAccountId());
				favorite.setFromAccountName(account.getNickname());
				favorite.setFromAccountAvatarUrl(account.getAvatarUrl());
			}
			moment.setFavorites(favorites);

			List<GaMomentCommentObject> comments = momentService.queryMomentCommentPagination(moment.getMomentId(),
					firstPageNum, commentPageSize);
			for (GaMomentCommentObject comment : comments) {
				AccountBasic account = accountService.queryAccount(comment.getFromAccountId());
				comment.setFromAccountName(account.getNickname());
				comment.setFromAccountAvatarUrl(account.getAvatarUrl());
			}
			moment.setComments(comments);

			QueryMomentResp respCmd = new QueryMomentResp(sequence, ErrorCode.SUCCESS, moment);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryMomentResp respCmd = new QueryMomentResp(sequence, ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private QueryMomentReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryMomentAdapter.class);

}
