package com.oct.ga.task.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.task.TaskMoveToReq;
import com.oct.ga.comm.cmd.task.TaskMoveToResp;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class TaskMoveToAdapter
		extends StpReqCommand
{
	public TaskMoveToAdapter()
	{
		super();

		this.setTag(Command.TASK_MOVE_TO_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new TaskMoveToReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String taskId = reqCmd.getTaskId();
		String projectId = reqCmd.getProjectId();

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");

			TaskProExtInfo task = taskService.query(taskId);
			String oldProjectId = task.getPid();
			taskService.moveto(taskId, projectId, currentTimestamp);

			// old project info version must increase.
			syncVerService.increase(oldProjectId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					this.getMyAccountId(), this.getTag());
			int num = taskService.countTaskChildNumber(oldProjectId);
			groupService.modifyNoteNum(oldProjectId, num, currentTimestamp);
			syncVerService.increase(oldProjectId, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			// new project info version must increase.
			syncVerService.increase(projectId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					this.getMyAccountId(), this.getTag());
			num = taskService.countTaskChildNumber(projectId);
			groupService.modifyNoteNum(oldProjectId, num, currentTimestamp);
			syncVerService.increase(projectId, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			TaskMoveToResp respCmd = new TaskMoveToResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			TaskMoveToResp respCmd = new TaskMoveToResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private TaskMoveToReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(TaskMoveToAdapter.class);

}
