package com.oct.ga.comm.cmd.moment;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvObjectExt;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMomentPaginationReq
		extends ReqCommand
{
	public QueryMomentPaginationReq()
	{
		super();

		this.setTag(Command.QUERY_MOMENT_PAGINATION_REQ);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObjectExt root = new TlvObjectExt(Command.QUERY_MOMENT_PAGINATION_REQ);

		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(this.sequence));
		TlvObject tTaskId = new TlvObject(2, this.getTaskId());
		TlvObject tPageNum = new TlvObject(3, 2, TlvByteUtil.short2Byte(this.getPageNum()));
		TlvObject tPageSize = new TlvObject(4, 2, TlvByteUtil.short2Byte(this.getPageSize()));

		root.plus(tSequence).plus(tTaskId).plus(tPageNum).plus(tPageSize);

		return root;
	}

	@Override
	public QueryMomentPaginationReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.QUERY_MOMENT_PAGINATION_REQ + ", child=4) to command");

		TlvParser.decodeChildren(tlv, 4);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tTaskId = tlv.getChild(1);
		taskId = new String(tTaskId.getValue(), "UTF-8");
		logger.debug("taskId: " + taskId);

		TlvObject tPageNum = tlv.getChild(2);
		pageNum = TlvByteUtil.byte2Short(tPageNum.getValue());
		logger.debug("pageNum: " + pageNum);

		TlvObject tPageSize = tlv.getChild(3);
		pageSize = TlvByteUtil.byte2Short(tPageSize.getValue());
		logger.debug("pageSize: " + pageSize);

		return this;
	}

	private String taskId;
	private short pageNum;
	private short pageSize;

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

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryMomentPaginationReq.class);

}
