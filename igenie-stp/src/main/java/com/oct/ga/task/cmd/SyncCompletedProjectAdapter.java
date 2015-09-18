package com.oct.ga.task.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.task.SyncCompletedProjectReq;
import com.oct.ga.comm.cmd.task.SyncCompletedProjectResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.taskext.ProjectMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class SyncCompletedProjectAdapter
		extends StpReqCommand
{
	public SyncCompletedProjectAdapter()
	{
		super();

		this.setTag(Command.SYNC_PROJECT_COMPLETED_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncCompletedProjectReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		int lastTryTime = reqCmd.getLastTryTime();
		List<ProjectMaster> projects = null;

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			// query uncompleted project id list which modify after lastTryTime
			List<String> projectIds = taskService.queryCompletedProjectIds(this.getMyAccountId(), lastTryTime);
			projects = new ArrayList<ProjectMaster>();
			for (String projectId : projectIds) {
				ProjectMaster projectInfo = taskService.queryProjectMaster(projectId);
				if (projectInfo.getId() != null && projectInfo.getId().length() > 0) {
					int childNum = groupService.queryChildNum(projectId);
					projectInfo.setChildNum(childNum);

					try {
						String leaderId = groupService.queryLeaderId(projectId);
						AccountBasic account = accountService.queryAccount(leaderId);

						projectInfo.setLeaderId(leaderId);
						projectInfo.setLeaderName(account.getNickname());
						projectInfo.setLeaderAvatarUrl(account.getAvatarUrl());
					} catch (Exception e) {
						logger.error("query leader info error: " + LogErrorMessage.getFullInfo(e));
					}

					projects.add(projectInfo);
				}
			}

			SyncCompletedProjectResp respCmd = new SyncCompletedProjectResp(sequence, ErrorCode.SUCCESS, projects,
					currentTimestamp);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncCompletedProjectResp respCmd = new SyncCompletedProjectResp(sequence, ErrorCode.UNKNOWN_FAILURE,
					projects, currentTimestamp);
			return respCmd;
		}
	}

	private SyncCompletedProjectReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncCompletedProjectAdapter.class);

}
