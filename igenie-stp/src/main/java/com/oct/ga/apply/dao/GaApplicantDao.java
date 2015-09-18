package com.oct.ga.apply.dao;

import java.util.List;

import com.oct.ga.comm.domain.apply.GaApplicantDetailInfo;
import com.oct.ga.comm.domain.apply.GaApplicantInfo;

public interface GaApplicantDao
{
	public void removeAll(String activityId, String accountId);

	public void add(String activityId, String accountId, int seq, String json, int timestamp);

	public List<GaApplicantInfo> query(String activityId, String accountId);

	public boolean isExistContact(String activityId, String accountId);

	public void updateContact(String activityId, String myAccountId, String contactInfoJson, int timestamp);

	public void addContact(String activityId, String myAccountId, String contactInfoJson, int timestamp);

	public String queryApplicantContact(String activityId, String accountId);
	
	public List<GaApplicantDetailInfo> query(String activityId);
}
