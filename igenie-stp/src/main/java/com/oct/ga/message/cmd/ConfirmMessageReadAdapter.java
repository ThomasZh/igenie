package com.oct.ga.message.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.msg.ConfirmMessageReadReq;
import com.oct.ga.comm.cmd.msg.ConfirmMessageReadResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaMessageService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ConfirmMessageReadAdapter
		extends StpReqCommand
{
	public ConfirmMessageReadAdapter()
	{
		super();

		this.setTag(Command.CONFIRM_MESSAGE_READ_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ConfirmMessageReadReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String chatId = reqCmd.getChatId();

		try {
			GaMessageService messageService = (GaMessageService) context.getBean("gaMessageService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

			int rows = messageService
					.batchUpdateMessageToReceviedState(chatId, this.getMyAccountId(), currentTimestamp);
			logger.debug("batchUpdateMessageToReceviedState effect rows= " + rows);

			short num = badgeNumService.countMessageNum(this.getMyAccountId());
			badgeNumService.modifyMessageNum(this.getMyAccountId(), num);

			short channelBadgeNum = messageService.countCacheBadgeNum(chatId, this.getMyAccountId());
			messageService.updateCacheBadgeNum(chatId, this.getMyAccountId(), channelBadgeNum, currentTimestamp);

			ConfirmMessageReadResp respCmd = new ConfirmMessageReadResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ConfirmMessageReadResp respCmd = new ConfirmMessageReadResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ConfirmMessageReadReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ConfirmMessageReadAdapter.class);

}
