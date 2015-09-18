package com.oct.ga.apply.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.apply.ApplicantInfosUploadReq;
import com.oct.ga.comm.cmd.apply.ApplicantInfosUploadResp;
import com.oct.ga.comm.domain.apply.GaApplicantCell;
import com.oct.ga.comm.domain.apply.GaApplicantInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ApplicantInfosUploadAdapter
		extends StpReqCommand
{
	public ApplicantInfosUploadAdapter()
	{
		super();

		this.setTag(Command.UPLOAD_APPLICANTS_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ApplicantInfosUploadReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String myAccountId = this.getMyAccountId();
		String activityId = reqCmd.getActivityId();
		List<GaApplicantInfo> applicantInfos = reqCmd.getApplicantInfos();
		List<GaApplicantCell> contactInfo = reqCmd.getContactInfo();

		try {
			GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");

			Gson gson = new Gson();
			String contactInfoJson = gson.toJson(contactInfo);
			applyService.modifyApplicantContact(activityId, myAccountId, contactInfoJson, currentTimestamp);
			
			applyService.removeAllApplicant(activityId, myAccountId);
			for (GaApplicantInfo applicantInfo : applicantInfos) {
				String json = gson.toJson(applicantInfo.getApplicant());
				logger.debug("json: " + json);
				applyService.addApplicant(activityId, myAccountId, applicantInfo.getSeq(), json, currentTimestamp);
			}

			ApplicantInfosUploadResp respCmd = new ApplicantInfosUploadResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ApplicantInfosUploadResp respCmd = new ApplicantInfosUploadResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ApplicantInfosUploadReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ApplicantInfosUploadAdapter.class);

}
