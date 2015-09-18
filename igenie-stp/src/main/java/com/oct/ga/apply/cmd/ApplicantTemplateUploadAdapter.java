package com.oct.ga.apply.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.apply.ApplicantTemplateUploadReq;
import com.oct.ga.comm.cmd.apply.ApplicantTemplateUploadResp;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ApplicantTemplateUploadAdapter
		extends StpReqCommand
{
	public ApplicantTemplateUploadAdapter()
	{
		super();

		this.setTag(Command.UPLOAD_APPLICANT_TEMPLATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ApplicantTemplateUploadReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String activityId = reqCmd.getActivityId();
		String contactJson = reqCmd.getContactJson();
		String participationJson = reqCmd.getParticipationJson();

		try {
			GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");

			applyService.modifyApplicantTemplate(activityId, contactJson, participationJson, currentTimestamp);

			ApplicantTemplateUploadResp respCmd = new ApplicantTemplateUploadResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ApplicantTemplateUploadResp respCmd = new ApplicantTemplateUploadResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private ApplicantTemplateUploadReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ApplicantTemplateUploadAdapter.class);

}
