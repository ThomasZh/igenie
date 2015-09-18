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
import com.oct.ga.comm.cmd.moment.QueryMomentFavoritePaginationReq;
import com.oct.ga.comm.cmd.moment.QueryMomentFavoritePaginationResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.moment.GaMomentFavoriteObject;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryMomentFavoritePaginationAdapter
		extends StpReqCommand
{
	public QueryMomentFavoritePaginationAdapter()
	{
		super();

		this.setTag(Command.QUERY_MOMENT_FAVORITE_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryMomentFavoritePaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String momentId = reqCmd.getMomentId();
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaMomentService momentService = (GaMomentService) context.getBean("gaMomentService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			List<GaMomentFavoriteObject> array = momentService.queryMomentFavoritePagination(momentId, pageNum, pageSize);
			for (GaMomentFavoriteObject favorite : array) {
				AccountBasic account = accountService.queryAccount(favorite.getFromAccountId());
				favorite.setFromAccountName(account.getNickname());
				favorite.setFromAccountAvatarUrl(account.getAvatarUrl());
			}

			QueryMomentFavoritePaginationResp respCmd = new QueryMomentFavoritePaginationResp(sequence,
					ErrorCode.SUCCESS, array);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryMomentFavoritePaginationResp respCmd = new QueryMomentFavoritePaginationResp(sequence,
					ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private QueryMomentFavoritePaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryMomentFavoritePaginationAdapter.class);

}
