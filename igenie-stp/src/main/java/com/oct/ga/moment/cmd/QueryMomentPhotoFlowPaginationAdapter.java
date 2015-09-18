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
import com.oct.ga.comm.cmd.moment.QueryMomentPhotoFlowPaginationReq;
import com.oct.ga.comm.cmd.moment.QueryMomentPhotoFlowPaginationResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.domain.moment.GaMomentPhotoObject;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryMomentPhotoFlowPaginationAdapter
		extends StpReqCommand
{
	public QueryMomentPhotoFlowPaginationAdapter()
	{
		super();

		this.setTag(Command.QUERY_MOMENT_PHOTOFLOW_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryMomentPhotoFlowPaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String taskId = reqCmd.getTaskId();
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaMomentService momentService = (GaMomentService) context.getBean("gaMomentService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			List<GaMomentPhotoObject> momentPhotos = momentService.queryMomentPhotoFlowPagination(taskId, pageNum,
					pageSize);

			for (GaMomentPhotoObject photo : momentPhotos) {
				GaMomentObject moment = momentService.queryMoment(photo.getMomentId());
				photo.setDesc(moment.getDesc());
				photo.setUserId(moment.getUserId());

				AccountBasic userInfo = accountService.queryAccount(moment.getUserId());
				photo.setUserName(userInfo.getNickname());
				photo.setUserPhotoUrl(userInfo.getAvatarUrl());
			}

			QueryMomentPhotoFlowPaginationResp respCmd = new QueryMomentPhotoFlowPaginationResp(sequence,
					ErrorCode.SUCCESS, momentPhotos);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryMomentPhotoFlowPaginationResp respCmd = new QueryMomentPhotoFlowPaginationResp(sequence,
					ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private QueryMomentPhotoFlowPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryMomentPhotoFlowPaginationAdapter.class);

}
