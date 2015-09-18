package com.oct.ga.comm.cmd.moment;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMomentFavoritePaginationReq
		extends ReqCommand
{
	public QueryMomentFavoritePaginationReq()
	{
		super();

		this.setTag(Command.QUERY_MOMENT_FAVORITE_PAGINATION_REQ);
	}

	public QueryMomentFavoritePaginationReq(String momentId, short pageNum, short pageSize)
	{
		this();

		this.setMomentId(momentId);
		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
	}

	public QueryMomentFavoritePaginationReq(int sequence, String momentId, short pageNum, short pageSize)
	{
		this(momentId, pageNum, pageSize);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tMomentId = new TlvObject(i++, momentId);
		TlvObject tPageNum = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getPageNum()));
		TlvObject tPageSize = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getPageSize()));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tMomentId);
		tlv.push(tPageNum);
		tlv.push(tPageSize);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public QueryMomentFavoritePaginationReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tMomentId = tlv.getChild(i++);
		momentId = new String(tMomentId.getValue(), "UTF-8");
		logger.debug("momentId: " + momentId);

		TlvObject tPageNum = tlv.getChild(2);
		pageNum = TlvByteUtil.byte2Short(tPageNum.getValue());
		logger.debug("pageNum: " + pageNum);

		TlvObject tPageSize = tlv.getChild(3);
		pageSize = TlvByteUtil.byte2Short(tPageSize.getValue());
		logger.debug("pageSize: " + pageSize);

		return this;
	}

	private String momentId;
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

	public String getMomentId()
	{
		return momentId;
	}

	public void setMomentId(String taskId)
	{
		this.momentId = taskId;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryMomentFavoritePaginationReq.class);

}
