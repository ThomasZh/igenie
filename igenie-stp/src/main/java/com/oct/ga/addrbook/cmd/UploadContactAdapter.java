package com.oct.ga.addrbook.cmd;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.addrbook.ContactServiceImpl;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.addrbook.UploadContactReq;
import com.oct.ga.comm.cmd.addrbook.UploadContactResp;
import com.oct.ga.comm.domain.Contact;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class UploadContactAdapter
		extends StpReqCommand
{
	public UploadContactAdapter()
	{
		super();

		this.setTag(Command.UPLOAD_CONTACT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new UploadContactReq().decode(tlv);
		sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		Contact contact = reqCmd.getContact();
		UploadContactResp respCmd = null;

		try {
			ContactServiceImpl contactService = (ContactServiceImpl) context.getBean("gaContactService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			// exist myAccountId, just update it.
			if (contact.getAccountId() != null && contact.getAccountId().length() > 0) {

				AccountBasic account = accountService.queryAccount(contact.getAccountId());
				contactService.update(contact, currentTimestamp);

				respCmd = new UploadContactResp(ErrorCode.SUCCESS, contact.getContactId(), contact.getState(),
						contact.getAccountId(), account.getNickname(), account.getAvatarUrl());
			} else {
				// client has no account info for this friend, so:
				// check to see, if the email is in system;
				respCmd = updateContactNotSystemAccount(context, contact);
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new UploadContactResp(ErrorCode.UNKNOWN_FAILURE, contact.getContactId(),
					GlobalArgs.CONTACT_STATE_NOT_GA_USER, null, null, null);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private UploadContactResp updateContactNotSystemAccount(ApplicationContext context, Contact contact)
			throws IOException, InterruptedException, SupSocketException
	{
		ContactServiceImpl contactService = (ContactServiceImpl) context.getBean("gaContactService");
		SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

		UploadContactResp respCmd = null;

		if (contact.getEmail() != null & contact.getEmail().length() > 0) {
			respCmd = new UploadContactResp(ErrorCode.UNKNOWN_FAILURE, contact.getContactId(),
					GlobalArgs.CONTACT_STATE_NOT_GA_USER, null, null, null);
		} else {// check account existence by email
			AccountBasic account = accountService.queryAccount(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, contact.getEmail());

			if (account.getAccountId() != null && account.getAccountId().length() > 0) {// systemAccount!
				contact.setAccountId(account.getAccountId());
				logger.debug("Friend's myAccountId: " + account.getAccountId());
				if (contact.getState() == GlobalArgs.CONTACT_STATE_NOT_GA_USER)
					contact.setState(GlobalArgs.CONTACT_STATE_NOT_FRIEND);
				respCmd = new UploadContactResp(ErrorCode.SUCCESS, contact.getContactId(), contact.getState(),
						contact.getAccountId(), null, null);
			} else {
				respCmd = new UploadContactResp(ErrorCode.SUCCESS, contact.getContactId(),
						GlobalArgs.CONTACT_STATE_NOT_GA_USER, null, null, null);
			}
		}

		Contact contactExist = contactService.query(contact.getContactId());
		if (contactExist.getContactId() == null) {
			contactService.add(contact, currentTimestamp);
		} else {
			contactService.update(contact, currentTimestamp);
		}

		return respCmd;
	}

	private UploadContactReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(UploadContactAdapter.class);

}
