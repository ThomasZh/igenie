package com.redoct.ga.sup.message.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.InlinecastMessageServiceIf;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.message.cmd.MultcastActivityJoinReq;
import com.redoct.ga.sup.message.cmd.MultcastActivityJoinResp;

public class MultcastActivityJoinAdapter
		extends SupReqCommand
{
	public MultcastActivityJoinAdapter()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_ACTIVITY_JOIN_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new MultcastActivityJoinReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String groupName = reqCmd.getGroupName();
		String leaderId = reqCmd.getLeaderId();
		String fromAccountName = reqCmd.getFromAccountName();
		int timestamp = reqCmd.getTimestamp();

		try {
			InlinecastMessageServiceIf inlinecastMessageService = (InlinecastMessageServiceIf) context
					.getBean("inlinecastMessageService");

			inlinecastMessageService.multicast(context, groupName, leaderId, fromAccountName, timestamp);

			MultcastActivityJoinResp respCmd = new MultcastActivityJoinResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			MultcastActivityJoinResp respCmd = new MultcastActivityJoinResp(this.getSequence(),
					ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private MultcastActivityJoinReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MultcastActivityJoinAdapter.class);

}
