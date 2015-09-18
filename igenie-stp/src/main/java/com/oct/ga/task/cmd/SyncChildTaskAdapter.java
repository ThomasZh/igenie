package com.oct.ga.task.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.task.SyncChildTaskReq;
import com.oct.ga.comm.cmd.task.SyncChildTaskResp;
import com.oct.ga.comm.domain.taskext.ChildTaskMaster;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class SyncChildTaskAdapter
		extends StpReqCommand
{
	public SyncChildTaskAdapter()
	{
		super();

		this.setTag(Command.SYNC_CHILD_TASK_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncChildTaskReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String projectId = reqCmd.getProjectId();
		int version = reqCmd.getVersion();
		String json = null;
		SyncChildTaskResp respCmd = null;

		try {
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			int maxVersion = syncVerService.queryMax(projectId, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD);
			if (version != maxVersion) {
				int updateTime = syncVerService.queryUpdateTime(projectId, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD,
						version);

				// query uncompleted child task which modify after lastTryTime
				List<ChildTaskMaster> tasks = taskService.queryLastModifyChildTask(projectId, updateTime);
				for (ChildTaskMaster childTask : tasks) {
					// Logic: get your member rank
					short rank = groupService.queryMemberRank(childTask.getId(), this.getMyAccountId());
					childTask.setRank(rank);
				}

				JSONArray jsonArray = JSONArray.fromObject(tasks);
				json = jsonArray.toString();
				logger.debug("json: " + json);
			}

			respCmd = new SyncChildTaskResp(ErrorCode.SUCCESS, projectId, maxVersion, json);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new SyncChildTaskResp(ErrorCode.UNKNOWN_FAILURE, projectId, 0, json);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private SyncChildTaskReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncChildTaskAdapter.class);

}
