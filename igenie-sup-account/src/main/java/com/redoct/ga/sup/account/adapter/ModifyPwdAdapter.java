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
import com.redoct.ga.sup.account.cmd.ModifyPwdReq;
import com.redoct.ga.sup.account.cmd.ModifyPwdResp;

public class ModifyPwdAdapter
		extends SupReqCommand
{
	public ModifyPwdAdapter()
	{
		super();

		this.setTag(SupCommandTag.MODIFY_PWD_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ModifyPwdReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		short loginType = reqCmd.getLoginType();
		String loginName = reqCmd.getLoginName();
		String md5Pwd = reqCmd.getMd5Pwd();
		ModifyPwdResp respCmd = null;

		try {
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			accountService.resetPwd(loginType, loginName, md5Pwd, this.getCurrentTimestamp());

			respCmd = new ModifyPwdResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new ModifyPwdResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ModifyPwdReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ModifyPwdAdapter.class);

}
