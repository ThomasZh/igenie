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
import com.oct.ga.comm.cmd.task.SyncTomorrowTaskReq;
import com.oct.ga.comm.cmd.task.SyncTomorrowTaskResp;
import com.oct.ga.comm.domain.taskext.TodayTaskMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class SyncTomorrowTaskAdapter
		extends StpReqCommand
{
	public SyncTomorrowTaskAdapter()
	{
		super();

		this.setTag(Command.SYNC_TOMORROW_TASK_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncTomorrowTaskReq().decode(tlv);
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

			List<TodayTaskMaster> tasks = new ArrayList<TodayTaskMaster>();
			// startTime<tomorrow & uncompleted task & lastTryTime
			int today0 = currentTimestamp - currentTimestamp % (86400);// dayTime
																		// at
																		// 0:00
			int tomorrow0 = today0 + 86400;
			int tomorrow24 = tomorrow0 + 86400;

			List<String> uncommpletedTaskIds = taskService.queryUncompletedTaskIds(this.getMyAccountId(), lastTryTime,
					tomorrow0, tomorrow24);
			for (String taskId : uncommpletedTaskIds) {
				TodayTaskMaster taskMasterInfo = taskService.queryTodayTaskMaster(taskId);

				if (taskMasterInfo.getDepth() > 0) {
					String porjectName = groupService.queryGroupName(taskMasterInfo.getPid());
					taskMasterInfo.setPname(porjectName);

					short pState = groupService.queryState(taskMasterInfo.getPid());
					taskMasterInfo.setpState(pState);
				}

				tasks.add(taskMasterInfo);
			}

			// today<executeEndTime<tomorrow & completed task & lastTryTime
			List<String> commpletedTaskIds = taskService.queryCompletedTaskIds(this.getMyAccountId(), lastTryTime,
					tomorrow0, tomorrow24);
			for (String taskId : commpletedTaskIds) {
				TodayTaskMaster taskMasterInfo = taskService.queryTodayTaskMaster(taskId);

				if (taskMasterInfo.getDepth() > 0) {
					String porjectName = groupService.queryGroupName(taskMasterInfo.getPid());
					taskMasterInfo.setPname(porjectName);

					short pState = groupService.queryState(taskMasterInfo.getPid());
					taskMasterInfo.setpState(pState);
				}

				tasks.add(taskMasterInfo);
			}

			JSONArray jsonArray = JSONArray.fromObject(tasks);
			json = jsonArray.toString();
			logger.debug("json: " + json);

			SyncTomorrowTaskResp respCmd = new SyncTomorrowTaskResp(ErrorCode.SUCCESS, json, currentTimestamp);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncTomorrowTaskResp respCmd = new SyncTomorrowTaskResp(ErrorCode.UNKNOWN_FAILURE, json, currentTimestamp);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private SyncTomorrowTaskReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncTomorrowTaskAdapter.class);

}
