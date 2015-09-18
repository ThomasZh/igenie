package com.oct.ga.apply.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.apply.ApplicantTemplateQueryReq;
import com.oct.ga.comm.cmd.apply.ApplicantTemplateQueryResp;
import com.oct.ga.comm.domain.apply.GaApplicantTemplate;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ApplicantTemplateQueryAdapter
		extends StpReqCommand
{
	public ApplicantTemplateQueryAdapter()
	{
		super();

		this.setTag(Command.QUERY_APPLICANT_TEMPLATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ApplicantTemplateQueryReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String activityId = reqCmd.getActivityId();

		try {
			GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");

			GaApplicantTemplate template = applyService.queryApplicantTemplate(activityId);

			ApplicantTemplateQueryResp respCmd = new ApplicantTemplateQueryResp(sequence, ErrorCode.SUCCESS,
					template.getContactJson(), template.getParticipationJson());
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ApplicantTemplateQueryResp respCmd = new ApplicantTemplateQueryResp(sequence, ErrorCode.UNKNOWN_FAILURE,
					"", "");
			return respCmd;
		}
	}

	private ApplicantTemplateQueryReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ApplicantTemplateQueryAdapter.class);

}
