package com.oct.ga.jpush;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.domain.msg.JPushMessage;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.domain.msg.OfflineMessageJsonBean;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.oct.ga.service.GaOfflineNotifyService;

public class JPushServiceImpl
		implements GaOfflineNotifyService
{
	@Override
	public void sendMessage(boolean isOnline, String jpushToken, int badgeNum, MessageInlinecast message)
	{
		JPushMessage jpush = new JPushMessage();
		jpush.setOnline(isOnline);
		jpush.setTitle(message.getChannelName());

		if (isOnline) {
			OfflineMessageJsonBean msg = new OfflineMessageJsonBean();
			msg.setMsgId(message.get_id());
			msg.setContentType(message.getContentType());
			msg.setChannelType(message.getChannelType());
			msg.setChannelId(message.getChannelId());
			msg.setChannelName(message.getChannelName());
			msg.setChatId(message.getChatId());
			msg.setFromAccountAvatarUrl(message.getFromAccountId());
			msg.setFromAccountName(message.getFromAccountName());
			msg.setFromAccountAvatarUrl(message.getFromAccountAvatarUrl());
			msg.setContent(message.getContent());
			msg.setTimestamp(message.getTimestamp());

			Gson gson = new Gson();
			String json = gson.toJson(msg);
			jpush.setMsgContent(json);
		} else {
			String txt = "";
			if (message.getContent() != null && message.getContent().length() > 0) {
				if (message.getContent().length() > 200)
					txt = message.getContent().substring(0, 200) + "...";
				else
					txt = message.getContent();
			}
			jpush.setMsgContent(message.getFromAccountName() + ": " + txt);

			jpush.putAttr("chatId", message.getChatId());
		}

		logger.debug("jpush msgContent: " + jpush.getMsgContent());

		jpush.setAlias(jpushToken);
		jpush.setTimestamp(message.getTimestamp());

		jpushMq.push(jpush);
	}

	@Override
	public void sendInvite(boolean isOnline, String jpushToken, int badgeNum, GaInvite invite)
	{
		int currentTimestamp = DatetimeUtil.currentTimestamp();
		JPushMessage jpush = new JPushMessage();
		jpush.setOnline(isOnline);
		jpush.setTitle(invite.getChannelName());

		if (isOnline) {
			OfflineMessageJsonBean msg = new OfflineMessageJsonBean();
			msg.setMsgId(invite.getInviteId());
			msg.setContentType(invite.getInviteType());
			msg.setChannelType(invite.getChannelType());
			msg.setChannelId(invite.getChannelId());
			msg.setChannelName(invite.getChannelName());
			msg.setFromAccountId(invite.getFromAccountId());
			msg.setFromAccountName(invite.getFromAccountName());
			msg.setFromAccountAvatarUrl(invite.getFromAccountAvatarUrl());
			msg.setTimestamp(currentTimestamp);

			Gson gson = new Gson();
			String json = gson.toJson(msg);
			jpush.setMsgContent(json);
		} else {
			switch (invite.getInviteType()) {
			case GlobalArgs.INVITE_TYPE_FOLLOW_ME:
				jpush.setMsgContent(invite.getFromAccountName() + ": invite as friend");
				break;
			case GlobalArgs.INVITE_TYPE_JOIN_ACTIVITY:
				jpush.setMsgContent(invite.getFromAccountName() + ": welcome to join activity");
				break;
			}

			jpush.putAttr("chatId", invite.getChannelId());
			jpush.putAttr("fromAccountName", invite.getFromAccountName());
		}

		logger.debug("jpush msgContent: " + jpush.getMsgContent());

		jpush.setAlias(jpushToken);
		jpush.setTimestamp(currentTimestamp);

		jpushMq.push(jpush);
	}

	@Override
	public void sendInviteFeedback(boolean isOnline, String jpushToken, int badgeNum, GaInviteFeedback feedback)
	{
		int currentTimestamp = DatetimeUtil.currentTimestamp();
		JPushMessage jpush = new JPushMessage();
		jpush.setOnline(isOnline);
		jpush.setTitle(feedback.getFeedbackChannelName());

		if (isOnline) {
			OfflineMessageJsonBean msg = new OfflineMessageJsonBean();
			msg.setMsgId(feedback.getInviteId());
			msg.setContentType(feedback.getFeedbackState());
			msg.setChannelType(feedback.getFeedbackChannelType());
			msg.setChannelId(feedback.getFeedbackChannelId());
			msg.setChannelName(feedback.getFeedbackChannelName());
			msg.setChatId(feedback.getFeedbackChannelId());
			msg.setFromAccountId(feedback.getFeedbackUserId());
			msg.setFromAccountName(feedback.getFeedbackUserName());
			msg.setFromAccountAvatarUrl(feedback.getFeedbackUserAvatarUrl());
			msg.setTimestamp(currentTimestamp);

			Gson gson = new Gson();
			String json = gson.toJson(msg);
			jpush.setMsgContent(json);
		} else {
			switch (feedback.getFeedbackState()) {
			case GlobalArgs.INVITE_STATE_ACCPET:
				jpush.setMsgContent(feedback.getFeedbackUserName() + ": accept your invite");
				// jpush.setMsgContent(feedback.getFeedbackUserName() +
				// ": 接受邀请!");
				break;
			case GlobalArgs.INVITE_STATE_REJECT:
				jpush.setMsgContent(feedback.getFeedbackUserName() + ": reject your invite");
				// jpush.setMsgContent(feedback.getFeedbackUserName() +
				// ": 拒绝邀请!");
				break;
			}

			jpush.putAttr("chatId", feedback.getFeedbackChannelId());
			jpush.putAttr("fromAccountName", feedback.getFeedbackUserName());
		}

		logger.debug("jpush msgContent: " + jpush.getMsgContent());

		jpush.setAlias(jpushToken);
		jpush.setTimestamp(currentTimestamp);

		jpushMq.push(jpush);
	}

	@Override
	public void sendActivityJoin(boolean isOnline, String jpushToken, int badgeNum, String activityName,
			String memberName)
	{
		JPushMessage jpush = new JPushMessage();
		jpush.setTitle(activityName);
		jpush.setMsgContent(memberName + ": join");
		jpush.setAlias(jpushToken);
		// jpush.setTimestamp(message.getTimestamp());
		jpushMq.push(jpush);
	}

	@Override
	public void sendApplyState(boolean isOnline, String jpushToken, int badgenum, GaApplyStateNotify notify)
	{
		int currentTimestamp = DatetimeUtil.currentTimestamp();
		JPushMessage jpush = new JPushMessage();
		jpush.setOnline(isOnline);
		jpush.setTitle(notify.getChannelName());

		if (isOnline) {
			OfflineMessageJsonBean msg = new OfflineMessageJsonBean();

			msg.setMsgId(notify.getMsgId());
			msg.setContentType(notify.getAction());
			msg.setChannelType(GlobalArgs.CHANNEL_TYPE_APPLY);
			msg.setChannelId(notify.getChannelId());
			msg.setChannelName(notify.getChannelName());
			msg.setFromAccountId(notify.getFromAccountId());
			msg.setFromAccountName(notify.getFromAccountName());
			msg.setFromAccountAvatarUrl(notify.getFromAccountAvatarUrl());
			msg.setTimestamp(currentTimestamp);
			msg.setContent(notify.getTxt());

			Gson gson = new Gson();
			String json = gson.toJson(msg);
			jpush.setMsgContent(json);
		} else {
			switch (notify.getAction()) {
			case GlobalArgs.INVITE_STATE_ACCPET:
				jpush.setMsgContent(notify.getFromAccountName() + ": apply has been accepted!");
				// jpush.setMsgContent(notify.getFromAccountName() +
				// ": 接收申请, 批准加入!");
				break;
			case GlobalArgs.INVITE_STATE_APPLY:
				jpush.setMsgContent(notify.getFromAccountName() + ": apply to join activity.");
				// jpush.setMsgContent(notify.getFromAccountName() +
				// ": 申请加入活动");
				break;
			case GlobalArgs.INVITE_STATE_JOIN:
				jpush.setMsgContent(notify.getFromAccountName() + ": join activity.");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 加入活动.");
				break;
			case GlobalArgs.INVITE_STATE_REFILL:
				jpush.setMsgContent(notify.getFromAccountName() + ": please refill the applicant!");
				// jpush.setMsgContent(notify.getFromAccountName() +
				// ": 重新填写信息!");
				break;
			case GlobalArgs.INVITE_STATE_REJECT:
				jpush.setMsgContent(notify.getFromAccountName() + ": apply has been reject!");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 拒绝申请!");
				break;
			}

			jpush.putAttr("chatId", notify.getChannelId());
			jpush.putAttr("fromAccountName", notify.getFromAccountName());
		}

		logger.debug("jpush msgContent: " + jpush.getMsgContent());

		jpush.setAlias(jpushToken);
		jpush.setTimestamp(currentTimestamp);

		jpushMq.push(jpush);
	}

	@Override
	public void sendTaskLog(boolean isOnline, String notifyToken, int badgenum, MsgFlowBasicInfo notify)
	{
		int currentTimestamp = DatetimeUtil.currentTimestamp();
		JPushMessage jpush = new JPushMessage();
		jpush.setOnline(isOnline);
		jpush.setTitle(notify.getChannelName());

		if (isOnline) {
			OfflineMessageJsonBean msg = new OfflineMessageJsonBean();

			msg.setMsgId(notify.getLogId());
			msg.setContentType(notify.getActionTag());
			msg.setChannelType(GlobalArgs.CHANNEL_TYPE_TASK_ACTIVITY);
			msg.setChannelId(notify.getChannelId());
			msg.setChannelName(notify.getChannelName());
			msg.setFromAccountId(notify.getFromAccountId());
			msg.setFromAccountName(notify.getFromAccountName());
			msg.setFromAccountAvatarUrl(notify.getFromAccountAvatarUrl());
			msg.setTimestamp(currentTimestamp);

			Gson gson = new Gson();
			String json = gson.toJson(msg);
			jpush.setMsgContent(json);
		} else {

			switch (notify.getActionTag()) {
			case GlobalArgs.TASK_ACTION_ADD: // create
				jpush.setMsgContent(notify.getFromAccountName() + ": new activity, please join it!");
				// jpush.setMsgContent(nofity.getFromAccountName() +
				// ": 创建活动, 邀请加入!");
				break;
			case GlobalArgs.TASK_ACTION_RECOMMEND: // recommend
				jpush.setMsgContent(notify.getFromAccountName() + ": recommend an interesting activity!");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 推荐活动!");
				break;
			case GlobalArgs.TASK_ACTION_ADD_ATTACH: // moment
				jpush.setMsgContent(notify.getFromAccountName() + ": upload a picture!");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 上传图片!");
				break;
			case GlobalArgs.TASK_ACTION_MOMENT_FAVORITE:
				jpush.setMsgContent(notify.getFromAccountName() + ": favorite your picture!");
				// jpush.setMsgContent(notify.getFromAccountName() +
				// ": 赞了您的图片!");
				break;
			case GlobalArgs.TASK_ACTION_MOMENT_COMMENT:
				jpush.setMsgContent(notify.getFromAccountName() + ": comment your picture!");
				// jpush.setMsgContent(notify.getFromAccountName() +
				// ": 评论了您的图片!");
				break;
			case GlobalArgs.TASK_ACTION_CHANGE_TIME:
				jpush.setMsgContent(notify.getFromAccountName() + ": modify activity time!");
				// jpush.setMsgContent(notify.getFromAccountName() +
				// ": 修改了活动时间!");
				break;
			case GlobalArgs.TASK_ACTION_CANCELED:
				jpush.setMsgContent(notify.getFromAccountName() + ": canceled!");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 取消活动!");
				break;
			case GlobalArgs.TASK_ACTION_COMPLETED:
				jpush.setMsgContent(notify.getFromAccountName() + ": completed!");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 活动完成!");
				break;
			case GlobalArgs.TASK_ACTION_UNCOMPLETED:
				jpush.setMsgContent(notify.getFromAccountName() + ": rework it!");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 恢复活动!");
				break;
			case GlobalArgs.TASK_ACTION_JOIN:
				jpush.setMsgContent(notify.getFromAccountName() + ": join!");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 加入活动!");
				break;
			case GlobalArgs.TASK_ACTION_QUIT:
				jpush.setMsgContent(notify.getFromAccountName() + ": quit!");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 退出活动!");
				break;
			case GlobalArgs.TASK_ACTION_APPLY:
				jpush.setMsgContent(notify.getFromAccountName() + ": I'd like to join this activity!");
				// jpush.setMsgContent(notify.getFromAccountName() +
				// ": 申请加入活动!");
				break;
			case GlobalArgs.TASK_ACTION_ACCEPT:
				jpush.setMsgContent(notify.getFromAccountName() + ": apply has been accepted!");
				// jpush.setMsgContent(notify.getFromAccountName() +
				// ": 接受申请，批准加入活动!");
				break;
			case GlobalArgs.TASK_ACTION_REJECT:
				jpush.setMsgContent(notify.getFromAccountName() + ": apply has been rejected!");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 拒绝申请!");
				break;
			case GlobalArgs.TASK_ACTION_REFILL:
				jpush.setMsgContent(notify.getFromAccountName() + ": please refill the information!");
				// jpush.setMsgContent(notify.getFromAccountName() +
				// ": 请重新填写申请信息!");
				break;
			case GlobalArgs.TASK_ACTION_KICKOUT_MEMBER:
				jpush.setMsgContent(notify.getFromAccountName() + ": kickout!");
				// jpush.setMsgContent(notify.getFromAccountName() + ": 踢出活动!");
				break;
			}

			jpush.putAttr("chatId", notify.getChannelId());
			jpush.putAttr("fromAccountName", notify.getFromAccountName());
		}

		logger.debug("jpush msgContent: " + jpush.getMsgContent());

		jpush.setAlias(notifyToken);
		jpush.setTimestamp(currentTimestamp);

		jpushMq.push(jpush);
	}

	// //////////////////////////////////////////////////////////////

	@Override
	public void sendTaskLog(boolean isOnline, String jpushToken, int badgeNum, NotifyTaskLog log)
	{
		// TODO Auto-generated method stub

	}

	private JPushMessageQueue jpushMq;

	public JPushMessageQueue getJpushMq()
	{
		return jpushMq;
	}

	public void setJpushMq(JPushMessageQueue jpushMq)
	{
		this.jpushMq = jpushMq;
	}

	private final static Logger logger = LoggerFactory.getLogger(JPushServiceImpl.class);

}
