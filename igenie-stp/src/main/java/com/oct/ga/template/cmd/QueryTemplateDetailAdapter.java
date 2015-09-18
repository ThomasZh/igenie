package com.oct.ga.template.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.template.QueryTemplateDetailReq;
import com.oct.ga.comm.cmd.template.QueryTemplateDetailResp;
import com.oct.ga.comm.domain.template.TemplateDefineJsonBean;
import com.oct.ga.comm.parser.JsonParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaTemplateService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class QueryTemplateDetailAdapter
		extends StpReqCommand
{
	public QueryTemplateDetailAdapter()
	{
		super();

		this.setTag(Command.QUERY_TEMPLATE_DETAIL_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryTemplateDetailReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		QueryTemplateDetailResp respCmd = null;
		String templateId = reqCmd.getTemplateId();

		try {
			GaTemplateService templateService = (GaTemplateService) context.getBean("gaTemplateService");

			TemplateDefineJsonBean templateInfo = templateService.queryMaxVersion(templateId);
			List<TemplateDefineJsonBean> childrenTemplateInfoArray = templateService.queryChildren(templateId);
			templateInfo.setChildren(childrenTemplateInfoArray);

			String jsonStr = JsonParser.template2Json(templateInfo);
			logger.debug("json: " + jsonStr);
			respCmd = new QueryTemplateDetailResp(ErrorCode.SUCCESS, jsonStr);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new QueryTemplateDetailResp(ErrorCode.UNKNOWN_FAILURE, null);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private QueryTemplateDetailReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryTemplateDetailAdapter.class);
}
