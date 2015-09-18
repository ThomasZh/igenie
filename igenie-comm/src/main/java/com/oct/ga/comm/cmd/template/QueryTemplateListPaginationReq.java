package com.oct.ga.comm.cmd.template;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryTemplateListPaginationReq
		extends ReqCommand
{
	public QueryTemplateListPaginationReq()
	{
		super();

		this.setTag(Command.QUERY_TEMPLATE_LIST_PAGINATION_REQ);
	}

	public QueryTemplateListPaginationReq(short type)
	{
		this();
		this.setTemplateType(type);
	}

	@Override
	public QueryTemplateListPaginationReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.QUERY_TEMPLATE_LIST_PAGINATION_REQ + ", child=6) to command");

		TlvParser.decodeChildren(tlv, 6);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tTemplateType = tlv.getChild(1);
		templateType = TlvByteUtil.byte2Short(tTemplateType.getValue());
		logger.debug("templateType: " + templateType);

		TlvObject tSupplierType = tlv.getChild(2);
		supplierType = TlvByteUtil.byte2Short(tSupplierType.getValue());
		logger.debug("supplierType: " + supplierType);

		TlvObject tAttachId = tlv.getChild(3);
		attachId = new String(tAttachId.getValue(), "UTF-8");
		logger.info("attachId: " + attachId);

		TlvObject tPageNum = tlv.getChild(4);
		pageNum = TlvByteUtil.byte2Short(tPageNum.getValue());
		logger.debug("pageNum: " + pageNum);

		TlvObject tPageSize = tlv.getChild(5);
		pageSize = TlvByteUtil.byte2Short(tPageSize.getValue());
		logger.debug("pageSize: " + pageSize);

		return this;
	}

	private short templateType;
	private short supplierType;
	private String attachId;
	private short pageNum;
	private short pageSize;

	public short getSupplierType()
	{
		return supplierType;
	}

	public void setSupplierType(short supplierType)
	{
		this.supplierType = supplierType;
	}

	public String getAttachId()
	{
		return attachId;
	}

	public void setAttachId(String attachId)
	{
		this.attachId = attachId;
	}

	public short getTemplateType()
	{
		return templateType;
	}

	public void setTemplateType(short templateType)
	{
		this.templateType = templateType;
	}

	public short getPageNum()
	{
		return pageNum;
	}

	public void setPageNum(short pageNum)
	{
		this.pageNum = pageNum;
	}

	public short getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(short pageSize)
	{
		this.pageSize = pageSize;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryTemplateListPaginationReq.class);
}
