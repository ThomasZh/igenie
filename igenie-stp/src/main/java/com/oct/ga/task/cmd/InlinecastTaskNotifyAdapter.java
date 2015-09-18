package com.oct.ga.task.cmd;

import java.io.UnsupportedEncodingException;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.inlinecast.InlinecastTaskNotifyReq;
import com.oct.ga.comm.cmd.task.SyncTaskActivityResp;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class InlinecastTaskNotifyAdapter
		extends StpReqCommand
{
	public InlinecastTaskNotifyAdapter()
	{
		super();

		this.setTag(Command.INLINECAST_TASK_ACTIVITY_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new InlinecastTaskNotifyReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		NotifyTaskLog activity = reqCmd.getTaskActivity();

		try {
			GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");


			// session.getService().getManagedSessions();
			IoService ioSerive = session.getService();

			IoSession ioSession = ioSerive.getManagedSessions().get(activity.getReciverIoSessionId());
			if (ioSession == null) { // this session is not online now!
				logger.warn("ioSession is null!");
				return null;
			}

			SyncTaskActivityResp syncTaskActivityResp = new SyncTaskActivityResp(activity);
			TlvObject tSyncTaskActivityResp = CommandParser.encode(syncTaskActivityResp);

			WriteFuture future = ioSession.write(tSyncTaskActivityResp);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (future.isWritten()) {
				taskService.updateActivityToReadState(activity);
				
				GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
				short num = badgeNumService.queryTaskLogNum(activity.getSendToAccountId());
				badgeNumService.modifyTaskLogNum(activity.getSendToAccountId(), --num);
			} else {
				// The messsage couldn't be written out completely for
				// some
				// reason.
				// (e.g. Connection is closed)
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
						+ "]|couldn't be written out syncTaskActivityResp completely for some reason.(e.g. Connection is closed)");
			}
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
		}

		// Warning: OldStpEventHandler do not response anything.
		return null;
	}

	private InlinecastTaskNotifyReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(InlinecastTaskNotifyAdapter.class);

}
