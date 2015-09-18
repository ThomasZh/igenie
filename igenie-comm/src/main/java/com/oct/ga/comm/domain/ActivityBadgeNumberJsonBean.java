package com.oct.ga.comm.domain;

import java.io.Serializable;
import java.util.List;

import com.oct.ga.comm.domain.msg.NotifyTaskLog;

public class ActivityBadgeNumberJsonBean
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3458675962425043931L;
	private List<BadgeNumberJsonBean> activityNumber;
	private List<NotifyTaskLog> activityList;

	public List<BadgeNumberJsonBean> getActivityNumber()
	{
		return activityNumber;
	}

	public void setActivityNumber(List<BadgeNumberJsonBean> activityNumber)
	{
		this.activityNumber = activityNumber;
	}

	public List<NotifyTaskLog> getActivityList()
	{
		return activityList;
	}

	public void setActivityList(List<NotifyTaskLog> activityList)
	{
		this.activityList = activityList;
	}

}
