package com.oct.ga.task.cmd;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.task.ModifyTaskMembersReq;
import com.oct.ga.comm.cmd.task.ModifyTaskMembersResp;
import com.oct.ga.comm.domain.GaResultSet;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.parser.PermissionModeParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class ModifyTaskMembersAdapter
		extends StpReqCommand
{
	public ModifyTaskMembersAdapter()
	{
		super();

		this.setTag(Command.TASKPRO_MODIFY_MEMBERS_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ModifyTaskMembersReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String taskId = reqCmd.getTaskId();
		String[] addIds = reqCmd.getAddUserIds();
		String[] removeIds = reqCmd.getRemoveUserIds();
		List<GaResultSet> rsArray = null;

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			TaskProExtInfo task = taskService.query(taskId);

			// PermissionMode: owner(rws), group(rws), other(rws)
			// rws: (read, write, share), eg:764
			// int permissionMode = PermissionModeParser.setMode(7, 7, 4);
			int permissionMode = task.getPermission();
			if (!this.judgeAuthorization(context, task, permissionMode)) {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.NOT_ALLOW + "]|has no allow to add task=[" + taskId + "] member.");

				ModifyTaskMembersResp respCmd = new ModifyTaskMembersResp(sequence, ErrorCode.NOT_ALLOW, taskId,
						rsArray);
				return respCmd;
			}

			rsArray = new ArrayList<GaResultSet>();
			if (addIds != null)
				for (String id : addIds) {
					GaResultSet rs = new GaResultSet();
					short errorCode = addTaskMember(context, task, id);
					rs.setAction(Command.ADD_TASK_MEMBER_REQ);
					rs.setId(id);
					rs.setErrorCode(errorCode);

					rsArray.add(rs);
				}

			if (removeIds != null)
				for (String id : removeIds) {
					GaResultSet rs = new GaResultSet();
					short errorCode = ErrorCode.SUCCESS;
					groupService.kickout(taskId, id, currentTimestamp, this.getMyAccountId());
					rs.setAction(Command.DELETE_TASK_MEMBER_REQ);
					rs.setId(id);
					rs.setErrorCode(errorCode);

					rsArray.add(rs);

					// TODO: log
				}

			syncVerService.increase(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
					this.getMyAccountId(), this.getTag());
			// if add/remove member, task info version must increase.
			syncVerService.increase(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			// if task has parent(project), modify project's
			// childTaskNumber.
			String pid = task.getPid();
			if (pid != null && pid.length() > 0) {
				syncVerService.increase(pid, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD, currentTimestamp,
						this.getMyAccountId(), this.getTag());
			}
			
			groupService.recountMemberAvailableNum(taskId, currentTimestamp);
			groupService.recountMemberNum(taskId, currentTimestamp);

			ModifyTaskMembersResp respCmd = new ModifyTaskMembersResp(sequence, ErrorCode.SUCCESS, taskId, rsArray);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			ModifyTaskMembersResp respCmd = new ModifyTaskMembersResp(sequence, ErrorCode.UNKNOWN_FAILURE, taskId,
					rsArray);
			return respCmd;
		}
	}

	private boolean judgeAuthorization(ApplicationContext context, TaskProExtInfo task, int permissionMode)
	{
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

		// this account is parent task owner?
		if (task.getCreateAccountId().equals(this.getMyAccountId())) {
			short ownerMode = PermissionModeParser.getOwnerMode(permissionMode);
			if (!PermissionModeParser.isShare(ownerMode)) {
				return false;
			}
		} else {
			// this account is child task member?
			if (groupService.isMember(task.getId(), this.getMyAccountId())) {
				short groupMode = PermissionModeParser.getGroupMode(permissionMode);
				if (!PermissionModeParser.isShare(groupMode)) {
					return false;
				}
			} else {
				short otherMode = PermissionModeParser.getOtherMode(permissionMode);
				if (!PermissionModeParser.isShare(otherMode)) {
					return false;
				}
			}
		}

		return true;
	}

	private short addTaskMember(ApplicationContext context, TaskProExtInfo task, String toAccountId)
			throws IOException, InterruptedException, SupSocketException
	{
		GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
		SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

		// GaInviteService3 inviteService = (GaInviteService3)
		// context.getBean("gaInviteService");
		// InlinecastInviteServiceImpl inlinecastInviteService =
		// (InlinecastInviteServiceImpl) context
		// .getBean("inlinecastInviteService");
		// InlinecastTasklogServiceImpl inlinecastTasklogService =
		// (InlinecastTasklogServiceImpl) context
		// .getBean("inlinecastTasklogService");
		//
		// IoService ioService = session.getService();
		// inlinecastInviteService.setIoService(ioService);
		// inlinecastTasklogService.setIoService(ioService);

		String taskId = task.getId();
		AccountBasic member = accountService.queryAccount(toAccountId);
		String toAccountName = member.getNickname();

		// this is a task
		if (task.getDepth() != 0) {
			if (groupService.isMember(taskId, toAccountId)) {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.MEMBER_ALREADY_EXIST + "]|this member already exist in task=[" + taskId + "].");
				return ErrorCode.MEMBER_ALREADY_EXIST;
			} else {
				groupService.joinAsMember(taskId, toAccountId, currentTimestamp);

				// not member before
				// Invite invite = new Invite();
				// invite.set_id(UUID.randomUUID().toString());
				// invite.setToChannelType(GlobalArgs.CHANNEL_TYPE_TASK);
				// invite.setFromAccountId(this.getMyAccountId());
				// invite.setFromAccountName(this.getMyAccountName());
				// invite.setToAccountId(toAccountId);
				// invite.setToAccountName(toAccountName);
				// invite.setInviteState(GlobalArgs.INVITE_STATE_DISPATCH);
				// invite.setTimestamp(currentTimestamp); // create time
				// invite.setCurrentTimestamp(currentTimestamp); //
				// lastUpdateTime
				// invite.setToChannelId(taskId);
				// invite.setToChannelName(task.getName());
				// invite.setAttachDesc(task.getDesc());
				// inviteService.add(invite);
				//
				// inlinecastInviteService.multicast(context, invite);

				// LOGIC: Add this action to task activity(log).
				NotifyTaskLog log = new NotifyTaskLog();
				log.set_id(UUID.randomUUID().toString());
				log.setChannelId(taskId);
				log.setTaskPid(task.getPid());
				log.setChannelName(task.getName());
				log.setFromAccountId(this.getMyAccountId());
				log.setFromAccountName(this.getMyAccountName());
				log.setCommandTag(Command.INVITE_REQ);
				log.setActivityState(GlobalArgs.INVITE_STATE_DISPATCH);
				log.setTimestamp(currentTimestamp);
				log.setToAccountId(toAccountId);
				log.setToAccountName(toAccountName);
				log.setSendToAccountId(toAccountId);
				log.setSendToAccountName(toAccountName);
				taskService.add(log);

				// inlinecastTasklogService.multicast(context, task, log);

				// send this message to online member
				List<GroupMemberDetailInfo> taskMembers = groupService.queryMembers(task.getId());
				for (GroupMemberDetailInfo taskMember : taskMembers) {
					// do not notify sender.
					if (this.getMyAccountId().equals(taskMember.getAccountId())) {
						taskService.addExtend(log.get_id(), taskMember.getAccountId(), GlobalArgs.SYNC_STATE_RECEIVED,
								currentTimestamp);
					} else {
						taskService.addExtend(log.get_id(), taskMember.getAccountId(), GlobalArgs.SYNC_STATE_NOT_RECEIVED,
								currentTimestamp);

						short num = badgeNumService.queryTaskLogNum(taskMember.getAccountId());
						badgeNumService.modifyTaskLogNum(taskMember.getAccountId(), ++num);
					}
				}// end of member loop

				return ErrorCode.SUCCESS;
			}
		} else { // This is a project.
			// LOGIC: toAccount is already project's member, do not
			// send an invite. just add into this task as member.
			if (groupService.isMember(taskId, toAccountId)) {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.MEMBER_ALREADY_EXIST + "]|this member already exist in project=[" + taskId + "].");

				return ErrorCode.MEMBER_ALREADY_EXIST;
			} else {
				groupService.applyWaitJoin(taskId, toAccountId, currentTimestamp, this.getMyAccountId());

				// Logic: this task channelType=Activity, make that user
				// subscribe this task(activity).
				if (task.getChannelType() == GlobalArgs.CHANNEL_TYPE_ACTIVITY) {
					GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");

					String subscriberId = toAccountId;
					if (activityService.isExistSubscribe(task.getId(), subscriberId)) {
						if (subscriberId.equals(this.getMyAccountId())) {
							activityService.updateSubscribe(task.getPid(), task.getId(), subscriberId,
									GlobalArgs.SYNC_STATE_READ, currentTimestamp);
						} else {
							activityService.updateSubscribe(task.getPid(), task.getId(), subscriberId,
									GlobalArgs.SYNC_STATE_NOT_RECEIVED, currentTimestamp);
						}
					} else {
						if (subscriberId.equals(this.getMyAccountId())) {
							activityService.addSubscribe(task.getPid(), task.getId(), subscriberId,
									GlobalArgs.SYNC_STATE_READ, currentTimestamp);
						} else {
							activityService.addSubscribe(task.getPid(), task.getId(), subscriberId,
									GlobalArgs.SYNC_STATE_NOT_RECEIVED, currentTimestamp);
						}
					}
				} else { // task
					// not member before
					// Invite invite = new Invite();
					// invite.set_id(UUID.randomUUID().toString());
					// invite.setToChannelType(GlobalArgs.CHANNEL_TYPE_TASK);
					// invite.setFromAccountId(this.getMyAccountId());
					// invite.setFromAccountName(this.getMyAccountName());
					// invite.setToAccountId(toAccountId);
					// invite.setToAccountName(toAccountName);
					// invite.setInviteState(GlobalArgs.INVITE_STATE_APPLY);
					// invite.setTimestamp(currentTimestamp); // create time
					// invite.setCurrentTimestamp(currentTimestamp); //
					// lastUpdateTime
					// invite.setToChannelId(taskId);
					// invite.setToChannelName(task.getName());
					// invite.setAttachDesc(task.getDesc());
					// inviteService.add(invite);
					//
					// // LOGIC: Send a invite message.
					// inlinecastInviteService.multicast(context, invite);

					// LOGIC: Add this action to task activity(log).
					NotifyTaskLog log = new NotifyTaskLog();
					log.set_id(UUID.randomUUID().toString());
					log.setChannelId(taskId);
					log.setTaskPid(task.getPid());
					log.setChannelName(task.getName());
					log.setFromAccountId(this.getMyAccountId());
					log.setFromAccountName(this.getMyAccountName());
					log.setCommandTag(Command.INVITE_REQ);
					log.setActivityState(GlobalArgs.INVITE_STATE_APPLY);
					log.setTimestamp(currentTimestamp);
					log.setToAccountId(toAccountId);
					log.setToAccountName(toAccountName);
					log.setSendToAccountId(toAccountId);
					log.setSendToAccountName(toAccountName);

					taskService.add(log);

					// send this message to online member
					List<GroupMemberDetailInfo> taskMembers = groupService.queryMembers(task.getId());
					for (GroupMemberDetailInfo taskMember : taskMembers) {
						// do not notify sender.
						if (this.getMyAccountId().equals(taskMember.getAccountId())) {
							taskService.addExtend(log.get_id(), taskMember.getAccountId(), GlobalArgs.SYNC_STATE_RECEIVED,
									currentTimestamp);
						} else {
							taskService.addExtend(log.get_id(), taskMember.getAccountId(), GlobalArgs.SYNC_STATE_NOT_RECEIVED,
									currentTimestamp);

							short num = badgeNumService.queryTaskLogNum(taskMember.getAccountId());
							badgeNumService.modifyTaskLogNum(taskMember.getAccountId(), ++num);
						}
					}// end of member loop
				}

				return ErrorCode.SUCCESS;
			}
		} // This task is a project.
	}

	private ModifyTaskMembersReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ModifyTaskMembersAdapter.class);

}
