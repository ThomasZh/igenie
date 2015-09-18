package com.oct.ga.comm.cmd.apply;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.apply.GaApplicantTemplateCell;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ApplicantTemplateUploadReq
		extends ReqCommand
{
	public ApplicantTemplateUploadReq()
	{
		super();

		this.setTag(Command.UPLOAD_APPLICANT_TEMPLATE_REQ);
	}

	public ApplicantTemplateUploadReq(int sequence, String activityId, String contactJson, String participationJson)
	{
		this();

		this.setSequence(sequence);
		this.setActivityId(activityId);
		this.setContactJson(contactJson);
		this.setParticipationJson(participationJson);

		Gson gson = new Gson();
		if (contactJson != null && contactJson.length() > 0) {
			contactCells = gson.fromJson(contactJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());
		}
		if (participationJson != null && participationJson.length() > 0) {
			participationCells = gson.fromJson(participationJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());
		}
	}

	public ApplicantTemplateUploadReq(int sequence, String activityId, List<GaApplicantTemplateCell> contactCells,
			List<GaApplicantTemplateCell> templateCells)
	{
		this();

		this.setSequence(sequence);
		this.setActivityId(activityId);
		this.setContactCells(contactCells);
		this.setParticipationCells(templateCells);

		Gson gson = new Gson();
		contactJson = gson.toJson(contactCells);
		participationJson = gson.toJson(templateCells);
	}

	@Override
	public ApplicantTemplateUploadReq decode(TlvObject tlv)
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

		TlvObject tContactJson = tlv.getChild(i++);
		contactJson = new String(tContactJson.getValue(), "UTF-8");
		logger.debug("contactJson: " + contactJson);
		Gson gson = new Gson();
		if (contactJson != null && contactJson.length() > 0) {
			contactCells = gson.fromJson(contactJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());

			for (int n = 0; n < contactCells.size(); n++) {
				GaApplicantTemplateCell contactCell = contactCells.get(n);
				contactCell.setSeq(n + 1);
			}
			
			contactJson = gson.toJson(contactCells);
			logger.debug("contactJson order by seq: " + contactJson);
		}

		TlvObject tParticipationJson = tlv.getChild(i++);
		participationJson = new String(tParticipationJson.getValue(), "UTF-8");
		logger.debug("participationJson: " + participationJson);
		if (participationJson != null && participationJson.length() > 0) {
			participationCells = gson.fromJson(participationJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());
			
			for (int n = 0; n < participationCells.size(); n++) {
				GaApplicantTemplateCell participationCell = participationCells.get(n);
				participationCell.setSeq(n + 1);
			}
			
			participationJson = gson.toJson(participationCells);
			logger.debug("participationJson order by seq: " + participationJson);
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tActivityId = new TlvObject(i++, activityId);
		TlvObject tContactJson = new TlvObject(i++, contactJson);
		TlvObject tParticipationJson = new TlvObject(i++, participationJson);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tActivityId);
		tlv.push(tContactJson);
		tlv.push(tParticipationJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String activityId;
	private List<GaApplicantTemplateCell> participationCells;
	private String participationJson;
	private List<GaApplicantTemplateCell> contactCells;
	private String contactJson;

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	public String getParticipationJson()
	{
		return participationJson;
	}

	public void setParticipationJson(String json)
	{
		this.participationJson = json;
	}

	public List<GaApplicantTemplateCell> getParticipationCells()
	{
		return participationCells;
	}

	public void setParticipationCells(List<GaApplicantTemplateCell> templateCells)
	{
		this.participationCells = templateCells;
	}

	public String getContactJson()
	{
		return contactJson;
	}

	public void setContactJson(String contactJson)
	{
		this.contactJson = contactJson;
	}

	public List<GaApplicantTemplateCell> getContactCells()
	{
		return contactCells;
	}

	public void setContactCells(List<GaApplicantTemplateCell> contactCells)
	{
		this.contactCells = contactCells;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplicantTemplateUploadReq.class);

}
