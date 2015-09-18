package com.oct.ga.template.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.template.QueryTemplateListPaginationReq;
import com.oct.ga.comm.cmd.template.QueryTemplateListPaginationResp;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.template.GaTemplateMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaTemplateService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class QueryTemplateListPaginationAdapter
		extends StpReqCommand
{
	public QueryTemplateListPaginationAdapter()
	{
		super();

		this.setTag(Command.QUERY_TEMPLATE_LIST_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryTemplateListPaginationReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		QueryTemplateListPaginationResp respCmd = null;
		short templateType = reqCmd.getTemplateType();
		short supplierType = reqCmd.getSupplierType();
		String attachId = reqCmd.getAttachId();
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaTemplateService templateService = (GaTemplateService) context.getBean("gaTemplateService");

			if (supplierType == GlobalArgs.TEMPLATE_SUPPLIER_TYPE_MINE)
				attachId = this.getMyAccountId();

			Page<GaTemplateMaster> templatePagination = templateService.queryTemplatePagination(templateType,
					supplierType, attachId, pageNum, pageSize);
			List<GaTemplateMaster> array = templatePagination.getPageItems();
			JSONArray jsonArray = JSONArray.fromObject(array);
			String json = jsonArray.toString();

			respCmd = new QueryTemplateListPaginationResp(ErrorCode.SUCCESS, json);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new QueryTemplateListPaginationResp(ErrorCode.UNKNOWN_FAILURE, null);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private QueryTemplateListPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryTemplateListPaginationAdapter.class);
}
