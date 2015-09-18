package com.oct.ga.template.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.template.MakeProject2TemplateReq;
import com.oct.ga.comm.cmd.template.MakeProject2TemplateResp;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.domain.template.TemplateDefineJsonBean;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.service.GaTemplateService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class MakeProject2TemplateAdapter
		extends StpReqCommand
{
	public MakeProject2TemplateAdapter()
	{
		super();

		this.setTag(Command.MAKE_PROJECT_TO_TEMPLATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new MakeProject2TemplateReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		MakeProject2TemplateResp respCmd = null;
		String taskId = reqCmd.getTaskId();
		String templateName = reqCmd.getTemplateName();

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaTemplateService templateService = (GaTemplateService) context.getBean("gaTemplateService");

			TaskProExtInfo project = taskService.query(taskId);
			String templatePid = null;

			TemplateDefineJsonBean projectTemplate = templateService.makeProject2Template(context, project,
					this.getMyAccountId(), templatePid, templateName);

			String templateId = projectTemplate.getTemplateId();
			logger.info(this.getMyAccountName() + " make a template " + templateName + "(" + templateId
					+ ") from task(" + taskId + ")");

			respCmd = new MakeProject2TemplateResp(ErrorCode.SUCCESS, taskId, templateId);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new MakeProject2TemplateResp(ErrorCode.UNKNOWN_FAILURE, taskId, null);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private MakeProject2TemplateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MakeProject2TemplateAdapter.class);
}
