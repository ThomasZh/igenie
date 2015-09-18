package com.oct.ga.gatekeeper.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.gatekeeper.QueryStpStatesReq;
import com.oct.ga.comm.cmd.gatekeeper.QueryStpStatesResp;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.gatekeeper.ServerListCache;
import com.oct.ga.stp.cmd.StpReqCommand;

public class QueryStpStatesAdapter
		extends StpReqCommand
{
	public QueryStpStatesAdapter()
	{
		super();

		this.setTag(Command.GK_QUERY_STP_STATES_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryStpStatesReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		QueryStpStatesResp respCmd = null;

		try {
			ServerListCache serverList = GenericSingleton.getInstance(ServerListCache.class);
			List<StpServerInfoJsonBean> stpArray = serverList.getAll();

			JSONArray jsonArray = JSONArray.fromObject(stpArray);
			String json = jsonArray.toString();

			respCmd = new QueryStpStatesResp(ErrorCode.SUCCESS, json);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[]|accountId=[]|commandTag=[" + this.getTag()
					+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new QueryStpStatesResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private QueryStpStatesReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryStpStatesAdapter.class);

}
