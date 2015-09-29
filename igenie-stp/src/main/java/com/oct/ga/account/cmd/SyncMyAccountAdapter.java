package com.oct.ga.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.account.SyncMyAccountReq;
import com.oct.ga.comm.cmd.account.SyncMyAccountResp;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class SyncMyAccountAdapter extends StpReqCommand {
	public SyncMyAccountAdapter() {
		super();

		this.setTag(Command.SYNC_MY_ACCOUNT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv) throws UnsupportedEncodingException {
		reqCmd = new SyncMyAccountReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context) throws Exception {
		String accountId = this.getMyAccountId();

		try {
			SupAccountService supAccountService = (SupAccountService) context.getBean("supAccountService");

			// return account info: accountname, phone, photo
			AccountMaster account = supAccountService.queryAccountMaster(accountId);

			// resp the current timestamp of server
			SyncMyAccountResp respCmd = new SyncMyAccountResp(sequence, account, currentTimestamp);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
		}

		// Warning: OldStpEventHandler do not response anything.
		return null;
	}

	private SyncMyAccountReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncMyAccountAdapter.class);

}
