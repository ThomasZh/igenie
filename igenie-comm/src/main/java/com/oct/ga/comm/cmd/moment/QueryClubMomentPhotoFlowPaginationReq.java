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

public class QueryClubMomentPhotoFlowPaginationReq
		extends ReqCommand
{
	public QueryClubMomentPhotoFlowPaginationReq()
	{
		super();

		this.setTag(Command.QUERY_CLUB_MOMENT_PHOTOFLOW_PAGINATION_REQ);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObjectExt root = new TlvObjectExt(Command.QUERY_CLUB_MOMENT_PHOTOFLOW_PAGINATION_REQ);

		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(this.sequence));
		TlvObject tClubId = new TlvObject(2, this.getClubId());
		TlvObject tPageNum = new TlvObject(3, 2, TlvByteUtil.short2Byte(this.getPageNum()));
		TlvObject tPageSize = new TlvObject(4, 2, TlvByteUtil.short2Byte(this.getPageSize()));

		root.plus(tSequence).plus(tClubId).plus(tPageNum).plus(tPageSize);

		return root;
	}

	@Override
	public QueryClubMomentPhotoFlowPaginationReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.QUERY_CLUB_MOMENT_PHOTOFLOW_PAGINATION_REQ + ", child=4) to command");

		TlvParser.decodeChildren(tlv, 4);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tClubId = tlv.getChild(1);
		clubId = new String(tClubId.getValue(), "UTF-8");
		logger.debug("clubId: " + clubId);

		TlvObject tPageNum = tlv.getChild(2);
		pageNum = TlvByteUtil.byte2Short(tPageNum.getValue());
		logger.debug("pageNum: " + pageNum);

		TlvObject tPageSize = tlv.getChild(3);
		pageSize = TlvByteUtil.byte2Short(tPageSize.getValue());
		logger.debug("pageSize: " + pageSize);

		return this;
	}

	private String clubId;
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

	public String getClubId()
	{
		return clubId;
	}

	public void setClubId(String clubId)
	{
		this.clubId = clubId;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryClubMomentPhotoFlowPaginationReq.class);

}
