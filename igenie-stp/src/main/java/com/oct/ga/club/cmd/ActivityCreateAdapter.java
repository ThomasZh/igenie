package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.StringUtil;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ActivityCreateReq;
import com.oct.ga.comm.cmd.club.ActivityCreateResp;
import com.oct.ga.comm.domain.apply.GaApplicantTemplateCell;
import com.oct.ga.comm.domain.club.ActivityCreateInfo;
import com.oct.ga.comm.domain.desc.GaDescChapter;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.domain.publish.GaPublishLoc;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.service.GaDescService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaPublishService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.message.SupMessageService;

public class ActivityCreateAdapter
		extends StpReqCommand
{
	public ActivityCreateAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_CREATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityCreateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String fromAccountId = this.getMyAccountId();
		String fromAccountName = (String) session.getAttribute("accountName");
		String fromAccountAvatarUrl = (String) session.getAttribute("avatarUrl");
		ActivityCreateResp respCmd = null;
		ActivityCreateInfo activity = reqCmd.getActivity();
		String[] subscriberIds = reqCmd.getSubscriberIds();
		List<GaPublishLoc> locations = reqCmd.getLocations();
		List<GaDescChapter> descChapters = reqCmd.getDescChapters();

		String activityId = null;

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");
			GaPublishService publishService = (GaPublishService) context.getBean("gaPublishService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

			activityId = activityService.create(activity, fromAccountId, currentTimestamp);
			groupService.createGroup(activityId, activity.getName(), GlobalArgs.CHANNEL_TYPE_ACTIVITY,
					currentTimestamp, this.getMyAccountId());
			syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			groupService.joinAsLeader(activityId, fromAccountId, currentTimestamp);
			syncVerService.increase(activityId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			// Logic: add myself to subscribe
			activityService.addSubscribe(activityId, activityId, fromAccountId, GlobalArgs.SYNC_STATE_READ,
					currentTimestamp);

			if (activity.getApplyFormType() == GlobalArgs.TRUE) {
				// Logic: add ApplicantTemplate
				List<GaApplicantTemplateCell> contactCells = activity.getContactCells();
				List<GaApplicantTemplateCell> participationCells = activity.getParticipationCells();

				// reorder the seq of array
				for (int n = 0; n < contactCells.size(); n++) {
					GaApplicantTemplateCell contactCell = contactCells.get(n);
					contactCell.setSeq(n + 1);
				}
				for (int n = 0; n < participationCells.size(); n++) {
					GaApplicantTemplateCell participationCell = participationCells.get(n);
					participationCell.setSeq(n + 1);
				}

				Gson gson = new Gson();
				String contactJson = gson.toJson(contactCells);
				String participationJson = gson.toJson(participationCells);
				logger.debug("contactJson order by seq: " + contactJson);
				logger.debug("participationJson order by seq: " + participationJson);

				applyService.modifyApplicantTemplate(activityId, contactJson, participationJson, currentTimestamp);
			}

			if (activity.getPublishType() == GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PUBLIC) {
				List<GaPublishLoc> array = new ArrayList<GaPublishLoc>();

				if (locations == null || locations.size() == 0) {
					short seq = 1;
					GaPublishLoc loc = new GaPublishLoc();
					loc.setSeq(seq);
					loc.setLocX(activity.getLocX());
					loc.setLocY(activity.getLocY());
					loc.setLocDesc(activity.getLocDesc());
					String locMask = StringUtil.locMask(activity.getLocX(), activity.getLocY());
					loc.setLocMask(locMask);

					array.add(loc);
				} else {
					for (GaPublishLoc location : locations) {
						String locMask = StringUtil.locMask(location.getLocX(), location.getLocY());
						location.setLocMask(locMask);
						array.add(location);
					}
				}

				publishService.modifyPublishLoc(activityId, array, currentTimestamp);
			}

			GaDescService descService = (GaDescService) context.getBean("gaDescService");
			if (descChapters != null) {
				for (GaDescChapter descChapter : descChapters) {
					descService.modify(activityId, descChapter.getSeq(), descChapter, currentTimestamp);
				}
			}

			IoSession session = this.getSession();
			respCmd = new ActivityCreateResp(ErrorCode.SUCCESS, activityId);
			respCmd.setSequence(sequence);
			TlvObject tResp = CommandParser.encode(respCmd);

			WriteFuture future = session.write(tResp);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (!future.isWritten()) {
				// The messsage couldn't be written out completely for
				// some reason. (e.g. Connection is closed)
				logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
						+ ErrorCode.CONNECTION_CLOSED
						+ "]|couldn't be written out resp completely for some reason.(e.g. Connection is closed)");

				session.close(true);
			} else {
				logger.info("create an activity success");
			}

			GaTaskLog log = new GaTaskLog();
			log.setLogId(UUID.randomUUID().toString());
			log.setChannelId(activityId);
			log.setFromAccountId(fromAccountId);
			log.setActionTag(GlobalArgs.TASK_ACTION_ADD);
			log.setToActionId(activityId);
			taskService.addLog(log, currentTimestamp);

			taskService.addLogExtend(log.getLogId(), fromAccountId, activityId, GlobalArgs.TASK_ACTION_ADD,
					GlobalArgs.SYNC_STATE_RECEIVED, currentTimestamp);

			String groupName = groupService.queryGroupName(activityId);
			// Logic: add to subscribe
			if (subscriberIds != null && subscriberIds.length > 0) {
				for (String id : subscriberIds) {
					activityService.addSubscribe(activityId, activityId, id, GlobalArgs.SYNC_STATE_NOT_RECEIVED,
							currentTimestamp);
					if (!id.equals(fromAccountId)) {
						short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
						taskService.addLogExtend(log.getLogId(), id, activityId, GlobalArgs.TASK_ACTION_ADD, syncState,
								currentTimestamp);

						// TODO send notify to friends
						try {
							MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
							msgFlowBasicInfo.setLogId(log.getLogId());
							msgFlowBasicInfo.setFromAccountId(fromAccountId);
							msgFlowBasicInfo.setFromAccountName(fromAccountName);
							msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
							msgFlowBasicInfo.setToActionAccountId(id);
							msgFlowBasicInfo.setToActionId(log.getChannelId());
							msgFlowBasicInfo.setActionTag(log.getActionTag());
							msgFlowBasicInfo.setChannelId(log.getChannelId());
							msgFlowBasicInfo.setChannelName(groupName);

							SupMessageService supMessageService = (SupMessageService) context
									.getBean("supMessageService");

							supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
						} catch (Exception e) {
							logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
									+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
									+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE
									+ "]|send task log notify message error: " + LogErrorMessage.getFullInfo(e));
						}
					}
				}
			}

			try {
				String exerciseActivityId = taskService.modifyExerciseProject2Completed(this.getMyAccountId(),
						GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_EXERCISE_3, currentTimestamp);

				if (exerciseActivityId != null) {
					GaTaskLog exerciseLog = new GaTaskLog();
					exerciseLog.setLogId(UUID.randomUUID().toString());
					exerciseLog.setChannelId(exerciseActivityId);
					exerciseLog.setFromAccountId(this.getMyAccountId());
					exerciseLog.setActionTag(GlobalArgs.TASK_ACTION_COMPLETED);
					exerciseLog.setToActionId(exerciseActivityId);
					taskService.addLog(exerciseLog, currentTimestamp);

					taskService.addLogExtend(exerciseLog.getLogId(), this.getMyAccountId(), exerciseActivityId,
							GlobalArgs.TASK_ACTION_COMPLETED, GlobalArgs.SYNC_STATE_READ, currentTimestamp);
				}
			} catch (Exception e) {
				logger.warn("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
						+ this.getMyAccountId() + "]|add task log error: " + LogErrorMessage.getFullInfo(e));
			}

			return null;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityCreateResp(ErrorCode.UNKNOWN_FAILURE, activityId);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityCreateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityCreateAdapter.class);

}
