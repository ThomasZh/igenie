package com.oct.ga.comm.cmd.msg;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.msg.MessageExtendUnicast;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class SyncMessageResp
		extends RespCommand
{
	public SyncMessageResp()
	{
		this.setTag(Command.SYNC_MESSAGE_RESP);
	}

	public SyncMessageResp(MessageExtendUnicast msg)
	{
		this();

		this.setMsg(msg);
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 1;

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
		TlvObject tAttachUrl = new TlvObject(i++, msg.getAttachUrl());
		TlvObject tTxt = new TlvObject(i++, msg.getContent());
		TlvObject tSyncState = new TlvObject(i++, 2, TlvByteUtil.short2Byte(msg.getSyncState()));

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
		tlv.push(tSyncState);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private MessageExtendUnicast msg = new MessageExtendUnicast();

	public MessageExtendUnicast getMsg()
	{
		return msg;
	}

	public void setMsg(MessageExtendUnicast msg)
	{
		this.msg = msg;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncMessageResp.class);

}
