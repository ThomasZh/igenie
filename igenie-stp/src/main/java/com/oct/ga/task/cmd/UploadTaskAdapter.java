package com.oct.ga.task.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.apache.mina.core.service.IoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.task.UploadTaskReq;
import com.oct.ga.comm.cmd.task.UploadTaskResp;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.domain.template.TemplateDefineJsonBean;
import com.oct.ga.comm.parser.PermissionModeParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.oct.ga.task.InlinecastTasklogServiceImpl;
import com.oct.ga.template.TemplateServiceImpl;

public class UploadTaskAdapter
		extends StpReqCommand
{
	public UploadTaskAdapter()
	{
		super();

		this.setTag(Command.UPLOAD_TASK_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new UploadTaskReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		TaskProExtInfo task = reqCmd.getTask();

		GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
		GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");

		// owner(rws), group(rws), other(rws)
		// rws: (read, write, share), eg:764
		// int permissionMode = PermissionModeParser.setMode(7, 7, 4);
		int permissionMode = task.getPermission();
		if (!judgeAuthorization(context, task, permissionMode)) {
			logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.NOT_ALLOW
					+ "]|has no allow to modify task=[" + task.getId() + "].");

			UploadTaskResp respCmd = new UploadTaskResp(ErrorCode.NOT_ALLOW, task.getId(), task.getVer());
			respCmd.setSequence(sequence);
			return respCmd;
		}

		try {
			if (!taskService.isExist(task.getId())) { // add
				task.setCreateAccountId(this.getMyAccountId());
				addTask(context, task);

				int ver = syncVerService.increase(task.getId(), GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO,
						currentTimestamp, this.getMyAccountId(), this.getTag());

				// if task has parent(project), modify project's
				// childTaskNumber.
				String pid = task.getPid();
				if (pid != null && pid.length() > 0) {
					int childNumber = taskService.countTaskChildNumber(pid);

					groupService.modifyChildNum(pid, childNumber, currentTimestamp);
					syncVerService.increase(pid, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD, currentTimestamp,
							this.getMyAccountId(), this.getTag());
				} else { // add project
					try {
						String activityId = taskService.modifyExerciseProject2Completed(this.getMyAccountId(),
								GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_3, currentTimestamp);

						if (activityId != null) {
							GaTaskLog log = new GaTaskLog();
							log.setLogId(UUID.randomUUID().toString());
							log.setChannelId(activityId);
							log.setFromAccountId(this.getMyAccountId());
							log.setActionTag(GlobalArgs.TASK_ACTION_COMPLETED);
							log.setToActionId(activityId);
							taskService.addLog(log, currentTimestamp);

							taskService.addLogExtend(log.getLogId(), this.getMyAccountId(), activityId,
									GlobalArgs.TASK_ACTION_COMPLETED, GlobalArgs.SYNC_STATE_READ, currentTimestamp);
						}
					} catch (Exception e) {
						logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
								+ "]|accountId=[" + this.getMyAccountId() + "]|add task log error: "
								+ LogErrorMessage.getFullInfo(e));
					}
				}

				UploadTaskResp respCmd = new UploadTaskResp(ErrorCode.SUCCESS, task.getId(), ver);
				respCmd.setSequence(sequence);
				return respCmd;
			} else { // update
				int maxVer = syncVerService.queryMax(task.getId(), GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO);
				if (task.getVer() != maxVer) {
					logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
							+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
							+ "]|ErrorCode=[" + ErrorCode.SYNC_VER_NOT_SAME + "]|sync version=[" + task.getVer()
							+ "] not same with max version=[" + maxVer + "],cann't modify task=[" + task.getId() + "].");

					UploadTaskResp respCmd = new UploadTaskResp(ErrorCode.SYNC_VER_NOT_SAME, task.getId(), maxVer);
					respCmd.setSequence(sequence);

					return respCmd;
				} else {
					if (groupService.isMember(task.getId(), this.getMyAccountId())) {
						updateTask(context, task);

						int ver = syncVerService.increase(task.getId(), GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO,
								currentTimestamp, this.getMyAccountId(), this.getTag());

						// if task has parent(project), modify project's
						// childTaskNumber.
						String pid = task.getPid();
						if (pid != null && pid.length() > 0) {
							syncVerService.increase(pid, GlobalArgs.SYNC_VERSION_TYPE_TASK_CHILD, currentTimestamp,
									this.getMyAccountId(), this.getTag());
						}

						UploadTaskResp respCmd = new UploadTaskResp(ErrorCode.SUCCESS, task.getId(), ver);
						respCmd.setSequence(sequence);
						return respCmd;
					} else {
						logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
								+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
								+ "]|ErrorCode=[" + ErrorCode.NOT_ALLOW + "]|has no allow to modify task=["
								+ task.getId() + "].");

						UploadTaskResp respCmd = new UploadTaskResp(ErrorCode.NOT_ALLOW, task.getId(), task.getVer());
						respCmd.setSequence(sequence);
						return respCmd;
					}
				}
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			UploadTaskResp respCmd = new UploadTaskResp(ErrorCode.UNKNOWN_FAILURE, task.getId(), task.getVer());
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private boolean judgeAuthorization(ApplicationContext context, TaskProExtInfo task, int permissionMode)
	{
		GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

		if (taskService.isExist(task.getId())) { // modify task
			// this account is task owner?
			if (groupService.isLeader(task.getId(), this.getMyAccountId())) {
				short ownerMode = PermissionModeParser.getOwnerMode(permissionMode);
				if (!PermissionModeParser.isWrite(ownerMode)) {
					return false;
				}
			} else {
				// this account is task member?
				if (groupService.isMember(task.getId(), this.getMyAccountId())) {
					short groupMode = PermissionModeParser.getGroupMode(permissionMode);
					if (!PermissionModeParser.isWrite(groupMode)) {
						return false;
					}
				} else {
					short otherMode = PermissionModeParser.getOtherMode(permissionMode);
					if (!PermissionModeParser.isWrite(otherMode)) {
						return false;
					}
				}
			}
		} else { // add task
			if (task.getPid() != null && task.getPid().length() > 0) {
				TaskProExtInfo parentTask = taskService.query(task.getPid());
				// int parentPermissionMode = PermissionModeParser.setMode(7, 7,
				// 4);
				int parentPermissionMode = parentTask.getPermission();

				// this account is parent task owner?
				if (parentTask.getCreateAccountId().equals(this.getMyAccountId())) {
					short ownerMode = PermissionModeParser.getOwnerMode(parentPermissionMode);
					if (!PermissionModeParser.isWrite(ownerMode)) {
						return false;
					}
				} else {
					short groupMode = PermissionModeParser.getGroupMode(parentPermissionMode);
					if (!PermissionModeParser.isWrite(groupMode)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	private void addTask(ApplicationContext context, TaskProExtInfo task)
			throws Exception
	{
		GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
		TemplateServiceImpl templateService = (TemplateServiceImpl) context.getBean("gaTemplateService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
		GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");

		short depth = 0;
		String pid = task.getPid();
		if (pid != null && pid.length() > 0) {
			depth++;
		}

		int permissionMode = 508; // default:774
		String templateId = task.getTemplateId();
		if (templateId == null || templateId.length() == 0 || templateId.equals("00000000-0000-0000-0000-000000000000")) {
			// if task has parent(project), add project's childTaskNumber.
			if (pid != null && pid.length() > 0) {
				TaskProExtInfo parentTask = taskService.query(pid);
				permissionMode = parentTask.getPermission();
			}
		} else {
			// LOGIC: get template permission mode.
			TemplateDefineJsonBean templateInfo = templateService.query(templateId, task.getTemplateVersion());
			permissionMode = templateInfo.getPermissionMode();
		}

		task.setPermission(permissionMode);
		taskService.add(task, currentTimestamp);

		// create task, and add this account into member as leader.
		groupService.createGroup(task.getId(), task.getName(), GlobalArgs.CHANNEL_TYPE_TASK, currentTimestamp,
				this.getMyAccountId(), depth);
		groupService.joinAsLeader(task.getId(), this.getMyAccountId(), currentTimestamp);
		syncVerService.increase(task.getId(), GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
				this.getMyAccountId(), this.getTag());
	}

	private void updateTask(ApplicationContext context, TaskProExtInfo task)
			throws UnsupportedEncodingException, InterruptedException, SupSocketException
	{
		GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
		TemplateServiceImpl templateService = (TemplateServiceImpl) context.getBean("gaTemplateService");
		InlinecastTasklogServiceImpl inlinecastTasklogService = (InlinecastTasklogServiceImpl) context
				.getBean("inlinecastTasklogService");
		GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

		IoService ioService = session.getService();
		inlinecastTasklogService.setIoService(ioService);

		TaskProExtInfo oldTask = taskService.query(task.getId());
		taskService.update(task, currentTimestamp);
		groupService.modifyGroupName(task.getId(), task.getName(), currentTimestamp);

		boolean isFeedbackUpdate = true; // default

		String templateId = task.getTemplateId();
		if (templateId == null || templateId.length() == 0 || templateId.equals("00000000-0000-0000-0000-000000000000")) {
		} else {
			// LOGIC: get template permission mode.
			TemplateDefineJsonBean templateInfo = templateService.query(templateId, oldTask.getTemplateVersion());
			isFeedbackUpdate = templateInfo.isFeedbackUpdate();
		}

		if (!isFeedbackUpdate) {
			return;
		}

		String pid = task.getPid();
		if (pid != null && pid.length() > 0) { // task
		} else {
			// if (task.getDepth() == 0) { // project
			// change time
			if (oldTask.getStartTime() != task.getStartTime() || oldTask.getEndTime() != task.getEndTime()) {
				GaTaskLog log = new GaTaskLog();
				log.setLogId(UUID.randomUUID().toString());
				log.setChannelId(task.getId());
				log.setFromAccountId(this.getMyAccountId());
				log.setActionTag(GlobalArgs.TASK_ACTION_CHANGE_TIME);
				log.setToActionId(task.getId());
				taskService.addLog(log, currentTimestamp);

				// send this message to online member
				List<GroupMemberDetailInfo> taskMembers = groupService.queryMembers(task.getId());
				for (GroupMemberDetailInfo taskMember : taskMembers) {
					short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
					// do not notify sender.
					if (this.getMyAccountId().equals(taskMember.getAccountId())) {
						syncState = GlobalArgs.SYNC_STATE_RECEIVED;
					}
					taskService.addLogExtend(log.getLogId(), taskMember.getAccountId(), task.getId(),
							GlobalArgs.TASK_ACTION_CHANGE_TIME, syncState, currentTimestamp);
				}// end of member loop
			}
		}
	}

	private UploadTaskReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(UploadTaskAdapter.class);

}
