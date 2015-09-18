package com.oct.ga.comm.domain.msg;

public class MessageOriginalMulticast
		extends MsgBasic
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6993218118890836226L;
	/**
	 * MessageID(UUID)
	 */
	private String _id;
	/**
	 * time(second) since 1970.1.1
	 */
	private int timestamp;
	private String chatId;
	/**
	 * 0:txt, 1:picture, 2:audio, 3:video, 9:others 101:join_game,
	 * 102:giveup_game, 103:play_next_step, 104:congratulate_win_game
	 */
	private short contentType;
	/**
	 * play_nex_step:{'step'='15','i'='3','j'='4'}
	 */
	private String content;
	private String attachUrl;

	public String get_id()
	{
		return _id;
	}

	public void set_id(String _id)
	{
		this._id = _id;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public short getContentType()
	{
		return contentType;
	}

	public void setContentType(short contentType)
	{
		this.contentType = contentType;
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

	public void setAttachUrl(String attachId)
	{
		this.attachUrl = attachId;
	}

	public String getChatId()
	{
		return chatId;
	}

	public void setChatId(String chatId)
	{
		this.chatId = chatId;
	}

}
