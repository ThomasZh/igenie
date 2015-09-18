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
import com.oct.ga.comm.cmd.task.DeleteTaskNoteReq;
import com.oct.ga.comm.cmd.task.DeleteTaskNoteResp;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class DeleteTaskNoteAdapter
		extends StpReqCommand
{
	public DeleteTaskNoteAdapter()
	{
		super();

		this.setTag(Command.DELETE_TASK_NOTE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new DeleteTaskNoteReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String noteId = reqCmd.getNoteId();
		String taskId = reqCmd.getTaskId();

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");

			taskService.removeNote(noteId, currentTimestamp);

			int num = taskService.countTaskNoteNumber(taskId);
			groupService.modifyNoteNum(taskId, num, currentTimestamp);

			syncVerService.increase(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_NOTE, currentTimestamp,
					this.getMyAccountId(), this.getTag());
			// if add note, task info version must increase.
			syncVerService.increase(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			TaskProExtInfo task = taskService.query(taskId);

			// if task has parent(project), modify project's
			// childTaskNumber.
			String pid = task.getPid();
			if (pid != null && pid.length() > 0) {
				syncVerService.increase(pid, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD, currentTimestamp,
						this.getMyAccountId(), this.getTag());
			}

			DeleteTaskNoteResp respCmd = new DeleteTaskNoteResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			DeleteTaskNoteResp respCmd = new DeleteTaskNoteResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private DeleteTaskNoteReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(DeleteTaskNoteAdapter.class);
}
