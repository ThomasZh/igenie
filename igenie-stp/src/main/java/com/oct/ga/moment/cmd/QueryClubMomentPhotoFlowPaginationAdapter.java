package com.oct.ga.moment.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.moment.QueryClubMomentPhotoFlowPaginationReq;
import com.oct.ga.comm.cmd.moment.QueryClubMomentPhotoFlowPaginationResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.domain.moment.GaMomentPhotoObject;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryClubMomentPhotoFlowPaginationAdapter
		extends StpReqCommand
{
	public QueryClubMomentPhotoFlowPaginationAdapter()
	{
		super();

		this.setTag(Command.QUERY_CLUB_MOMENT_PHOTOFLOW_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryClubMomentPhotoFlowPaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String clubId = reqCmd.getClubId();
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();
		String json = null;

		try {
			GaMomentService momentService = (GaMomentService) context.getBean("gaMomentService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			List<GaMomentPhotoObject> momentPhotos = momentService.queryClubMomentPhotoFlowPagination(clubId, pageNum,
					pageSize);

			for (GaMomentPhotoObject photo : momentPhotos) {
				GaMomentObject moment = momentService.queryMoment(photo.getMomentId());
				photo.setDesc(moment.getDesc());
				photo.setUserId(moment.getUserId());
				
				AccountBasic userInfo = accountService.queryAccount(moment.getUserId());
				photo.setUserName(userInfo.getNickname());
				photo.setUserPhotoUrl(userInfo.getAvatarUrl());
			}

			JSONArray jsonArray = JSONArray.fromObject(momentPhotos);
			json = jsonArray.toString();
			logger.debug("json: " + json);

			QueryClubMomentPhotoFlowPaginationResp respCmd = new QueryClubMomentPhotoFlowPaginationResp(
					ErrorCode.SUCCESS, json);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryClubMomentPhotoFlowPaginationResp respCmd = new QueryClubMomentPhotoFlowPaginationResp(
					ErrorCode.UNKNOWN_FAILURE, json);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private QueryClubMomentPhotoFlowPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryClubMomentPhotoFlowPaginationAdapter.class);

}
