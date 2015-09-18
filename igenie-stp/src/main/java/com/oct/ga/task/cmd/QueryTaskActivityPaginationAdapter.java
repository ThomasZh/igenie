package com.oct.ga.task.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.mina.core.future.WriteFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.task.QueryTaskActivityPaginationReq;
import com.oct.ga.comm.cmd.task.QueryTaskActivityPaginationResp;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class QueryTaskActivityPaginationAdapter
		extends StpReqCommand
{
	public QueryTaskActivityPaginationAdapter()
	{
		super();

		this.setTag(Command.QUERY_TASK_ACTIVITY_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryTaskActivityPaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String taskId = reqCmd.getTaskId();
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");

			Page<NotifyTaskLog> taskActivities = taskService.queryTaskActivityPagination(taskId, this.getMyAccountId(),
					0, pageNum, pageSize);
			List<NotifyTaskLog> array = taskActivities.getPageItems();

			for (NotifyTaskLog log : array) {
				short depth = groupService.queryDepth(log.getChannelId());
				log.setDepth(depth);

				log.setSendToAccountId(getMyAccountId());
			}
			JSONArray jsonArray = JSONArray.fromObject(array);
			String json = jsonArray.toString();
			logger.debug("json: " + json);

			QueryTaskActivityPaginationResp respCmd = new QueryTaskActivityPaginationResp(ErrorCode.SUCCESS);
			respCmd.setJson(json);
			respCmd.setSequence(sequence);
			TlvObject tlvResp = CommandParser.encode(respCmd);

			WriteFuture future = session.write(tlvResp);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (future.isWritten()) {
//				for (NotifyTaskLog log : array) {
//					if (log.getSyncState() == GlobalArgs.SYNC_STATE_NOT_RECEIVED)
//						taskService.updateActivityToReadState(log);
//				}

				int rows = taskService.batchUpdateActivityToReceivedState(taskId, this.getMyAccountId(),
						currentTimestamp);

				short num = badgeNumService.queryTaskLogNum(this.getMyAccountId());
				num -= rows;
				badgeNumService.modifyTaskLogNum(this.getMyAccountId(), num);
			} else {
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
						+ "]|couldn't be written out QueryTaskActivityPaginationResp completely for some reason.(e.g. Connection is closed)");
			}

			return null;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryTaskActivityPaginationResp respCmd = new QueryTaskActivityPaginationResp(ErrorCode.UNKNOWN_FAILURE);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private QueryTaskActivityPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryTaskActivityPaginationAdapter.class);

}
