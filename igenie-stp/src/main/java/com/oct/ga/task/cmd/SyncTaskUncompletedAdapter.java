package com.oct.ga.task.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.base.SyncTimestampResp;
import com.oct.ga.comm.cmd.task.SyncTaskBaseResp;
import com.oct.ga.comm.cmd.task.SyncTaskUncompletedReq;
import com.oct.ga.comm.domain.taskpro.TaskProBaseInfo;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class SyncTaskUncompletedAdapter
		extends StpReqCommand
{
	public SyncTaskUncompletedAdapter()
	{
		super();

		this.setTag(Command.SYNC_TASKPRO_UNCOMPLETED_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncTaskUncompletedReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		int lastTryTime = reqCmd.getLastTryTime();
		String json = null;

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			List<TaskProBaseInfo> tasks = new ArrayList<TaskProBaseInfo>();
			// Logic: query uncompleted task which create after lastTryTime &
			// startTime<tomorrow
			int today = currentTimestamp - currentTimestamp % (86400);// wholedays
			int tommorrow = today + 86400;
			List<String> taskIds = taskService.queryUncompletedTaskIds(this.getMyAccountId(), lastTryTime, tommorrow);
			for (String taskId : taskIds) {
				TaskProBaseInfo taskBaseInfo = taskService.queryTaskBaseInfo(taskId);

				String porjectName = groupService.queryGroupName(taskBaseInfo.getPid());
				taskBaseInfo.setPname(porjectName);
				
				short memberRank = groupService.queryMemberRank(taskId, this.getMyAccountId());
				taskBaseInfo.setMemberRank(memberRank);

				tasks.add(taskBaseInfo);
			}

			JSONArray jsonArray = JSONArray.fromObject(tasks);
			json = jsonArray.toString();
			logger.debug("json: " + json);

			SyncTaskBaseResp syncTaskResp = new SyncTaskBaseResp(json);
			TlvObject syncTaskTlv = CommandParser.encode(syncTaskResp);
			session.write(syncTaskTlv);

			List<TaskProBaseInfo> projects = new ArrayList<TaskProBaseInfo>();

			// Logic: query uncompleted project ids which create after
			// lastTryTime
			List<String> projectIds = taskService.queryUncompletedProjectIds(this.getMyAccountId(), lastTryTime);

			for (String projectId : projectIds) {
				TaskProBaseInfo projectBaseInfo = taskService.queryTaskBaseInfo(projectId);

				short memberRank = groupService.queryMemberRank(projectId, this.getMyAccountId());
				projectBaseInfo.setMemberRank(memberRank);
				
				projects.add(projectBaseInfo);
			}

			jsonArray = JSONArray.fromObject(projects);
			json = jsonArray.toString();
			logger.debug("json: " + json);

			SyncTaskBaseResp syncProjectResp = new SyncTaskBaseResp(json);
			TlvObject syncProjectTlv = CommandParser.encode(syncProjectResp);
			session.write(syncProjectTlv);

			SyncTimestampResp respCmd = new SyncTimestampResp(Command.SYNC_TASKPRO_UNCOMPLETED_REQ, currentTimestamp);
			TlvObject tResp = CommandParser.encode(respCmd);
			session.write(tResp);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
		}

		return null;
	}

	private SyncTaskUncompletedReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskUncompletedAdapter.class);

}
