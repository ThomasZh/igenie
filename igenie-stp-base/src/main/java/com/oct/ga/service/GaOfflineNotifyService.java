package com.oct.ga.service;

import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;

public interface GaOfflineNotifyService
{
	public void sendMessage(boolean isOnline, String notifyToken, int badgenum, MessageInlinecast message);

	public void sendInvite(boolean isOnline, String notifyToken, int badgenum, GaInvite invite);

	public void sendInviteFeedback(boolean isOnline, String notifyToken, int badgenum, GaInviteFeedback feedback);

	public void sendActivityJoin(boolean isOnline, String notifyToken, int badgenum, String activityName,
			String memberName);

	public void sendApplyState(boolean isOnline, String notifyToken, int badgenum, GaApplyStateNotify notify);

	public void sendTaskLog(boolean isOnline, String notifyToken, int badgenum, NotifyTaskLog log);

	public void sendTaskLog(boolean isOnline, String notifyToken, int badgenum, MsgFlowBasicInfo notify);
}
