package com.oct.ga.comm.cmd.msg;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class UploadMessageReq
		extends ReqCommand
{
	public UploadMessageReq()
	{
		super();

		this.setTag(Command.UPLOAD_MESSAGE_REQ);
	}

	public UploadMessageReq(String msgId, short contentType, short channelType, String channelId, String chatId,
			String toAccountId, String content, String attachUrl)
	{
		this();

		this.setMsgId(msgId);
		this.setContentType(contentType);
		this.setChannelType(channelType);
		this.setChannelId(channelId);
		this.setChatId(chatId);
		this.setToAccountId(toAccountId);
		this.setContent(content);
		this.setAttachUrl(attachUrl);
	}

	public UploadMessageReq(int sequence, String msgId, short contentType, short channelType, String channelId,
			String chatId, String toAccountId, String content, String attachUrl)
	{
		this(msgId, contentType, channelType, channelId, chatId, toAccountId, content, attachUrl);

		this.setSequence(sequence);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tMsgId = new TlvObject(i++, msgId);
		TlvObject tContentType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(contentType));
		TlvObject tChannelType = new TlvObject(i++, 2, TlvByteUtil.short2Byte(channelType));
		TlvObject tChannelId = new TlvObject(i++, channelId);
		TlvObject tChatId = new TlvObject(i++, chatId);
		TlvObject tToAccountId = new TlvObject(i++, toAccountId);
		TlvObject tContent = new TlvObject(i++, content);
		TlvObject tAttachUrl = new TlvObject(i++, attachUrl);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tMsgId);
		tlv.push(tContentType);
		tlv.push(tChannelType);
		tlv.push(tChannelId);
		tlv.push(tChatId);
		tlv.push(tToAccountId);
		tlv.push(tContent);
		tlv.push(tAttachUrl);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public UploadMessageReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 9;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tMsgId = tlv.getChild(i++);
		msgId = new String(tMsgId.getValue(), "UTF-8");
		logger.debug("msgId: " + msgId);

		TlvObject tContentType = tlv.getChild(i++);
		contentType = TlvByteUtil.byte2Short(tContentType.getValue());
		logger.debug("contentType: " + contentType);

		TlvObject tChannelType = tlv.getChild(i++);
		channelType = TlvByteUtil.byte2Short(tChannelType.getValue());
		logger.debug("channelType: " + channelType);

		TlvObject tChannelId = tlv.getChild(i++);
		channelId = new String(tChannelId.getValue(), "UTF-8");
		logger.debug("channelId: " + channelId);

		TlvObject tChatId = tlv.getChild(i++);
		chatId = new String(tChatId.getValue(), "UTF-8");
		logger.debug("chatId: " + chatId);

		TlvObject tToAccountId = tlv.getChild(i++);
		toAccountId = new String(tToAccountId.getValue(), "UTF-8");
		logger.debug("toAccountId: " + toAccountId);

		TlvObject tContent = tlv.getChild(i++);
		content = new String(tContent.getValue(), "UTF-8");
		logger.debug("content: " + content);

		TlvObject tAttachUrl = tlv.getChild(i++);
		attachUrl = new String(tAttachUrl.getValue(), "UTF-8");
		logger.debug("attachUrl: " + attachUrl);

		return this;
	}

	/**
	 * UUID.randomUUID().toString()
	 */
	private String msgId;
	/**
	 * 0:txt
	 */
	private short contentType;
	/**
	 * 101:activity,102:question_create,103:question
	 */
	private short channelType;
	/**
	 * activityId
	 */
	private String channelId;
	/**
	 * 101:activity=acitivtyId,103:question=
	 * chatId[md5(fromAccontId+toAccountId)]
	 */
	private String chatId;
	/**
	 * 102:question_create=toAccountId must have
	 */
	private String toAccountId;
	private String content;
	private String attachUrl;

	public String getMsgId()
	{
		return msgId;
	}

	public void setMsgId(String msgId)
	{
		this.msgId = msgId;
	}

	public short getContentType()
	{
		return contentType;
	}

	public void setContentType(short contentType)
	{
		this.contentType = contentType;
	}

	public short getChannelType()
	{
		return channelType;
	}

	public void setChannelType(short channelType)
	{
		this.channelType = channelType;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getAttachUrl()
	{
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl)
	{
		this.attachUrl = attachUrl;
	}

	public String getChatId()
	{
		return chatId;
	}

	public void setChatId(String chatId)
	{
		this.chatId = chatId;
	}

	public String getToAccountId()
	{
		return toAccountId;
	}

	public void setToAccountId(String toAccountId)
	{
		this.toAccountId = toAccountId;
	}

	private final static Logger logger = LoggerFactory.getLogger(UploadMessageReq.class);
}
