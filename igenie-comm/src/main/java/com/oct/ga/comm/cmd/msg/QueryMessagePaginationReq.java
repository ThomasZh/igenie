package com.oct.ga.comm.cmd.msg;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMessagePaginationReq
		extends ReqCommand
{
	public QueryMessagePaginationReq()
	{
		super();

		this.setTag(Command.QUERY_MESSAGE_PAGINATION_REQ);
	}

	public QueryMessagePaginationReq(String chatId, short pageNum, short pageSize)
	{
		this();

		this.setChatId(chatId);
		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
	}

	public QueryMessagePaginationReq(int sequence, String chatId, short pageNum, short pageSize)
	{
		this(chatId, pageNum, pageSize);

		this.setSequence(sequence);
	}

	@Override
	public QueryMessagePaginationReq decode(TlvObject tlv)
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

		TlvObject tChatId = tlv.getChild(i++);
		chatId = new String(tChatId.getValue(), "UTF-8");
		logger.debug("chatId: " + chatId);

		TlvObject tPageNum = tlv.getChild(i++);
		pageNum = TlvByteUtil.byte2Short(tPageNum.getValue());
		logger.debug("pageNum: " + pageNum);

		TlvObject tPageSize = tlv.getChild(i++);
		pageSize = TlvByteUtil.byte2Short(tPageSize.getValue());
		logger.debug("pageSize: " + pageSize);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tChatId = new TlvObject(i++, chatId);
		TlvObject tPageNum = new TlvObject(i++, 2, TlvByteUtil.short2Byte(pageNum));
		TlvObject tPageSize = new TlvObject(i++, 2, TlvByteUtil.short2Byte(pageSize));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tChatId);
		tlv.push(tPageNum);
		tlv.push(tPageSize);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String chatId;
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

	public String getChatId()
	{
		return chatId;
	}

	public void setChatId(String chatId)
	{
		this.chatId = chatId;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryMessagePaginationReq.class);
}
