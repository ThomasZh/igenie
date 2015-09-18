package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.apply.GaApplicantCell;
import com.oct.ga.comm.domain.apply.GaApplicantDetailInfo;
import com.oct.ga.comm.domain.apply.GaApplicantInfo;
import com.oct.ga.comm.domain.apply.GaApplicantTemplate;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;

public interface GaApplyService
{
	public List<GaApplyStateNotify> queryNotReceived(String accountId);

	public void modifySyncStateToReceived(String fromAccountId, String toAccountId, String channelId, int timestamp);

	public String modify(String fromAccountId, String toAccountId, String channelId, short action, String txt,
			int timestamp);

	// ////////////////////////////////////////////////////////////////////

	public void modifyApplicantTemplate(String activityId, String contactJson, String participationJson, int timestamp);

	public GaApplicantTemplate queryApplicantTemplate(String activityId);

	// ////////////////////////////////////////////////////////////////////

	public void modifyApplicantContact(String activityId, String myAccountId, String contactInfoJson, int timestamp);

	public void removeAllApplicant(String activityId, String accountId);

	public void addApplicant(String activityId, String accountId, int seq, String json, int timestamp);

	public List<GaApplicantCell> queryApplicantContact(String activityId, String accountId);

	public List<GaApplicantInfo> queryApplicants(String activityId, String accountId);

	public List<GaApplicantDetailInfo> query(String activityId);

	// ////////////////////////////////////////////////////////////////////

	public short queryApplyNum(String accountId);
}
