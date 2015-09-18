package com.oct.ga.comm.cmd.apply;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.apply.GaApplicantCell;
import com.oct.ga.comm.domain.apply.GaApplicantInfo;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ApplicantInfosQueryResp
		extends RespCommand
{
	public ApplicantInfosQueryResp()
	{
		this.setTag(Command.QUERY_APPLICANTS_RESP);
	}

	public ApplicantInfosQueryResp(int sequence, short respState, short memberState,
			List<GaApplicantCell> applicantContactInfo, List<GaApplicantInfo> applicantInfos)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setMemberState(memberState);
		this.setApplicantContactInfo(applicantContactInfo);
		this.setApplicantInfos(applicantInfos);
	}

	@Override
	public ApplicantInfosQueryResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 5;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tResultState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultState.getValue()));
		logger.debug("resultState: " + this.getRespState());

		TlvObject tMemberState = tlv.getChild(i++);
		memberState = TlvByteUtil.byte2Short(tMemberState.getValue());
		logger.debug("memberState: " + memberState);

		TlvObject tApplicantContactInfoJson = tlv.getChild(i++);
		String applicantContactInfoJson = new String(tApplicantContactInfoJson.getValue(), "UTF-8");
		logger.debug("applicantContactInfoJson: " + applicantContactInfoJson);
		Gson gson = new Gson();
		if (applicantContactInfoJson != null && applicantContactInfoJson.length() > 0) {
			applicantContactInfo = gson.fromJson(applicantContactInfoJson, new TypeToken<List<GaApplicantCell>>()
			{
			}.getType());
		}

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null && json.length() > 0) {
			applicantInfos = gson.fromJson(json, new TypeToken<List<GaApplicantInfo>>()
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
		TlvObject tMemberState = new TlvObject(i++, 2, TlvByteUtil.short2Byte(memberState));
		logger.debug("memberState: " + memberState);
		Gson gson = new Gson();
		String applicantContactInfoJson = gson.toJson(applicantContactInfo);
		TlvObject tApplicantContactInfoJson = new TlvObject(i++, applicantContactInfoJson);
		String json = gson.toJson(applicantInfos);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tMemberState);
		tlv.push(tApplicantContactInfoJson);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private short memberState;
	private List<GaApplicantCell> applicantContactInfo;
	private List<GaApplicantInfo> applicantInfos;

	public List<GaApplicantInfo> getApplicantInfos()
	{
		return applicantInfos;
	}

	public void setApplicantInfos(List<GaApplicantInfo> applicantInfos)
	{
		this.applicantInfos = applicantInfos;
	}

	public List<GaApplicantCell> getApplicantContactInfo()
	{
		return applicantContactInfo;
	}

	public void setApplicantContactInfo(List<GaApplicantCell> applicantContactInfo)
	{
		this.applicantContactInfo = applicantContactInfo;
	}

	public short getMemberState()
	{
		return memberState;
	}

	public void setMemberState(short accountState)
	{
		this.memberState = accountState;
	}

	private final static Logger logger = LoggerFactory.getLogger(ApplicantInfosQueryResp.class);

}
