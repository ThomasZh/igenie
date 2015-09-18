package com.redoct.ga.sup.message.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.InlinecastMessageServiceIf;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.message.cmd.MultcastTaskLogReq;
import com.redoct.ga.sup.message.cmd.MultcastTaskLogResp;

public class MultcastMsgFlowAdapter
		extends SupReqCommand
{
	public MultcastMsgFlowAdapter()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_TASK_LOG_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new MultcastTaskLogReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		MsgFlowBasicInfo msg = reqCmd.getMsg();

		try {
			InlinecastMessageServiceIf inlinecastMessageService = (InlinecastMessageServiceIf) context
					.getBean("inlinecastMessageService");

			logger.debug("send msg to " + msg.getToActionId());
			inlinecastMessageService.multicast(context, msg);

			MultcastTaskLogResp respCmd = new MultcastTaskLogResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			MultcastTaskLogResp respCmd = new MultcastTaskLogResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private MultcastTaskLogReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MultcastMsgFlowAdapter.class);

}
