package com.redoct.ga.sup.account.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.cmd.CreateAccountReq;
import com.redoct.ga.sup.account.cmd.CreateAccountResp;

public class CreateAccountAdapter
		extends SupReqCommand
{
	public CreateAccountAdapter()
	{
		super();

		this.setTag(SupCommandTag.CREATE_ACCOUNT_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new CreateAccountReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String nickname = reqCmd.getNickname();
		String avatarUrl = reqCmd.getAvatarUrl();
		String desc = reqCmd.getDesc();
		CreateAccountResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			String accountId = accountService.createAccount(nickname, avatarUrl, desc, getCurrentTimestamp());

			respCmd = new CreateAccountResp(this.getSequence(), ErrorCode.SUCCESS, accountId);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new CreateAccountResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private CreateAccountReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(CreateAccountAdapter.class);

}
