package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityQuerySubscribeFilterByTimeRangePaginationReq
		extends ReqCommand
{
	public ActivityQuerySubscribeFilterByTimeRangePaginationReq()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_SUBSCRIBE_FILTER_BY_TIME_RANGE_PAGINATION_REQ);
	}

	public ActivityQuerySubscribeFilterByTimeRangePaginationReq(short pageNum, short pageSize)
	{
		this();

		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
	}

	@Override
	public TlvObject encode()
	{
		int i = 0;
		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tStartTime = new TlvObject(i++, 4, TlvByteUtil.int2Byte(startTime));
		TlvObject tEndTime = new TlvObject(i++, 4, TlvByteUtil.int2Byte(endTime));
		TlvObject tPageNum = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.pageNum));
		TlvObject tPageSize = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.pageSize));

		TlvObject tlv = new TlvObject(this.getTag());

		tlv.push(tSequence);
		tlv.push(tStartTime);
		tlv.push(tEndTime);
		tlv.push(tPageNum);
		tlv.push(tPageSize);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityQuerySubscribeFilterByTimeRangePaginationReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 5;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tStartTime = tlv.getChild(i++);
		startTime = TlvByteUtil.byte2Int(tStartTime.getValue());
		logger.debug("startTime: " + startTime);

		TlvObject tEndTime = tlv.getChild(i++);
		endTime = TlvByteUtil.byte2Int(tEndTime.getValue());
		logger.debug("endTime: " + endTime);

		TlvObject tPageNum = tlv.getChild(i++);
		pageNum = TlvByteUtil.byte2Short(tPageNum.getValue());
		logger.debug("pageNum: " + pageNum);

		TlvObject tPageSize = tlv.getChild(i++);
		pageSize = TlvByteUtil.byte2Short(tPageSize.getValue());
		logger.debug("pageSize: " + pageSize);

		return this;
	}

	private int startTime;
	private int endTime;
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

	public int getStartTime()
	{
		return startTime;
	}

	public void setStartTime(int startTime)
	{
		this.startTime = startTime;
	}

	public int getEndTime()
	{
		return endTime;
	}

	public void setEndTime(int endTime)
	{
		this.endTime = endTime;
	}

	private final static Logger logger = LoggerFactory
			.getLogger(ActivityQuerySubscribeFilterByTimeRangePaginationReq.class);

}
