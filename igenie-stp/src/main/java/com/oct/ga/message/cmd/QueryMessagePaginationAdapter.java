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
import com.oct.ga.comm.cmd.msg.QueryMessagePaginationReq;
import com.oct.ga.comm.cmd.msg.QueryMessagePaginationResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.msg.MsgExtend;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaMessageService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryMessagePaginationAdapter
		extends StpReqCommand
{
	public QueryMessagePaginationAdapter()
	{
		super();

		this.setTag(Command.QUERY_MESSAGE_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryMessagePaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String chatId = reqCmd.getChatId();
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaMessageService messageService = (GaMessageService) context.getBean("gaMessageService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			List<MsgExtend> messages = messageService.queryPagination(chatId, this.getMyAccountId(), pageNum, pageSize);
			List<MsgExtend> msgs = new ArrayList<MsgExtend>();

			for (int i = messages.size() - 1; i >= 0; i--) {
				MsgExtend msg = messages.get(i);

				msg.setCurrentTimestamp(currentTimestamp);
				msg.setChatId(chatId);

				String groupName = groupService.queryGroupName(msg.getChannelId());
				msg.setChannelName(groupName);

				if (msg.getFromAccountId() != null) {
					try {
						AccountBasic account = accountService.queryAccount(msg.getFromAccountId());
						msg.setFromAccountName(account.getNickname());
						msg.setFromAccountAvatarUrl(account.getAvatarUrl());
					} catch (Exception e) {
						continue;
					}

					msgs.add(msg);
				}
			}

			QueryMessagePaginationResp respCmd = new QueryMessagePaginationResp(sequence, ErrorCode.SUCCESS, msgs);
			TlvObject tlvResp = CommandParser.encode(respCmd);

			WriteFuture future = session.write(tlvResp);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (future.isWritten()) {
				GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

				int rows = messageService.batchUpdateMessageToReceviedState(chatId, this.getMyAccountId(),
						currentTimestamp);
				logger.debug("batchUpdateMessageToReceviedState effect rows= " + rows);

				short num = badgeNumService.countMessageNum(this.getMyAccountId());
				badgeNumService.modifyMessageNum(this.getMyAccountId(), num);

				short channelBadgeNum = messageService.countCacheBadgeNum(chatId, this.getMyAccountId());
				messageService.updateCacheBadgeNum(chatId, this.getMyAccountId(), channelBadgeNum, currentTimestamp);
			} else {
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
						+ "]|couldn't be written out QueryMessagePaginationResp completely for some reason.(e.g. Connection is closed)");
			}

			// Warning: OldStpEventHandler do not response anything.
			return null;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryMessagePaginationResp respCmd = new QueryMessagePaginationResp(sequence, ErrorCode.UNKNOWN_FAILURE,
					null);
			return respCmd;
		}
	}

	private QueryMessagePaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryMessagePaginationAdapter.class);
}
