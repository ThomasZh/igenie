package com.oct.ga.apply;

import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.apply.dao.GaApplicantDao;
import com.oct.ga.apply.dao.GaApplicantTemplateDao;
import com.oct.ga.apply.dao.GaApplyDao;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.apply.GaApplicantCell;
import com.oct.ga.comm.domain.apply.GaApplicantDetailInfo;
import com.oct.ga.comm.domain.apply.GaApplicantInfo;
import com.oct.ga.comm.domain.apply.GaApplicantTemplate;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.service.GaApplyService;

public class ApplyServiceImpl
		implements GaApplyService
{
	@Override
	public List<GaApplyStateNotify> queryNotReceived(String accountId)
	{
		return applyDao.queryNotReceived(accountId);
	}

	@Override
	public void modifySyncStateToReceived(String fromAccountId, String toAccountId, String channelId, int timestamp)
	{
		applyDao.updateSyncState(fromAccountId, toAccountId, channelId, GlobalArgs.SYNC_STATE_RECEIVED, timestamp);
	}

	@Override
	public String modify(String fromAccountId, String toAccountId, String channelId, short action, String txt,
			int timestamp)
	{
		if (applyDao.isExist(fromAccountId, toAccountId, channelId)) {
			applyDao.update(fromAccountId, toAccountId, channelId, action, txt, timestamp);
			String id = applyDao.queryId(fromAccountId, toAccountId, channelId);
			return id;
		} else {
			String msgId = UUID.randomUUID().toString();
			applyDao.add(msgId, fromAccountId, toAccountId, channelId, action, txt, timestamp);
			return msgId;
		}
	}

	// ////////////////////////////////////////////////////////////////////

	@Override
	public void modifyApplicantTemplate(String activityId, String contactJson, String participationJson, int timestamp)
	{
		if (applicantTemplateDao.isExist(activityId)) {
			applicantTemplateDao.update(activityId, contactJson, participationJson, timestamp);
		} else {
			applicantTemplateDao.add(activityId, contactJson, participationJson, timestamp);
		}
	}

	@Override
	public GaApplicantTemplate queryApplicantTemplate(String activityId)
	{
		return applicantTemplateDao.query(activityId);
	}

	// ////////////////////////////////////////////////////////////////////

	@Override
	public void modifyApplicantContact(String activityId, String accountId, String contactInfoJson, int timestamp)
	{
		if (applicantDao.isExistContact(activityId, accountId)) {
			applicantDao.updateContact(activityId, accountId, contactInfoJson, timestamp);
		} else {
			applicantDao.addContact(activityId, accountId, contactInfoJson, timestamp);
		}
	}

	@Override
	public void removeAllApplicant(String activityId, String accountId)
	{
		applicantDao.removeAll(activityId, accountId);
	}

	@Override
	public void addApplicant(String activityId, String accountId, int seq, String json, int timestamp)
	{
		applicantDao.add(activityId, accountId, seq, json, timestamp);
	}

	@Override
	public List<GaApplicantInfo> queryApplicants(String activityId, String accountId)
	{
		return applicantDao.query(activityId, accountId);
	}

	@Override
	public List<GaApplicantDetailInfo> query(String activityId)
	{
		return applicantDao.query(activityId);
	}

	@Override
	public List<GaApplicantCell> queryApplicantContact(String activityId, String accountId)
	{
		List<GaApplicantCell> contact = null;

		String json = applicantDao.queryApplicantContact(activityId, accountId);
		Gson gson = new Gson();
		if (json != null && json.length() > 0) {
			contact = gson.fromJson(json, new TypeToken<List<GaApplicantCell>>()
			{
			}.getType());
		}

		return contact;
	}

	@Override
	public short queryApplyNum(String accountId)
	{
		return applyDao.queryApplyNum(accountId);
	}

	// ////////////////////////////////////////////////////////////////////

	private GaApplyDao applyDao;
	private GaApplicantTemplateDao applicantTemplateDao;
	private GaApplicantDao applicantDao;

	public GaApplicantTemplateDao getApplicantTemplateDao()
	{
		return applicantTemplateDao;
	}

	public void setApplicantTemplateDao(GaApplicantTemplateDao applicantTemplateDao)
	{
		this.applicantTemplateDao = applicantTemplateDao;
	}

	public GaApplicantDao getApplicantDao()
	{
		return applicantDao;
	}

	public void setApplicantDao(GaApplicantDao applicantDao)
	{
		this.applicantDao = applicantDao;
	}

	public GaApplyDao getApplyDao()
	{
		return applyDao;
	}

	public void setApplyDao(GaApplyDao applyDao)
	{
		this.applyDao = applyDao;
	}

}
