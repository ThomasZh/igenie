package com.oct.ga.apply.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.apply.ApplicantInfosQueryReq;
import com.oct.ga.comm.cmd.apply.ApplicantInfosQueryResp;
import com.oct.ga.comm.domain.apply.GaApplicantCell;
import com.oct.ga.comm.domain.apply.GaApplicantInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ApplicantInfosQueryAdapter
		extends StpReqCommand
{
	public ApplicantInfosQueryAdapter()
	{
		super();

		this.setTag(Command.QUERY_APPLICANTS_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ApplicantInfosQueryReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String activityId = reqCmd.getActivityId();
		String accountId = reqCmd.getAccountId();
		short memberState = GlobalArgs.INVITE_STATE_QUIT;

		try {
			GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			memberState = groupService.queryMemberState(activityId, accountId);
			List<GaApplicantCell> applicantContactInfo = applyService.queryApplicantContact(activityId, accountId);
			List<GaApplicantInfo> applicantInfos = applyService.queryApplicants(activityId, accountId);

			ApplicantInfosQueryResp respCmd = new ApplicantInfosQueryResp(sequence, ErrorCode.SUCCESS, memberState,
					applicantContactInfo, applicantInfos);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ApplicantInfosQueryResp respCmd = new ApplicantInfosQueryResp(sequence, ErrorCode.UNKNOWN_FAILURE,
					memberState, null, null);
			return respCmd;
		}
	}

	private ApplicantInfosQueryReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ApplicantInfosQueryAdapter.class);

}
