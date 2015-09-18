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
import com.redoct.ga.sup.account.cmd.CreateLostPwdEkeyReq;
import com.redoct.ga.sup.account.cmd.CreateLostPwdEkeyResp;

public class CreateLostPwdEkeyAdapter
		extends SupReqCommand
{
	public CreateLostPwdEkeyAdapter()
	{
		super();

		this.setTag(SupCommandTag.CREATE_ACCOUNT_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new CreateLostPwdEkeyReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		short loginType = reqCmd.getLoginType();
		String loginName = reqCmd.getLoginName();
		CreateLostPwdEkeyResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			String ekey = accountService.createEkey(loginType, loginName, this.getCurrentTimestamp());

			respCmd = new CreateLostPwdEkeyResp(this.getSequence(), ErrorCode.SUCCESS, ekey);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new CreateLostPwdEkeyResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private CreateLostPwdEkeyReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(CreateLostPwdEkeyAdapter.class);

}
