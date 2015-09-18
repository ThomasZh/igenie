package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityQueryFutureFilterByLocPaginationReq
		extends ReqCommand
{
	public ActivityQueryFutureFilterByLocPaginationReq()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_FUTURE_FILTERBY_LOC_PAGINATION_REQ);
	}

	public ActivityQueryFutureFilterByLocPaginationReq(short pageNum, short pageSize)
	{
		this();

		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
	}

	public ActivityQueryFutureFilterByLocPaginationReq(int sequence, short pageNum, short pageSize)
	{
		this(pageNum, pageSize);

		this.setSequence(sequence);
	}

	public ActivityQueryFutureFilterByLocPaginationReq(int sequence, String locX, String locY, short pageNum,
			short pageSize)
	{
		this(sequence, pageNum, pageSize);

		this.setLocX(locX);
		this.setLocY(locY);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tLocX = new TlvObject(i++, locX);
		TlvObject tLocY = new TlvObject(i++, locY);
		TlvObject tPageNum = new TlvObject(i++, 2, TlvByteUtil.short2Byte(pageNum));
		TlvObject tPageSize = new TlvObject(i++, 2, TlvByteUtil.short2Byte(pageSize));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tLocX);
		tlv.push(tLocY);
		tlv.push(tPageNum);
		tlv.push(tPageSize);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityQueryFutureFilterByLocPaginationReq decode(TlvObject tlv)
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

		TlvObject tLocX = tlv.getChild(i++);
		locX = new String(tLocX.getValue(), "UTF-8");
		logger.debug("locX: " + locX);

		TlvObject tLocY = tlv.getChild(i++);
		locY = new String(tLocY.getValue(), "UTF-8");
		logger.debug("locY: " + locY);

		TlvObject tPageNum = tlv.getChild(i++);
		pageNum = TlvByteUtil.byte2Short(tPageNum.getValue());
		logger.debug("pageNum: " + pageNum);

		TlvObject tPageSize = tlv.getChild(i++);
		pageSize = TlvByteUtil.byte2Short(tPageSize.getValue());
		logger.debug("pageSize: " + pageSize);

		return this;
	}

	private String locX;
	private String locY;
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

	public String getLocX()
	{
		return locX;
	}

	public void setLocX(String locX)
	{
		this.locX = locX;
	}

	public String getLocY()
	{
		return locY;
	}

	public void setLocY(String locY)
	{
		this.locY = locY;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryFutureFilterByLocPaginationReq.class);

}
