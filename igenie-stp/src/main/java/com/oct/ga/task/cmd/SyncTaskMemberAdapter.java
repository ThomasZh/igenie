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
import com.oct.ga.comm.cmd.task.SyncTaskMemberReq;
import com.oct.ga.comm.cmd.task.SyncTaskMemberResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.group.GroupMemberMasterInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaSyncVerService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class SyncTaskMemberAdapter
		extends StpReqCommand
{
	public SyncTaskMemberAdapter()
	{
		super();

		this.setTag(Command.SYNC_TASKPRO_MEMBER_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncTaskMemberReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String taskId = reqCmd.getTaskId();
		int version = reqCmd.getVersion();
		List<GroupMemberMasterInfo> members = null;

		try {
			GaSyncVerService syncVerService = (GaSyncVerService) context.getBean("gaSyncVerService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");

			int maxVersion = syncVerService.queryMax(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_INFO);
			if (version != maxVersion) {
				int updateTime = syncVerService.queryUpdateTime(taskId, GlobalArgs.SYNC_VERSION_TYPE_TASK_MEMBER,
						version);

				// id, state, rank
				members = groupService.queryLastChangedMembersMasterInfo(taskId, updateTime);

				for (GroupMemberMasterInfo member : members) {
					// id, firstName, imageFileTransId
					AccountBasic masterInfo = accountService.queryAccount(member.getAccountId());
					member.setNickname(masterInfo.getNickname());
					member.setAvatarUrl(masterInfo.getAvatarUrl());
				}
			}

			SyncTaskMemberResp respCmd = new SyncTaskMemberResp(sequence, taskId, maxVersion, members);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncTaskMemberResp respCmd = new SyncTaskMemberResp(sequence, taskId, 0, members);
			return respCmd;
		}
	}

	private SyncTaskMemberReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskMemberAdapter.class);

}
