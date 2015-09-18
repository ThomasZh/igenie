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
import com.oct.ga.comm.cmd.task.SyncTaskDetailReq;
import com.oct.ga.comm.cmd.task.SyncTaskDetailResp;
import com.oct.ga.comm.domain.club.ClubDetailInfo;
import com.oct.ga.comm.domain.taskpro.TaskProDetailInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaClubService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

//return TaskProDetailInfo
public class SyncTaskDetailAdapter
		extends StpReqCommand
{
	public SyncTaskDetailAdapter()
	{
		super();

		this.setTag(Command.SYNC_TASKPRO_DETAIL_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncTaskDetailReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String taskId = reqCmd.getTaskId();
		int version = reqCmd.getVersion();
		TaskProDetailInfo taskDetail = null;

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");

			int maxVersion = syncVerService.queryMax(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO);
			if (version == maxVersion) {
				SyncTaskDetailResp respCmd = new SyncTaskDetailResp(sequence, ErrorCode.SYNC_VER_SAME, taskDetail);
				return respCmd;
			} else {
				int updateTime = syncVerService
						.queryUpdateTime(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO, version);

				taskDetail = taskService.queryTaskDetailLastUpdate(taskId, updateTime);
				taskDetail.setVer(maxVersion);

				String pid = taskDetail.getPid();
				if (taskDetail.getDepth() != 0) {
					String pname = groupService.queryGroupName(pid);

					taskDetail.setPname(pname);
				}

				short channelType = groupService.queryChannelType(taskId);
				switch (channelType) {
				case GlobalArgs.CHANNEL_TYPE_ACTIVITY:
					ClubDetailInfo club = clubService.queryDetail(pid);

					logger.debug("title background image url: " + club.getTitleBkImage());
					taskDetail.setTitleBkImage(club.getTitleBkImage());
					break;
				case GlobalArgs.CHANNEL_TYPE_TASK:
				}

				short memberRank = groupService.queryMemberRank(taskId, this.getMyAccountId());
				taskDetail.setMemberRank(memberRank);

				short memberState = groupService.queryMemberState(taskId, this.getMyAccountId());
				taskDetail.setMemberState(memberState);

				short memberAvailableNum = groupService.queryMemberAvailableNum(taskId);
				taskDetail.setMemberAvailableNum(memberAvailableNum);

				short approveType = activityService.queryApproveType(taskId);
				taskDetail.setApproveType(approveType);

				short applyFormType = activityService.queryApplyFormType(taskId);
				taskDetail.setApplyFormType(applyFormType);

				SyncTaskDetailResp respCmd = new SyncTaskDetailResp(sequence, ErrorCode.SUCCESS, taskDetail);
				return respCmd;
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncTaskDetailResp respCmd = new SyncTaskDetailResp(sequence, ErrorCode.UNKNOWN_FAILURE, taskDetail);
			return respCmd;
		}
	}

	private SyncTaskDetailReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskDetailAdapter.class);

}
