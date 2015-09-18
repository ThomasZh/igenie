package com.oct.ga.publish.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.publish.QueryLocHotReq;
import com.oct.ga.comm.cmd.publish.QueryLocHotResp;
import com.oct.ga.comm.domain.publish.GaPublishHotLoc;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaPublishService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class QueryLocHotAdapter
		extends StpReqCommand
{
	public QueryLocHotAdapter()
	{
		super();

		this.setTag(Command.PUBLISH_QUERY_LOC_HOT_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryLocHotReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		QueryLocHotResp respCmd = null;
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaPublishService publishService = (GaPublishService) context.getBean("gaPublishService");

			List<GaPublishHotLoc> array = publishService.queryHotLocs(pageNum, pageSize);

			respCmd = new QueryLocHotResp(sequence, ErrorCode.SUCCESS, array);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new QueryLocHotResp(sequence, ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private QueryLocHotReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryLocHotAdapter.class);
}
