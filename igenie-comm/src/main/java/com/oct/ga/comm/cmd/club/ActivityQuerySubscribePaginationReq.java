package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityQuerySubscribePaginationReq
		extends ReqCommand
{
	public ActivityQuerySubscribePaginationReq()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_SUBSCRIBE_PAGINATION_REQ);
	}

	public ActivityQuerySubscribePaginationReq(short pageNum, short pageSize)
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
		TlvObject tPageNum = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.pageNum));
		TlvObject tPageSize = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.pageSize));

		TlvObject tlv = new TlvObject(this.getTag());

		tlv.push(tSequence);
		tlv.push(tPageNum);
		tlv.push(tPageSize);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityQuerySubscribePaginationReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tPageNum = tlv.getChild(i++);
		pageNum = TlvByteUtil.byte2Short(tPageNum.getValue());
		logger.debug("pageNum: " + pageNum);

		TlvObject tPageSize = tlv.getChild(i++);
		pageSize = TlvByteUtil.byte2Short(tPageSize.getValue());
		logger.debug("pageSize: " + pageSize);

		return this;
	}

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

	private final static Logger logger = LoggerFactory.getLogger(ActivityQuerySubscribePaginationReq.class);

}
