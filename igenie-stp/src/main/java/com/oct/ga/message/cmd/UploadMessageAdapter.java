package com.oct.ga.message.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.msg.UploadMessageReq;
import com.oct.ga.comm.cmd.msg.UploadMessageResp;
import com.oct.ga.comm.domain.msg.MessageOriginalMulticast;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.message.SupMessageService;

public class UploadMessageAdapter
		extends StpReqCommand
{
	public UploadMessageAdapter()
	{
		super();

		this.setTag(Command.UPLOAD_MESSAGE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new UploadMessageReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String fromAccountId = this.getMyAccountId();
		// String deviceId = (String) session.getAttribute("deviceId");
		String fromAccountName = (String) session.getAttribute("accountName");
		String fromAccountAvatarUrl = (String) session.getAttribute("avatarUrl");
		String toAccountId = reqCmd.getToAccountId();
		String chatId = reqCmd.getChatId();

		MessageOriginalMulticast msg = new MessageOriginalMulticast();
		msg.set_id(reqCmd.getMsgId());
		msg.setFromAccountId(fromAccountId);
		msg.setFromAccountName(fromAccountName);
		msg.setFromAccountAvatarUrl(fromAccountAvatarUrl);
		msg.setContentType(reqCmd.getContentType());
		msg.setChannelType(reqCmd.getChannelType());
		msg.setChannelId(reqCmd.getChannelId());
		msg.setContent(reqCmd.getContent());
		msg.setAttachUrl(reqCmd.getAttachUrl());
		switch (msg.getChannelType()) {
		case GlobalArgs.CHANNEL_TYPE_TASK:
			chatId = msg.getChannelId();
			break;
		case GlobalArgs.CHANNEL_TYPE_QUESTION:
			break;
		case GlobalArgs.CHANNEL_TYPE_CREATE_QUESTION:
			chatId = toAccountId;
			break;
		}
		msg.setChatId(chatId);
		msg.setTimestamp(currentTimestamp);
		logger.debug("timestamp: " + currentTimestamp);

		try {
			SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");
			supMessageService.send(msg, currentTimestamp);
			logger.debug("send multcast msg from=[" + this.getMyAccountId() + "] to channel=[" + chatId + "]");
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			UploadMessageResp respCmd = new UploadMessageResp(ErrorCode.SUCCESS, msg.get_id(), currentTimestamp);
			respCmd.setSequence(sequence);
			return respCmd;
		}

		UploadMessageResp respCmd = new UploadMessageResp(ErrorCode.SUCCESS, msg.get_id(), currentTimestamp);
		respCmd.setSequence(sequence);
		return respCmd;
	}

	private UploadMessageReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(UploadMessageAdapter.class);
}
