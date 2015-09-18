package com.oct.ga.comm.cmd.apply;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.apply.GaApplicantTemplateCell;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ApplicantTemplateQueryResp
		extends RespCommand
{
	public ApplicantTemplateQueryResp()
	{
		this.setTag(Command.QUERY_APPLICANT_TEMPLATE_RESP);
	}

	public ApplicantTemplateQueryResp(int sequence, short respState, String contactJson, String participationJson)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setContactJson(contactJson);
		this.setParticipationJson(participationJson);

		Gson gson = new Gson();
		if (contactJson != null && contactJson.length() > 0) {
			contactCells = gson.fromJson(participationJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());

		}
		if (participationJson != null && participationJson.length() > 0) {
			participationCells = gson.fromJson(participationJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());
		}
	}

	public ApplicantTemplateQueryResp(int sequence, short respState, List<GaApplicantTemplateCell> contactCells,
			List<GaApplicantTemplateCell> templateCells)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setContactCells(contactCells);
		this.setParticipationCells(templateCells);

		Gson gson = new Gson();
		contactJson = gson.toJson(contactCells);
		participationJson = gson.toJson(templateCells);
	}

	@Override
	public ApplicantTemplateQueryResp decode(TlvObject tlv)
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

		TlvObject tResultState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultState.getValue()));
		logger.debug("resultState: " + this.getRespState());

		TlvObject tContactJson = tlv.getChild(i++);
		contactJson = new String(tContactJson.getValue(), "UTF-8");
		logger.debug("contactJson: " + contactJson);
		Gson gson = new Gson();
		if (contactJson != null && contactJson.length() > 0) {
			contactCells = gson.fromJson(contactJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());
		}

		TlvObject tParticipationJson = tlv.getChild(i++);
		participationJson = new String(tParticipationJson.getValue(), "UTF-8");
		logger.debug("participationJson: " + participationJson);
		if (participationJson != null && participationJson.length() > 0) {
			participationCells = gson.fromJson(participationJson, new TypeToken<List<GaApplicantTemplateCell>>()
			{
			}.getType());
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tContactJson = new TlvObject(i++, contactJson);
		TlvObject tParticipationJson = new TlvObject(i++, participationJson);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tContactJson);
		tlv.push(tParticipationJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String participationJson;
	private List<GaApplicantTemplateCell> participationCells;
	private List<GaApplicantTemplateCell> contactCells;
	private String contactJson;

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

	private final static Logger logger = LoggerFactory.getLogger(ApplicantTemplateQueryResp.class);

}
