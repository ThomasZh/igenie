package com.oct.ga.task.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.task.UpdateTaskStateReq;
import com.oct.ga.comm.cmd.task.UpdateTaskStateResp;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.message.SupMessageService;

public class UpdateTaskStateAdapter
		extends StpReqCommand
{
	public UpdateTaskStateAdapter()
	{
		super();

		this.setTag(Command.UPDATE_TASK_STATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new UpdateTaskStateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String taskId = reqCmd.getTaskId();
		short state = reqCmd.getState();
		String fromAccountId = this.getMyAccountId();
		String fromAccountName = (String) session.getAttribute("accountName");
		String fromAccountAvatarUrl = (String) session.getAttribute("avatarUrl");


		try {
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

			groupService.updateSate(taskId, state, currentTimestamp);
			// not complete => completed
			if (state == GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED) {
				taskService.modifyCompletedTime(taskId, currentTimestamp);
			}

			syncVerService.increase(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			TaskProExtInfo task = taskService.query(taskId);

			if (task.getDepth() == 0) { // project
				GaTaskLog log = new GaTaskLog();
				log.setLogId(UUID.randomUUID().toString());
				log.setChannelId(taskId);
				log.setFromAccountId(this.getMyAccountId());
				short actionTag = GlobalArgs.TASK_ACTION_COMPLETED;
				// not complete => completed
				if (state == GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED) {
					actionTag = GlobalArgs.TASK_ACTION_COMPLETED;
				} else {
					actionTag = GlobalArgs.TASK_ACTION_UNCOMPLETED;
				}
				log.setActionTag(actionTag);
				log.setToActionId(taskId);
				taskService.addLog(log, currentTimestamp);

				String groupName = groupService.queryGroupName(taskId);
				
				// send this message to online member
				List<GroupMemberDetailInfo> taskMembers = groupService.queryMembers(taskId);
				for (GroupMemberDetailInfo taskMember : taskMembers) {
					short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
					// do not notify sender.
					if (this.getMyAccountId().equals(taskMember.getAccountId())) {
						syncState = GlobalArgs.SYNC_STATE_RECEIVED;
					}
					taskService.addLogExtend(log.getLogId(), taskMember.getAccountId(), taskId, actionTag, syncState,
							currentTimestamp);

					try {
						SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");

						MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
						msgFlowBasicInfo.setLogId(log.getLogId());
						msgFlowBasicInfo.setFromAccountId(fromAccountId);
						msgFlowBasicInfo.setFromAccountName(fromAccountName);
						msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
						msgFlowBasicInfo.setToActionAccountId(taskMember.getAccountId());
						msgFlowBasicInfo.setToActionId(log.getChannelId());
						msgFlowBasicInfo.setActionTag(log.getActionTag());
						msgFlowBasicInfo.setChannelId(log.getChannelId());
						msgFlowBasicInfo.setChannelName(groupName);

						supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
					} catch (Exception e) {
						logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
								+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
								+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|send apply notify message error: "
								+ LogErrorMessage.getFullInfo(e));
					}
					// TODO badge number
					// short num =
					// badgeNumService.queryTaskLogNum(taskMember.getAccountId());
					// badgeNumService.modifyTaskLogNum(taskMember.getAccountId(),
					// ++num);
				}// end of member loop
			}

			// if task has parent(project), modify project's
			// childTaskNumber.
			String pid = task.getPid();
			if (pid != null && pid.length() > 0) {
				syncVerService.increase(pid, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD, currentTimestamp,
						this.getMyAccountId(), this.getTag());
			}

			UpdateTaskStateResp respCmd = new UpdateTaskStateResp(ErrorCode.SUCCESS);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			UpdateTaskStateResp respCmd = new UpdateTaskStateResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}// end of execute

	private UpdateTaskStateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(UpdateTaskStateAdapter.class);

}
