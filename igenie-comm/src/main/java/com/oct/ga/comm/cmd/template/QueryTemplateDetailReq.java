package com.oct.ga.comm.cmd.template;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryTemplateDetailReq
		extends ReqCommand
{
	public QueryTemplateDetailReq()
	{
		super();

		this.setTag(Command.QUERY_TEMPLATE_DETAIL_REQ);
	}

	@Override
	public QueryTemplateDetailReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.QUERY_TEMPLATE_DETAIL_REQ + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tTemplateId = tlv.getChild(1);
		templateId = new String(tTemplateId.getValue(), "UTF-8");
		logger.debug("templateId: " + templateId);

		return this;
	}

	private String templateId;

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryTemplateDetailReq.class);
}
