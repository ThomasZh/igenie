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
import com.oct.ga.comm.cmd.task.SyncUncompletedProjectReq;
import com.oct.ga.comm.cmd.task.SyncUncompletedProjectResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.taskext.ProjectMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class SyncUncompletedProjectAdapter
		extends StpReqCommand
{
	public SyncUncompletedProjectAdapter()
	{
		super();

		this.setTag(Command.SYNC_PROJECT_UNCOMPLETED_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncUncompletedProjectReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		int lastTryTime = reqCmd.getLastTryTime();

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			// query uncompleted project id list which modify after lastTryTime
			List<String> projectIds = taskService.queryUncompletedProjectIds(this.getMyAccountId(), lastTryTime);
			List<ProjectMaster> projects = new ArrayList<ProjectMaster>();
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

			SyncUncompletedProjectResp respCmd = new SyncUncompletedProjectResp(sequence, ErrorCode.SUCCESS, projects,
					currentTimestamp);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncUncompletedProjectResp respCmd = new SyncUncompletedProjectResp(sequence, ErrorCode.UNKNOWN_FAILURE,
					null, currentTimestamp);
			return respCmd;
		}
	}

	private SyncUncompletedProjectReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncUncompletedProjectAdapter.class);
}
