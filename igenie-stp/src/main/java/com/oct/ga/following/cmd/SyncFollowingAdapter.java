package com.oct.ga.following.cmd;

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
import com.oct.ga.comm.cmd.following.SyncFollowingReq;
import com.oct.ga.comm.cmd.following.SyncFollowingResp;
import com.oct.ga.comm.domain.account.AccountDetail;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class SyncFollowingAdapter
		extends StpReqCommand
{
	public SyncFollowingAdapter()
	{
		super();

		this.setTag(Command.SYNC_FOLLOWING_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncFollowingReq().decode(tlv);
		sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		int lastTryTime = reqCmd.getLastTryTime();
		String myUserId = this.getMyAccountId();

		try {
			GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			// id, state
			List<String> ids = followingService.queryFollowingLastUpdateIds(myUserId, lastTryTime);
			List<AccountDetail> followeds = accountService.queryAccountDetails(ids);

			JSONArray jsonArray = JSONArray.fromObject(followeds);
			String json = jsonArray.toString();
			logger.debug("json: " + json);

			SyncFollowingResp respCmd = new SyncFollowingResp(sequence, ErrorCode.SUCCESS, json, currentTimestamp);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncFollowingResp respCmd = new SyncFollowingResp(sequence, ErrorCode.UNKNOWN_FAILURE, null,
					currentTimestamp);
			return respCmd;
		}
	}

	private SyncFollowingReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncFollowingAdapter.class);

}
