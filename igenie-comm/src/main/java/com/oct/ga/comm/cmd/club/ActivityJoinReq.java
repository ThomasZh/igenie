package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.apply.GaApplicantCell;
import com.oct.ga.comm.domain.apply.GaApplicantInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityJoinReq
		extends ReqCommand
{
	public ActivityJoinReq()
	{
		super();

		this.setTag(Command.ACTIVITY_JOIN_REQ);
	}

	public ActivityJoinReq(int sequence, String activityId, List<GaApplicantCell> contactInfo,
			List<GaApplicantInfo> applicantInfos)
	{
		this();

		this.setSequence(sequence);
		this.setActivityId(activityId);
		this.setContactInfo(contactInfo);
		this.setApplicantInfos(applicantInfos);
	}

	@Override
	public TlvObject encode()
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tActivityId = new TlvObject(i++, activityId);
		Gson gson = new Gson();
		String contactInfoJson = gson.toJson(contactInfo);
		TlvObject tContactInfoJson = new TlvObject(i++, contactInfoJson);
		String applicantInfosJson = gson.toJson(applicantInfos);
		TlvObject tApplicantInfosJson = new TlvObject(i++, applicantInfosJson);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tActivityId);
		tlv.push(tContactInfoJson);
		tlv.push(tApplicantInfosJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	@Override
	public ActivityJoinReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 4;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tActivityId = tlv.getChild(i++);
		activityId = new String(tActivityId.getValue(), "UTF-8");
		logger.debug("activityId: " + activityId);

		TlvObject tContactInfoJson = tlv.getChild(i++);
		String contactInfoJson = new String(tContactInfoJson.getValue(), "UTF-8");
		logger.debug("contactInfoJson: " + contactInfoJson);
		Gson gson = new Gson();
		if (contactInfoJson != null && contactInfoJson.length() > 0) {
			contactInfo = gson.fromJson(contactInfoJson, new TypeToken<List<GaApplicantCell>>()
			{
			}.getType());
		}

		TlvObject tApplicantInfosJson = tlv.getChild(i++);
		String applicantInfosJson = new String(tApplicantInfosJson.getValue(), "UTF-8");
		logger.debug("applicantInfosJson: " + applicantInfosJson);
		if (applicantInfosJson != null && applicantInfosJson.length() > 0) {
			applicantInfos = gson.fromJson(applicantInfosJson, new TypeToken<List<GaApplicantInfo>>()
			{
			}.getType());
		}

		return this;
	}

	private String activityId;
	private List<GaApplicantCell> contactInfo;
	private List<GaApplicantInfo> applicantInfos;

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	public List<GaApplicantCell> getContactInfo()
	{
		return contactInfo;
	}

	public void setContactInfo(List<GaApplicantCell> contactInfo)
	{
		this.contactInfo = contactInfo;
	}

	public List<GaApplicantInfo> getApplicantInfos()
	{
		return applicantInfos;
	}

	public void setApplicantInfos(List<GaApplicantInfo> applicantInfos)
	{
		this.applicantInfos = applicantInfos;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityJoinReq.class);

}
