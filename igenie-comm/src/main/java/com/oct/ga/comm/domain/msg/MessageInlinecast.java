package com.oct.ga.comm.domain.msg;

public class MessageInlinecast
		extends MessageExtendUnicast
{
	public MessageInlinecast clone()
			throws CloneNotSupportedException
	{
		return (MessageInlinecast) this.clone();
	}

	public MessageInlinecast copy()
	{
		MessageInlinecast message = new MessageInlinecast();

		message.set_id(this.get_id());
		message.setContent(this.getContent());
		message.setContentType(this.getContentType());
		message.setCurrentTimestamp(currentTimestamp);
		message.setFromAccountId(this.getFromAccountId());
		message.setFromAccountName(this.getFromAccountName());
		message.setReciverIoSessionId(reciverIoSessionId);
		message.setSenderDeviceId(senderDeviceId);
		message.setSyncState(this.getSyncState());
		message.setTimestamp(this.getTimestamp());
		message.setToAccountId(this.getToAccountId());
		message.setToAccountName(this.getToAccountName());
		message.setChannelId(this.getChannelId());
		message.setChannelName(this.getChannelName());
		message.setChannelType(this.getChannelType());
		message.setAttachUrl(this.getAttachUrl());

		return message;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6917959482321181449L;
	private long reciverIoSessionId;
	private String senderDeviceId;
	private int currentTimestamp;

	public String getSenderDeviceId()
	{
		return senderDeviceId;
	}

	public void setSenderDeviceId(String senderDeviceId)
	{
		this.senderDeviceId = senderDeviceId;
	}

	public long getReciverIoSessionId()
	{
		return reciverIoSessionId;
	}

	public void setReciverIoSessionId(long reciverIoSessionId)
	{
		this.reciverIoSessionId = reciverIoSessionId;
	}

	public int getCurrentTimestamp()
	{
		return currentTimestamp;
	}

	public void setCurrentTimestamp(int currentTimestamp)
	{
		this.currentTimestamp = currentTimestamp;
	}

}
