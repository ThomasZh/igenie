package com.oct.ga.comm.cmd.task;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.cmd.msg.QueryMessagePaginationResp;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryTaskActivityPaginationReq
		extends ReqCommand
{
	public QueryTaskActivityPaginationReq()
	{
		super();

		this.setTag(Command.QUERY_TASK_ACTIVITY_PAGINATION_REQ);
	}

	@Override
	public QueryTaskActivityPaginationReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.QUERY_TASK_ACTIVITY_PAGINATION_REQ + ", child=4) to command");

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

	private final static Logger logger = LoggerFactory.getLogger(QueryMessagePaginationResp.class);

}
