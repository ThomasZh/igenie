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
import com.oct.ga.comm.cmd.task.UploadTaskNoteReq;
import com.oct.ga.comm.cmd.task.UploadTaskNoteResp;
import com.oct.ga.comm.domain.task.TaskNote;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class UploadTaskNoteAdapter
		extends StpReqCommand
{
	public UploadTaskNoteAdapter()
	{
		super();

		this.setTag(Command.UPLOAD_TASK_NOTE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new UploadTaskNoteReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		TaskNote note = reqCmd.getNote();

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");

			note.setAccountId(this.getMyAccountId());
			boolean isExist = taskService.isExistNote(note.getNoteId());
			if (isExist) {
				taskService.modify(note, currentTimestamp);
			} else {
				taskService.add(note, currentTimestamp);
				int num = taskService.countTaskNoteNumber(note.getTaskId());
				groupService.modifyNoteNum(note.getTaskId(), num, currentTimestamp);

				// if add note, task info version must increase.
				syncVerService.increase(note.getTaskId(), GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
						this.getMyAccountId(), this.getTag());
			}

			syncVerService.increase(note.getTaskId(), GlobalArgs.SYNC_VERSION_TYPE_TASK_NOTE, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			TaskProExtInfo task = taskService.query(note.getTaskId());

			// if task has parent(project), modify project's
			// childTaskNumber.
			String pid = task.getPid();
			if (pid != null && pid.length() > 0) {
				syncVerService.increase(pid, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD, currentTimestamp,
						this.getMyAccountId(), this.getTag());
			}

			UploadTaskNoteResp respCmd = new UploadTaskNoteResp(sequence, ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			UploadTaskNoteResp respCmd = new UploadTaskNoteResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private UploadTaskNoteReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(UploadTaskNoteAdapter.class);
}
