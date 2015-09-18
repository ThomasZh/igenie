package com.oct.ga.apply.dao;

import com.oct.ga.comm.domain.apply.GaApplicantTemplate;

public interface GaApplicantTemplateDao
{
	public boolean isExist(String activityId);

	public void add(String activityId, String contactJson, String participationJson, int timestamp);

	public void update(String activityId, String contactJson, String participationJson, int timestamp);

	public GaApplicantTemplate query(String activityId);
}
