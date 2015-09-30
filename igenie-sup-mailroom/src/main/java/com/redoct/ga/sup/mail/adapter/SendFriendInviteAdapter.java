package com.redoct.ga.sup.mail.adapter;

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
import com.redoct.ga.sup.mail.SupMailService;
import com.redoct.ga.sup.mail.cmd.SendFriendInviteEmailReq;
import com.redoct.ga.sup.mail.cmd.SendFriendInviteEmailResp;

public class SendFriendInviteAdapter
		extends SupReqCommand
{
	public SendFriendInviteAdapter()
	{
		super();

		this.setTag(SupCommandTag.SEND_FORGOT_PWD_EMAIL_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SendFriendInviteEmailReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String fromEmail = reqCmd.getFromEmail();
		String fromName = reqCmd.getFromName();
		String toEmail = reqCmd.getToEmail();
		String toName = reqCmd.getToName();
		String ekey = reqCmd.getEkey();
		SendFriendInviteEmailResp respCmd = null;

		try {
			SupMailService mailService = (SupMailService) context.getBean("supMailService");

			mailService.sendFriendInvite(fromEmail, fromName, toEmail, toName, ekey);

			respCmd = new SendFriendInviteEmailResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new SendFriendInviteEmailResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private SendFriendInviteEmailReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SendFriendInviteAdapter.class);

}
