package com.redoct.ga.sup.message.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.InlinecastMessageServiceIf;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.message.cmd.MultcastApplyStateReq;
import com.redoct.ga.sup.message.cmd.MultcastApplyStateResp;

public class MultcastApplyStateAdapter
		extends SupReqCommand
{
	public MultcastApplyStateAdapter()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_APPLY_STATE_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new MultcastApplyStateReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		GaApplyStateNotify applyNotify = reqCmd.getMsg();

		try {
			InlinecastMessageServiceIf inlinecastMessageService = (InlinecastMessageServiceIf) context
					.getBean("inlinecastMessageService");

			inlinecastMessageService.multicast(context, applyNotify);

			MultcastApplyStateResp respCmd = new MultcastApplyStateResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			MultcastApplyStateResp respCmd = new MultcastApplyStateResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private MultcastApplyStateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MultcastApplyStateAdapter.class);

}
