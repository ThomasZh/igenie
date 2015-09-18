package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

/**
 * query my created history(start<today) logs by pagination.
 * 
 * @author thomas
 * 
 */
public class ActivityQueryAccountHistoryPaginationReq
		extends ReqCommand
{
	public ActivityQueryAccountHistoryPaginationReq()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_ACCOUNT_HISTORY_PAGINATION_REQ);
	}

	public ActivityQueryAccountHistoryPaginationReq(short pageNum, short pageSize)
	{
		this();

		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
	}

	public ActivityQueryAccountHistoryPaginationReq(int sequence, short pageNum, short pageSize)
	{
		this(pageNum, pageSize);

		this.setSequence(sequence);
	}

	public ActivityQueryAccountHistoryPaginationReq(int sequence, String accountId, short pageNum, short pageSize)
	{
		this(sequence, pageNum, pageSize);

		this.setAccountId(accountId);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tAccountId = new TlvObject(i++, accountId);
		TlvObject tPageNum = new TlvObject(i++, 2, TlvByteUtil.short2Byte(pageNum));
		TlvObject tPageSize = new TlvObject(i++, 2, TlvByteUtil.short2Byte(pageSize));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tAccountId);
		tlv.push(tPageNum);
		tlv.push(tPageSize);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityQueryAccountHistoryPaginationReq decode(TlvObject tlv)
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

		TlvObject tAccountId = tlv.getChild(i++);
		accountId = new String(tAccountId.getValue(), "UTF-8");
		logger.debug("accountId: " + accountId);

		TlvObject tPageNum = tlv.getChild(i++);
		pageNum = TlvByteUtil.byte2Short(tPageNum.getValue());
		logger.debug("pageNum: " + pageNum);

		TlvObject tPageSize = tlv.getChild(i++);
		pageSize = TlvByteUtil.byte2Short(tPageSize.getValue());
		logger.debug("pageSize: " + pageSize);

		return this;
	}

	private String accountId;
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

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryAccountHistoryPaginationReq.class);

}
