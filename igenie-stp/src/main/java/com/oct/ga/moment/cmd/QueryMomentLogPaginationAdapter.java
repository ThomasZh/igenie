package com.oct.ga.moment.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.moment.QueryMomentLogPaginationReq;
import com.oct.ga.comm.cmd.moment.QueryMomentLogPaginationResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.moment.GaMomentLogObject;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryMomentLogPaginationAdapter
		extends StpReqCommand
{
	public QueryMomentLogPaginationAdapter()
	{
		super();

		this.setTag(Command.QUERY_MOMENT_LOG_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryMomentLogPaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String myAccountId = this.getMyAccountId();
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaMomentService momentService = (GaMomentService) context.getBean("gaMomentService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

			List<GaMomentLogObject> array = momentService.queryLogPagination(myAccountId, pageNum, pageSize);
			for (GaMomentLogObject log : array) {
				AccountBasic account = accountService.queryAccount(log.getFromAccountId());
				log.setFromAccountName(account.getNickname());
				log.setFromAccountAvatarUrl(account.getAvatarUrl());
			}

			momentService.modifyLogSyncState(myAccountId, GlobalArgs.SYNC_STATE_RECEIVED);
			short badgeNum = badgeNumService.countMomentLogNum(myAccountId);
			badgeNumService.modifyMomentLogNum(myAccountId, badgeNum);

			QueryMomentLogPaginationResp respCmd = new QueryMomentLogPaginationResp(sequence, ErrorCode.SUCCESS, array);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryMomentLogPaginationResp respCmd = new QueryMomentLogPaginationResp(sequence,
					ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private QueryMomentLogPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryMomentLogPaginationAdapter.class);

}
