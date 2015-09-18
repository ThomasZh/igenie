package com.oct.ga.following.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.following.QueryAccountReq;
import com.oct.ga.comm.cmd.following.QueryAccountResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryAccountAdapter
		extends StpReqCommand
{
	public QueryAccountAdapter()
	{
		super();

		this.setTag(Command.QUERY_ACCOUNT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryAccountReq().decode(tlv);
		sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String loginName = reqCmd.getFriendRegisterId();
		AccountMaster account = new AccountMaster();

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			AccountBasic baseAccount = null;
			if (loginName.contains("@")) { // email
				baseAccount = accountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, loginName);	
			} else { // phone
				baseAccount = accountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_PHONE, loginName);
			}
			
			if (baseAccount.getAccountId() != null && baseAccount.getAccountId().length() > 0) {
				account = accountService.queryAccountMaster(baseAccount.getAccountId());

				QueryAccountResp respCmd = new QueryAccountResp(ErrorCode.SUCCESS, account);
				respCmd.setSequence(sequence);
				return respCmd;
			} else {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.MEMBER_NOT_GA_ACCOUNT + "]|this friend register id=[" + loginName
						+ "] is not GA account");

				QueryAccountResp respCmd = new QueryAccountResp(ErrorCode.MEMBER_NOT_GA_ACCOUNT, account);
				respCmd.setSequence(sequence);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryAccountResp respCmd = new QueryAccountResp(ErrorCode.UNKNOWN_FAILURE, account);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private QueryAccountReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryAccountAdapter.class);

}
