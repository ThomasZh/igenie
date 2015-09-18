package com.oct.ga.following.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.following.ImportFollowingReq;
import com.oct.ga.comm.cmd.following.ImportFollowingResp;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaFollowingService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class ImportContactAdapter
		extends StpReqCommand
{
	public ImportContactAdapter()
	{
		super();

		this.setTag(Command.IMPORT_FOLLOWING_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ImportFollowingReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ImportFollowingResp respCmd = null;
		String myUserId = this.getMyAccountId();
		String accountId = reqCmd.getAccountId();

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaFollowingService followingService = (GaFollowingService) context.getBean("gaFollowingService");

			AccountMaster account = accountService.queryAccountMaster(accountId);
			followingService.follow(myUserId, accountId, currentTimestamp);

			respCmd = new ImportFollowingResp(ErrorCode.SUCCESS, account);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ImportFollowingResp(ErrorCode.UNKNOWN_FAILURE, null);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private ImportFollowingReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ImportContactAdapter.class);

}
