package com.oct.ga.message.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.future.WriteFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.msg.QueryMessageBadgeNumberReq;
import com.oct.ga.comm.cmd.msg.QueryMessageBadgeNumberResp;
import com.oct.ga.comm.domain.MessageBadgeNumberJsonBean;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.msg.MsgExtend;
import com.oct.ga.comm.domain.msg.MsgLastCacheJsonBean;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaMessageService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryMessageBadgeNumberAdapter
		extends StpReqCommand
{
	public QueryMessageBadgeNumberAdapter()
	{
		super();

		this.setTag(Command.QUERY_MESSAGE_BADGE_NUMBER_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryMessageBadgeNumberReq().decode(tlv);

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		QueryMessageBadgeNumberResp respCmd = null;
		String accountId = this.getMyAccountId();
		int lastTryTime = reqCmd.getLastTryTime();
		int monthAgo = currentTimestamp - 2419200;
		if (monthAgo > lastTryTime)
			lastTryTime = monthAgo;

		try {
			GaMessageService messageService = (GaMessageService) context.getBean("gaMessageService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			MessageBadgeNumberJsonBean messageBadge = new MessageBadgeNumberJsonBean();

			List<MsgLastCacheJsonBean> rsChatNumbers = messageService.queryLastCaches(accountId, lastTryTime);
			List<MsgExtend> messageList = new ArrayList<MsgExtend>();

			for (MsgLastCacheJsonBean rsChatNumber : rsChatNumbers) {
				MsgExtend message = messageService.query(rsChatNumber.getMsgId());

				String groupName = groupService.queryGroupName(message.getChannelId());
				message.setChannelName(groupName);

				message.setCurrentTimestamp(currentTimestamp);

				if (message.getFromAccountId() != null) {
					try {
						AccountBasic account = accountService.queryAccount(message.getFromAccountId());
						message.setFromAccountName(account.getNickname());
						message.setFromAccountAvatarUrl(account.getAvatarUrl());
					} catch (Exception e) {
						logger.warn("ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
						continue;
					}
					
					messageList.add(message);
				}
			}

			messageBadge.setMessageList(messageList);
			messageBadge.setChatNumber(rsChatNumbers);

			respCmd = new QueryMessageBadgeNumberResp(sequence, ErrorCode.SUCCESS, currentTimestamp, messageBadge);
			TlvObject tlvResp = CommandParser.encode(respCmd);

			WriteFuture future = session.write(tlvResp);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (!future.isWritten()) {
				// The messsage couldn't be written out completely for
				// some reason. (e.g. Connection is closed)
				logger.warn("sessionId=["
						+ session.getId()
						+ "]|deviceId=["
						+ this.getMyDeviceId()
						+ "]|accountId=["
						+ this.getMyAccountId()
						+ "]|commandTag=["
						+ this.getTag()
						+ "]|ErrorCode=["
						+ ErrorCode.CONNECTION_CLOSED
						+ "]|couldn't be written out QueryMessageBadgeNumberResp completely for some reason.(e.g. Connection is closed)");
			}

			return null;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new QueryMessageBadgeNumberResp(sequence, ErrorCode.UNKNOWN_FAILURE, currentTimestamp, null);
			return respCmd;
		}
	}

	private QueryMessageBadgeNumberReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryMessageBadgeNumberAdapter.class);
}
