package com.oct.ga.comm.cmd.inlinecast;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class InlinecastMessageReq
		extends ReqCommand
{
	public InlinecastMessageReq()
	{
		this.setTag(Command.INLINECAST_MESSAGE_REQ);
	}

	public InlinecastMessageReq(MessageInlinecast msg)
	{
		this();

		this.setMessage(msg);
	}

	@Override
	public InlinecastMessageReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 16;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tMessageId = tlv.getChild(i++);
		String messageId = new String(tMessageId.getValue(), "UTF-8");
		logger.debug("messageId: " + messageId);

		TlvObject tContentType = tlv.getChild(i++);
		short contentType = TlvByteUtil.byte2Short(tContentType.getValue());
		logger.debug("contentType: " + contentType);

		TlvObject tFromAccountId = tlv.getChild(i++);
		String fromAccountId = new String(tFromAccountId.getValue(), "UTF-8");
		logger.debug("fromAccountId: " + fromAccountId);

		TlvObject tFromAccountName = tlv.getChild(i++);
		String fromAccountName = new String(tFromAccountName.getValue(), "UTF-8");
		logger.debug("fromAccountName: " + fromAccountName);

		TlvObject tFromAccountAvatarUrl = tlv.getChild(i++);
		String fromAccountAvatarUrl = new String(tFromAccountAvatarUrl.getValue(), "UTF-8");
		logger.debug("fromAccountAvatarUrl: " + fromAccountAvatarUrl);

		TlvObject tTimestamp = tlv.getChild(i++);
		int timestamp = TlvByteUtil.byte2Int(tTimestamp.getValue());
		logger.debug("timestamp: " + timestamp);

		TlvObject tCurrentTimestamp = tlv.getChild(i++);
		currentTimestamp = TlvByteUtil.byte2Int(tCurrentTimestamp.getValue());
		logger.debug("currentTimestamp: " + currentTimestamp);

		TlvObject tChannelType = tlv.getChild(i++);
		short channelType = TlvByteUtil.byte2Short(tChannelType.getValue());
		logger.debug("channelType: " + channelType);

		TlvObject tChannelId = tlv.getChild(i++);
		String channelId = new String(tChannelId.getValue(), "UTF-8");
		logger.debug("channelId: " + channelId);

		TlvObject tChannelName = tlv.getChild(i++);
		String channelName = new String(tChannelName.getValue(), "UTF-8");
		logger.debug("channelName: " + channelName);

		TlvObject tChatId = tlv.getChild(i++);
		String chatId = new String(tChatId.getValue(), "UTF-8");
		logger.debug("chatId: " + chatId);

		TlvObject tContent = tlv.getChild(i++);
		String content = new String(tContent.getValue(), "UTF-8");
		logger.debug("content: " + content);

		TlvObject tAttachUrl = tlv.getChild(i++);
		String attachUrl = new String(tAttachUrl.getValue(), "UTF-8");
		logger.debug("attachUrl: " + attachUrl);

		TlvObject tToAccountlId = tlv.getChild(i++);
		String toAccountlId = new String(tToAccountlId.getValue(), "UTF-8");
		logger.debug("toAccountlId: " + toAccountlId);

		TlvObject tToAccountName = tlv.getChild(i++);
		String toAccountName = new String(tToAccountName.getValue(), "UTF-8");
		logger.debug("toAccountName: " + toAccountName);

		TlvObject tIoSessionId = tlv.getChild(i++);
		long ioSessionId = TlvByteUtil.byte2Long(tIoSessionId.getValue());
		logger.debug("ioSessionId: " + ioSessionId);

		msg.set_id(messageId);
		msg.setContentType(contentType);
		msg.setFromAccountId(fromAccountId);
		msg.setFromAccountName(fromAccountName);
		msg.setFromAccountAvatarUrl(fromAccountAvatarUrl);
		msg.setTimestamp(timestamp);
		msg.setCurrentTimestamp(currentTimestamp);
		msg.setChannelType(channelType);
		msg.setChannelId(channelId);
		msg.setChannelName(channelName);
		msg.setChatId(chatId);
		msg.setContent(content);
		msg.setAttachUrl(attachUrl);
		msg.setToAccountId(toAccountlId);
		msg.setToAccountName(toAccountName);
		msg.setReciverIoSessionId(ioSessionId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tMessageId = new TlvObject(i++, msg.get_id());
		TlvObject tMessageType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(msg.getContentType()));
		TlvObject tFromAccountId = new TlvObject(i++, msg.getFromAccountId());
		TlvObject tFromAccountName = new TlvObject(i++, msg.getFromAccountName());
		TlvObject tFromAccountAvatarUrl = new TlvObject(i++, msg.getFromAccountAvatarUrl());
		TlvObject tTimestamp = new TlvObject(i++, 4, TlvByteUtil.int2Byte(msg.getTimestamp()));
		// in order for client modify deltTime between server.
		TlvObject tCurrentTimestamp = new TlvObject(i++, 4, TlvByteUtil.int2Byte(DatetimeUtil.currentTimestamp()));
		TlvObject tChannelType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(msg.getChannelType()));
		TlvObject tChannelId = new TlvObject(i++, msg.getChannelId());
		TlvObject tChannelName = new TlvObject(i++, msg.getChannelName());
		TlvObject tChatId = new TlvObject(i++, msg.getChatId());
		TlvObject tTxt = new TlvObject(i++, msg.getContent());
		TlvObject tAttachUrl = new TlvObject(i++, msg.getAttachUrl());
		TlvObject tToAccountlId = new TlvObject(i++, msg.getToAccountId());
		TlvObject tToAccountName = new TlvObject(i++, msg.getToAccountName());
		TlvObject tIoSessionId = new TlvObject(i++, 8, TlvByteUtil.long2Byte(msg.getReciverIoSessionId()));

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tMessageId);
		tlv.push(tMessageType);
		tlv.push(tFromAccountId);
		tlv.push(tFromAccountName);
		tlv.push(tFromAccountAvatarUrl);
		tlv.push(tTimestamp);
		tlv.push(tCurrentTimestamp);
		tlv.push(tChannelType);
		tlv.push(tChannelId);
		tlv.push(tChannelName);
		tlv.push(tChatId);
		tlv.push(tTxt);
		tlv.push(tAttachUrl);
		tlv.push(tToAccountlId);
		tlv.push(tToAccountName);
		tlv.push(tIoSessionId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private MessageInlinecast msg = new MessageInlinecast();

	public MessageInlinecast getMessage()
	{
		return msg;
	}

	public void setMessage(MessageInlinecast msg)
	{
		this.msg = msg;
	}

	private final static Logger logger = LoggerFactory.getLogger(InlinecastMessageReq.class);

}
