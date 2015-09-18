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
import com.oct.ga.comm.cmd.task.SyncTaskNoteReq;
import com.oct.ga.comm.cmd.task.SyncTaskNoteResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.task.TaskNote;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class SyncTaskNoteAdapter
		extends StpReqCommand
{
	public SyncTaskNoteAdapter()
	{
		super();

		this.setTag(Command.SYNC_TASKPRO_NOTE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncTaskNoteReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String taskId = reqCmd.getTaskId();
		int version = reqCmd.getVersion();
		List<TaskNote> notes = null;
		int maxVersion = 0;

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			maxVersion = syncVerService.queryMax(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_NOTE);
			if (version != maxVersion) {
				int updateTime = syncVerService
						.queryUpdateTime(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_NOTE, version);

				notes = taskService.queryLastUpdateTaskNotes(taskId, updateTime);

				for (TaskNote note : notes) {
					// id, firstName, imageFileTransId
					AccountBasic masterInfo = accountService.queryAccount(note.getAccountId());
					note.setAccountName(masterInfo.getNickname());
				}
			}

			SyncTaskNoteResp syncTaskNoteResp = new SyncTaskNoteResp(sequence, ErrorCode.SUCCESS, taskId, maxVersion,
					notes);
			return syncTaskNoteResp;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncTaskNoteResp syncTaskNoteResp = new SyncTaskNoteResp(sequence, ErrorCode.UNKNOWN_FAILURE, taskId,
					maxVersion, notes);
			return syncTaskNoteResp;
		}
	}

	private SyncTaskNoteReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskNoteAdapter.class);

}
