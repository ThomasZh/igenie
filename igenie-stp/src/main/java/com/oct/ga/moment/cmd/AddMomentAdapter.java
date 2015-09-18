package com.oct.ga.moment.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.StringUtil;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.moment.AddMomentReq;
import com.oct.ga.comm.cmd.moment.AddMomentResp;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class AddMomentAdapter
		extends StpReqCommand
{
	public AddMomentAdapter()
	{
		super();

		this.setTag(Command.ADD_MOMENT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new AddMomentReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String channelId = reqCmd.getChannelId();
		String momentId = reqCmd.getMomentId();
		String desc = reqCmd.getDesc();
		String[] photos = reqCmd.getPhotos();
		int photoNum = 0;

		try {
			GaMomentService momentService = (GaMomentService) context.getBean("gaMomentService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");

			if (photos != null)
				photoNum = photos.length;

			String superId = "00000000-0000-0000-0000-000000000000";
			short channelType = groupService.queryChannelType(channelId);
			switch (channelType) {
			case GlobalArgs.CHANNEL_TYPE_ACTIVITY:
			case GlobalArgs.CHANNEL_TYPE_TASK:
				TaskProExtInfo task = taskService.query(channelId);
				superId = task.getPid();

				GaTaskLog log = new GaTaskLog();
				log.setLogId(UUID.randomUUID().toString());
				log.setChannelId(channelId);
				log.setFromAccountId(this.getMyAccountId());
				log.setActionTag(GlobalArgs.TASK_ACTION_ADD_ATTACH);
				log.setToActionId(momentId);
				taskService.addLog(log, currentTimestamp);

				// send this message to online member
				List<GroupMemberDetailInfo> taskMembers = groupService.queryMembers(task.getId());
				for (GroupMemberDetailInfo taskMember : taskMembers) {
					short syncState = GlobalArgs.SYNC_STATE_NOT_RECEIVED;
					// do not notify sender.
					if (this.getMyAccountId().equals(taskMember.getAccountId())) {
						syncState = GlobalArgs.SYNC_STATE_RECEIVED;
					}

					taskService.addLogExtend(log.getLogId(), taskMember.getAccountId(), channelId,
							GlobalArgs.TASK_ACTION_ADD_ATTACH, syncState, currentTimestamp);
				}// end of member loop
			}

			momentService.addMoment(superId, channelId, momentId, this.getMyAccountId(), desc, photoNum,
					currentTimestamp);
			for (int i = 0; i < photoNum; i++) {
				String imageUrl = photos[i];
				String photoId = StringUtil.parserIdFromImageUtrl(imageUrl);
				momentService.addMomentPhoto(superId, channelId, momentId, i, photoId, photos[i], currentTimestamp);
			}

			int num = momentService.countPhotoNum(channelId);
			groupService.modifyAttachmentNum(channelId, num, currentTimestamp);
			syncVerService.increase(channelId, GlobalArgs.SYNC_VERSION_TYPE_TASK_FILE, currentTimestamp,
					this.getMyAccountId(), this.getTag());
			syncVerService.increase(channelId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, currentTimestamp,
					this.getMyAccountId(), this.getTag());

			AddMomentResp respCmd = new AddMomentResp(ErrorCode.SUCCESS);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			AddMomentResp respCmd = new AddMomentResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private AddMomentReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(AddMomentAdapter.class);

}
