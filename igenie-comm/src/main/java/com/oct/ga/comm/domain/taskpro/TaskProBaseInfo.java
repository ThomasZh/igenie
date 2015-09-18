package com.oct.ga.comm.domain.taskpro;

import com.google.gson.Gson;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class TaskProBaseInfo
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7590174671746208143L;
	private String id;
	private String name;
	private String pid;
	private String pname;
	private int startTime = 0;
	private int endTime = 0;
	private short state = GlobalArgs.CLUB_ACTIVITY_STATE_OPENING;
	private short color = 0;
	private short depth = 0;
	private short memberRank = GlobalArgs.MEMBER_RANK_NORMAL;
	private short memberState = GlobalArgs.INVITE_STATE_APPLY;
	private short childNum = 0;
	private short memberNum = 0;
	private short fileNum = 0;
	private short noteNum = 0;
	private String desc;
	private int ver = 0;

	@Override
	public TaskProBaseInfo decode(String json)
	{
		Gson gson = new Gson();
		TaskProBaseInfo info = gson.fromJson(json, TaskProBaseInfo.class);
		return info;
	}

	@Override
	public String encode()
	{
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}

	@Override
	public String toString()
	{
		return this.encode();
	}

	public String getPname()
	{
		return pname;
	}

	public void setPname(String pname)
	{
		this.pname = pname;
	}

	public short getColor()
	{
		return color;
	}

	public void setColor(short color)
	{
		this.color = color;
	}

	public short getDepth()
	{
		return depth;
	}

	public void setDepth(short depth)
	{
		this.depth = depth;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPid()
	{
		return pid;
	}

	public void setPid(String pid)
	{
		this.pid = pid;
	}

	public int getStartTime()
	{
		return startTime;
	}

	public void setStartTime(int startTime)
	{
		this.startTime = startTime;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

	public short getChildNum()
	{
		return childNum;
	}

	public void setChildNum(short childNum)
	{
		this.childNum = childNum;
	}

	public short getMemberNum()
	{
		return memberNum;
	}

	public void setMemberNum(short memberNum)
	{
		this.memberNum = memberNum;
	}

	public short getFileNum()
	{
		return fileNum;
	}

	public void setFileNum(short fileNum)
	{
		this.fileNum = fileNum;
	}

	public short getNoteNum()
	{
		return noteNum;
	}

	public void setNoteNum(short noteNum)
	{
		this.noteNum = noteNum;
	}

	public short getMemberRank()
	{
		return memberRank;
	}

	public void setMemberRank(short asMemberRank)
	{
		this.memberRank = asMemberRank;
	}

	public int getEndTime()
	{
		return endTime;
	}

	public void setEndTime(int endTime)
	{
		this.endTime = endTime;
	}

	public short getMemberState()
	{
		return memberState;
	}

	public void setMemberState(short memberState)
	{
		this.memberState = memberState;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public int getVer()
	{
		return ver;
	}

	public void setVer(int ver)
	{
		this.ver = ver;
	}

}
