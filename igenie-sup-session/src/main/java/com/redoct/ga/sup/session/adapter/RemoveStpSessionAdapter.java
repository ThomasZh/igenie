package com.redoct.ga.sup.session.adapter;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.session.SessionCacheManager;
import com.redoct.ga.sup.session.cmd.RemoveStpSessionReq;
import com.redoct.ga.sup.session.cmd.RemoveStpSessionResp;

public class RemoveStpSessionAdapter
		extends SupReqCommand
{
	public RemoveStpSessionAdapter()
	{
		super();

		this.setTag(SupCommandTag.QUERY_STP_SESSION_REQ);
	}

	@Override
	public SupReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new RemoveStpSessionReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		String accountId = reqCmd.getAccountId();
		RemoveStpSessionResp respCmd = null;

		try {
			SessionCacheManager sessionCacheManager = GenericSingleton.getInstance(SessionCacheManager.class);

			String sessionTicket = sessionCacheManager.getSessionTicket(accountId);
			sessionCacheManager.removeStpSession(sessionTicket);
			sessionCacheManager.removeSessionTicket(accountId);
			
			respCmd = new RemoveStpSessionResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("SupCommandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new RemoveStpSessionResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private RemoveStpSessionReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(RemoveStpSessionAdapter.class);

}
