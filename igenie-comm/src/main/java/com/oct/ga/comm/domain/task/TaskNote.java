package com.oct.ga.comm.domain.task;

import java.io.Serializable;

public class TaskNote
		implements Serializable
{
	public TaskNote()
	{
	}

	public TaskNote(String noteId, String taskId, String txt)
	{
		this.setNoteId(noteId);
		this.setTaskId(taskId);
		this.setTxt(txt);
	}

	private String noteId;
	private String taskId;
	private String accountId;
	private String accountName;
	private int timestamp;
	private String txt;
	private short state;

	public String getNoteId()
	{
		return noteId;
	}

	public void setNoteId(String noteId)
	{
		this.noteId = noteId;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String createrId)
	{
		this.accountId = createrId;
	}

	public String getTxt()
	{
		return txt;
	}

	public void setTxt(String txt)
	{
		this.txt = txt;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String createName)
	{
		this.accountName = createName;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

	private static final long serialVersionUID = -6282765812353583576L;
}
