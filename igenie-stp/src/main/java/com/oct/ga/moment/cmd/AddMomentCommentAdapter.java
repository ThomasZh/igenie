package com.oct.ga.moment.cmd;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.moment.AddCommentMomentReq;
import com.oct.ga.comm.cmd.moment.AddCommentMomentResp;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaMomentService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.message.SupMessageService;

public class AddMomentCommentAdapter
		extends StpReqCommand
{
	public AddMomentCommentAdapter()
	{
		super();

		this.setTag(Command.ADD_MOMENT_COMMENT_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new AddCommentMomentReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String momentId = reqCmd.getMomentId();
		String txt = reqCmd.getTxt();
		String myAccountId = this.getMyAccountId();
		String fromAccountId = this.getMyAccountId();
		String fromAccountName = (String) session.getAttribute("accountName");
		String fromAccountAvatarUrl = (String) session.getAttribute("avatarUrl");

		try {
			GaMomentService momentService = (GaMomentService) context.getBean("gaMomentService");
			GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			momentService.addMomentComment(momentId, myAccountId, txt, currentTimestamp);

			// int num = momentService.queryCommentNum(momentId);
			// momentService.modifyFavoriteNum(momentId, ++num);
			int num = momentService.countCommentNum(momentId);
			momentService.modifyCommentNum(momentId, num);

			String momentOwnerId = momentService.queryMomentOwner(momentId);
			momentService.addLog(momentId, myAccountId, GlobalArgs.TASK_ACTION_MOMENT_COMMENT, txt, momentOwnerId,
					currentTimestamp);
			// TODO send notify to moment owner

			String channelId = momentService.queryChannelId(momentId);
			GaTaskLog log = new GaTaskLog();
			log.setLogId(UUID.randomUUID().toString());
			log.setChannelId(channelId);
			log.setFromAccountId(this.getMyAccountId());
			log.setActionTag(GlobalArgs.TASK_ACTION_MOMENT_COMMENT);
			log.setToActionId(momentId);
			taskService.addLog(log, currentTimestamp);

			taskService.addLogExtend(log.getLogId(), myAccountId, channelId, GlobalArgs.TASK_ACTION_MOMENT_COMMENT,
					GlobalArgs.SYNC_STATE_RECEIVED, currentTimestamp);

			IoSession session = this.getSession();
			AddCommentMomentResp respCmd = new AddCommentMomentResp(sequence, ErrorCode.SUCCESS);
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
				logger.info("add moment comment success");
			}

			if (!myAccountId.equals(momentOwnerId)) {
				taskService.addLogExtend(log.getLogId(), momentOwnerId, channelId,
						GlobalArgs.TASK_ACTION_MOMENT_COMMENT, GlobalArgs.SYNC_STATE_NOT_RECEIVED, currentTimestamp);

				short badgeNum = badgeNumService.countMomentLogNum(momentOwnerId);
				badgeNumService.modifyMomentLogNum(momentOwnerId, badgeNum);

				String groupName = groupService.queryGroupName(channelId);
				// Logic send a notify to friends
				try {
					MsgFlowBasicInfo msgFlowBasicInfo = new MsgFlowBasicInfo();
					msgFlowBasicInfo.setLogId(log.getLogId());
					msgFlowBasicInfo.setFromAccountId(fromAccountId);
					msgFlowBasicInfo.setFromAccountName(fromAccountName);
					msgFlowBasicInfo.setFromAccountAvatarUrl(fromAccountAvatarUrl);
					msgFlowBasicInfo.setToActionAccountId(momentOwnerId);
					msgFlowBasicInfo.setToActionId(log.getChannelId());
					msgFlowBasicInfo.setActionTag(log.getActionTag());
					msgFlowBasicInfo.setChannelId(log.getChannelId());
					msgFlowBasicInfo.setChannelName(groupName);

					SupMessageService supMessageService = (SupMessageService) context.getBean("supMessageService");
					supMessageService.sendMsgFlow(msgFlowBasicInfo, currentTimestamp);
				} catch (Exception e) {
					logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId()
							+ "]|accountId=[" + this.getMyAccountId() + "]|commandTag=[" + this.getTag()
							+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|send task log notify message error: "
							+ LogErrorMessage.getFullInfo(e));
				}
			}

			return null;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			AddCommentMomentResp respCmd = new AddCommentMomentResp(sequence, ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private AddCommentMomentReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(AddMomentCommentAdapter.class);

}
