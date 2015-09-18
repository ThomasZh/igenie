package com.redoct.ga.sup.message.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.MessageOriginalMulticast;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaMessageService;
import com.oct.ga.service.InlinecastMessageServiceIf;
import com.redoct.ga.sup.SupCommandParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.message.cmd.MultcastMessageReq;
import com.redoct.ga.sup.message.cmd.MultcastMessageResp;

public class MultcastMessageAdapter
		extends SupReqCommand
{
	public MultcastMessageAdapter()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_MESSAGE_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new MultcastMessageReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		MessageOriginalMulticast msg = reqCmd.getMsg();
		MultcastMessageResp respCmd = null;
		String fromAccountId = msg.getFromAccountId();
		String chatId = msg.getChatId();
		String toAccountId = null;

		switch (msg.getChannelType()) {
		case GlobalArgs.CHANNEL_TYPE_CREATE_QUESTION:
			toAccountId = chatId;
			chatId = EcryptUtil.md5ChatId(fromAccountId, toAccountId);
			break;
		case GlobalArgs.CHANNEL_TYPE_TASK:
		case GlobalArgs.CHANNEL_TYPE_QUESTION:
			break;
		}
		msg.setChatId(chatId);

		try {
			logger.debug("msgId: " + msg.get_id());
			logger.debug("fromAccountId: " + msg.getFromAccountId());
			logger.debug("fromAccountName: " + msg.getFromAccountName());
			logger.debug("fromAccountAvatarUrl: " + msg.getFromAccountAvatarUrl());
			logger.debug("channelType: " + msg.getChannelType());
			logger.debug("channelId: " + msg.getChannelId());
			logger.debug("chatId: " + msg.getChatId());
			logger.debug("contentType: " + msg.getContentType());
			logger.debug("content: " + msg.getContent());
			logger.debug("attachUrl: " + msg.getAttachUrl());

			IoSession session = this.getIoSession();
			respCmd = new MultcastMessageResp(this.getSequence(), ErrorCode.SUCCESS);
			TlvObject tResp = null;
			try {
				tResp = SupCommandParser.encode(respCmd);
			} catch (Exception e) {
				logger.error("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.ENCODING_FAILURE + "]" + LogErrorMessage.getFullInfo(e));
				respCmd = new MultcastMessageResp(this.getSequence(), ErrorCode.ENCODING_FAILURE);
				return respCmd;
			}

			WriteFuture future = session.write(tResp);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (!future.isWritten()) {
				// The messsage couldn't be written out completely for
				// some reason. (e.g. Connection is closed)
				logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.CONNECTION_CLOSED
						+ "]|couldn't be written out resp completely for some reason.(e.g. Connection is closed)");

				session.close(true);
			} else {
				logger.info("mesg from=[" + fromAccountId + "] to chat=[" + chatId + "]");
			}

			int currentTimestamp = msg.getTimestamp();
			logger.debug("timestamp: " + currentTimestamp);
			try {
				GaMessageService messageService = (GaMessageService) context.getBean("gaMessageService");
				InlinecastMessageServiceIf inlinecastMessageService = (InlinecastMessageServiceIf) context
						.getBean("inlinecastMessageService");
				GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
				GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

				// Store this message into
				// database.table:MessageOriginalMulticast
				messageService.addOriginal(msg, currentTimestamp);

				switch (msg.getChannelType()) {
				case GlobalArgs.CHANNEL_TYPE_CREATE_QUESTION:
					if (!groupService.isActive(chatId)) {
						groupService.createGroup(chatId, "咨询", msg.getChannelType(), currentTimestamp, fromAccountId);
						groupService.joinAsLeader(chatId, toAccountId, currentTimestamp - 1);
						groupService.joinAsMember(chatId, fromAccountId, currentTimestamp);
					}
					break;
				case GlobalArgs.CHANNEL_TYPE_QUESTION:
				case GlobalArgs.CHANNEL_TYPE_TASK:
				default:
					break;
				}

				String channelName = groupService.queryGroupName(msg.getChannelId());
				List<String> ids = groupService.queryActiveMemberIds(chatId);
				for (String memberId : ids) {
					MessageInlinecast message = new MessageInlinecast();// msg.copy();
					message.set_id(msg.get_id());
					message.setContentType(msg.getContentType());
					message.setChannelType(msg.getChannelType());
					message.setChannelId(msg.getChannelId());
					message.setChannelName(channelName);
					message.setChatId(msg.getChatId());
					message.setTimestamp(currentTimestamp);
					message.setContent(msg.getContent());
					message.setAttachUrl(msg.getAttachUrl());
					message.setFromAccountId(fromAccountId);
					message.setFromAccountName(msg.getFromAccountName());
					message.setFromAccountAvatarUrl(msg.getFromAccountAvatarUrl());
					message.setToAccountId(memberId);

					// don't send to myself
					if (message.getFromAccountId().equals(message.getToAccountId())) {
						message.setSyncState(GlobalArgs.SYNC_STATE_RECEIVED);
						logger.debug("Not send to member himself(" + message.getToAccountId() + ") a message.");

						messageService.addExtend(message.get_id(), message.getToAccountId(), message.getChatId(),
								message.getSyncState(), currentTimestamp);
					} else {
						logger.debug("Send to member(" + message.getToAccountId() + ") a message.");

						message.setSyncState(GlobalArgs.SYNC_STATE_NOT_RECEIVED);
						messageService.addExtend(message.get_id(), message.getToAccountId(), message.getChatId(),
								message.getSyncState(), currentTimestamp);

						short badgeNum = badgeNumService.countMessageNum(message.getToAccountId());
						badgeNumService.modifyMessageNum(message.getToAccountId(), badgeNum);

						short channelBadgeNum = messageService.countCacheBadgeNum(msg.getChatId(),
								message.getToAccountId());
						// Logic: add or update into cache table
						if (messageService.isExistCache(msg.getChatId(), message.getToAccountId())) {
							messageService.updateCache(msg.getChatId(), message.getToAccountId(), message.get_id(),
									channelBadgeNum, currentTimestamp);
						} else {
							messageService.addCache(msg.getChatId(), message.getToAccountId(), message.get_id(),
									channelBadgeNum, currentTimestamp);
						}

						try {
							inlinecastMessageService.multicast(context, message);
						} catch (Exception e) {
							logger.error("multicast message error: " + LogErrorMessage.getFullInfo(e));
						}
					}
				}
			} catch (Exception e) {
				logger.error("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
			}

			return null;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));
			return null;
		}
	}

	private MultcastMessageReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MultcastMessageAdapter.class);

}
