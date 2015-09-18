package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.account.SyncAccountBaseInfoReq;
import com.oct.ga.comm.cmd.account.SyncAccountBaseInfoResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class SyncAccountBaseAdapter
		extends StpReqCommand
{
	public SyncAccountBaseAdapter()
	{
		super();

		this.setTag(Command.SYNC_ACCOUNT_BASE_INFO_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncAccountBaseInfoReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = reqCmd.getAccountId();

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");

			// return account info: accountname, phone, photo
			AccountBasic account = supAccountService.queryAccount(accountId);

			SyncAccountBaseInfoResp respCmd = new SyncAccountBaseInfoResp(this.getSequence(), ErrorCode.SUCCESS,
					accountId, account.getNickname(), account.getAvatarUrl());
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncAccountBaseInfoResp respCmd = new SyncAccountBaseInfoResp(this.getSequence(),
					ErrorCode.UNKNOWN_FAILURE, accountId, null, null);
			return respCmd;
		}
	}

	private SyncAccountBaseInfoReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncAccountBaseAdapter.class);

}
