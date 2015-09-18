package com.oct.ga.task.cmd;

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
import com.oct.ga.comm.cmd.task.SyncTaskChildReq;
import com.oct.ga.comm.cmd.task.SyncTaskChildResp;
import com.oct.ga.comm.domain.taskpro.TaskProBaseInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class SyncTaskChildAdapter
		extends StpReqCommand
{
	public SyncTaskChildAdapter()
	{
		super();

		this.setTag(Command.SYNC_TASKPRO_CHILD_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncTaskChildReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String taskId = reqCmd.getTaskId();
		int version = reqCmd.getVersion();
		List<TaskProBaseInfo> tasks = null;

		try {
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			int maxVersion = syncVerService.queryMax(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD);
			if (version != maxVersion) {
				int updateTime = syncVerService.queryUpdateTime(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD,
						version);

				// Logic: query uncompleted task(base info) which modify after
				// lastTryTime
				tasks = taskService.queryLastUpdateChildTask(taskId, updateTime);
				String porjectName = groupService.queryGroupName(taskId);
				for (TaskProBaseInfo childTask : tasks) {
					childTask.setPname(porjectName);
					
					int childVersion = syncVerService.queryMax(childTask.getId(), GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO);
					childTask.setVer(childVersion);

					// Logic: get member rank
					short rank = groupService.queryMemberRank(childTask.getId(), this.getMyAccountId());
					childTask.setMemberRank(rank);
				}
			}

			SyncTaskChildResp respCmd = new SyncTaskChildResp(sequence, ErrorCode.SUCCESS, taskId, maxVersion, tasks);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncTaskChildResp respCmd = new SyncTaskChildResp(sequence, ErrorCode.UNKNOWN_FAILURE, taskId, 0, tasks);
			return respCmd;
		}
	}

	private SyncTaskChildReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskChildAdapter.class);

}
