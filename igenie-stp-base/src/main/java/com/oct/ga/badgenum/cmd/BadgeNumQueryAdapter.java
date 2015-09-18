package com.oct.ga.badgenum.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.badgenum.BadgeNumQueryReq;
import com.oct.ga.comm.cmd.badgenum.BadgeNumQueryResp;
import com.oct.ga.comm.domain.AccountBadgeNumJsonBean;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class BadgeNumQueryAdapter
		extends StpReqCommand
{
	public BadgeNumQueryAdapter()
	{
		super();

		this.setTag(Command.QUERY_BADGE_NUMBER_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new BadgeNumQueryReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		BadgeNumQueryResp respCmd = null;

		try {
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

			AccountBadgeNumJsonBean badgeNum = badgeNumService.query(this.getMyAccountId());

			respCmd = new BadgeNumQueryResp(ErrorCode.SUCCESS, badgeNum);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new BadgeNumQueryResp(ErrorCode.UNKNOWN_FAILURE, null);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private BadgeNumQueryReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(BadgeNumQueryAdapter.class);
}
