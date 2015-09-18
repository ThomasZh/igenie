package com.oct.ga.stp.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.mina.core.future.WriteFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.base.QueryActivityBadgeNumberReq;
import com.oct.ga.comm.cmd.base.QueryActivityBadgeNumberResp;
import com.oct.ga.comm.domain.ActivityBadgeNumberJsonBean;
import com.oct.ga.comm.domain.BadgeNumberJsonBean;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.parser.StpCommandParser;

public class QueryActivityBadgeNumberAdapter
		extends StpReqCommand
{
	public QueryActivityBadgeNumberAdapter()
	{
		super();

		this.setTag(Command.QUERY_ACTIVITY_BADGE_NUMBER_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryActivityBadgeNumberReq().decode(tlv);

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

		QueryActivityBadgeNumberResp respCmd = null;
		String accountId = this.getMyAccountId();
		int lastTryTime = reqCmd.getLastTryTime();
		int monthAgo = currentTimestamp - 2419200;
		// if (monthAgo > lastTryTime)
		lastTryTime = monthAgo;

		try {
			List<BadgeNumberJsonBean> activityNumbers = new ArrayList<BadgeNumberJsonBean>();

			List<String> projectIds = taskService.queryUncompleteProjectIdsByAccount(accountId);
			for (String projectId : projectIds) {
				short channelType = taskService.queryChannelType(projectId);
				if (channelType == GlobalArgs.CHANNEL_TYPE_ACTIVITY || channelType == GlobalArgs.CHANNEL_TYPE_TASK) {
					BadgeNumberJsonBean activityNumber = taskService
							.queryBadgeNumber(projectId, accountId, lastTryTime);
					if (activityNumber.getNumber() > 0)
						activityNumbers.add(activityNumber);
				}
			}

//			List<NotifyTaskLog> logList = new ArrayList<NotifyTaskLog>();
//			for (BadgeNumberJsonBean activityNumber : activityNumbers) {
//				String projectId = activityNumber.getId();
//				NotifyTaskLog log = taskService.queryLastOneByProject(projectId, accountId, lastTryTime);
//				if (log.get_id() != null && log.get_id().length() > 0) {
//					short depth = groupService.queryDepth(log.getToChannelId());
//					log.setDepth(depth);
//
//					logList.add(log);
//				}
//			}

			ActivityBadgeNumberJsonBean activityBadge = new ActivityBadgeNumberJsonBean();
			activityBadge.setActivityNumber(activityNumbers);
//			activityBadge.setActivityList(logList);

			JSONObject jsonObject = JSONObject.fromObject(activityBadge);
			String json = jsonObject.toString();
			logger.debug(json);

			respCmd = new QueryActivityBadgeNumberResp(ErrorCode.SUCCESS, currentTimestamp, json);
			respCmd.setSequence(sequence);
			TlvObject tlvResp = StpCommandParser.encode(respCmd);

			WriteFuture future = session.write(tlvResp);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (!future.isWritten()) {
				// The messsage couldn't be written out completely for
				// some reason. (e.g. Connection is closed)
				logger.warn("sessionId=["
						+ session.getId()
						+ "]|deviceId=["
						+ this.getMyDeviceId()
						+ "]|accountId=["
						+ this.getMyAccountId()
						+ "]|commandTag=["
						+ this.getTag()
						+ "]|ErrorCode=["
						+ ErrorCode.CONNECTION_CLOSED
						+ "]|couldn't be written out QueryActivityBadgeNumberResp completely for some reason.(e.g. Connection is closed)");
			}

			return null;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new QueryActivityBadgeNumberResp(ErrorCode.UNKNOWN_FAILURE, currentTimestamp, null);
		}

		respCmd.setSequence(sequence);
		return respCmd;
	}

	private QueryActivityBadgeNumberReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryActivityBadgeNumberAdapter.class);
}
