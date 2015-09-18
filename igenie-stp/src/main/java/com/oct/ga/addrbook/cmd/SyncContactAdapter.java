package com.oct.ga.addrbook.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.addrbook.SyncContactReq;
import com.oct.ga.comm.cmd.addrbook.SyncContactResp;
import com.oct.ga.comm.cmd.base.SyncTimestampResp;
import com.oct.ga.comm.domain.account.AccountDetail;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class SyncContactAdapter
		extends StpReqCommand
{
	public SyncContactAdapter()
	{
		super();

		this.setTag(Command.SYNC_CONTACT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncContactReq().decode(tlv);

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String myUserId = this.getMyAccountId();
		int lastTryTime = reqCmd.getLastTryTime();

		try {
			GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			List<AccountDetail> userIds = followingService.queryFollowingLastUpdate(myUserId, lastTryTime);
			for (AccountDetail userId : userIds) {
				AccountMaster account = accountService.queryAccountMaster(userId.getAccountId());

				SyncContactResp respCmd = new SyncContactResp(account);
				TlvObject tResp = CommandParser.encode(respCmd);
				session.write(tResp);
			}

			SyncTimestampResp respCmd = new SyncTimestampResp(Command.SYNC_CONTACT_REQ, currentTimestamp);
			TlvObject tResp = CommandParser.encode(respCmd);
			session.write(tResp);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
		}

		// Warning: OldStpEventHandler do not response anything.
		return null;
	}

	private SyncContactReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncContactAdapter.class);

}
