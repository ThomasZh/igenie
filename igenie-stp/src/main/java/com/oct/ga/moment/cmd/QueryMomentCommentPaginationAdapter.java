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
import com.oct.ga.comm.cmd.moment.QueryMomentCommentPaginationReq;
import com.oct.ga.comm.cmd.moment.QueryMomentCommentPaginationResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.moment.GaMomentCommentObject;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryMomentCommentPaginationAdapter
		extends StpReqCommand
{
	public QueryMomentCommentPaginationAdapter()
	{
		super();

		this.setTag(Command.QUERY_MOMENT_COMMENT_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryMomentCommentPaginationReq().decode(tlv);
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

			List<GaMomentCommentObject> array = momentService.queryMomentCommentPagination(momentId, pageNum, pageSize);
			for (GaMomentCommentObject comment : array) {
				AccountBasic account = accountService.queryAccount(comment.getFromAccountId());
				comment.setFromAccountName(account.getNickname());
				comment.setFromAccountAvatarUrl(account.getAvatarUrl());
			}

			QueryMomentCommentPaginationResp respCmd = new QueryMomentCommentPaginationResp(sequence,
					ErrorCode.SUCCESS, array);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryMomentCommentPaginationResp respCmd = new QueryMomentCommentPaginationResp(sequence,
					ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private QueryMomentCommentPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryMomentCommentPaginationAdapter.class);

}
