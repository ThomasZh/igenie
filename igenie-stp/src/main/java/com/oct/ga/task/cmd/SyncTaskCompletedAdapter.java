package com.oct.ga.task.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.base.SyncTimestampResp;
import com.oct.ga.comm.cmd.task.SyncTaskBaseResp;
import com.oct.ga.comm.cmd.task.SyncTaskCompletedReq;
import com.oct.ga.comm.domain.taskpro.TaskProBaseInfo;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class SyncTaskCompletedAdapter
		extends StpReqCommand
{
	public SyncTaskCompletedAdapter()
	{
		super();

		this.setTag(Command.SYNC_TASKPRO_COMPLETED_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncTaskCompletedReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		int lastTryTime = reqCmd.getLastTryTime();
		String json = null;

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			// Logic: query uncompleted project ids which create after lastTryTime
			List<String> projectIds = taskService.queryCompletedProjectIds(this.getMyAccountId(), lastTryTime);
			
			List<TaskProBaseInfo> projects = new ArrayList<TaskProBaseInfo>();
			for (String projectId : projectIds) {
				TaskProBaseInfo projectBaseInfo = taskService.queryTaskBaseInfo(projectId);

				short memberRank = groupService.queryMemberRank(projectId, this.getMyAccountId());
				projectBaseInfo.setMemberRank(memberRank);
				
				projects.add(projectBaseInfo);
			}
			
			JSONArray jsonArray = JSONArray.fromObject(projects);
			json = jsonArray.toString();
			logger.debug("json: " + json);
			
			SyncTaskBaseResp syncTaskBaseResp = new SyncTaskBaseResp(json);
			TlvObject syncTaskBaseTlv = CommandParser.encode(syncTaskBaseResp);
			session.write(syncTaskBaseTlv);

			
			SyncTimestampResp respCmd = new SyncTimestampResp(Command.SYNC_TASKPRO_COMPLETED_REQ, currentTimestamp);
			TlvObject tResp = CommandParser.encode(respCmd);
			session.write(tResp);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
		}

		return null;
	}

	private SyncTaskCompletedReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncTaskCompletedAdapter.class);

}
