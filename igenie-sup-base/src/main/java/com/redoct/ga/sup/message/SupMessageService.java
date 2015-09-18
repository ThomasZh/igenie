package com.redoct.ga.sup.message;

import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.MessageOriginalMulticast;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;

public interface SupMessageService
{
	public void send(MessageOriginalMulticast msg, int timestamp)
			throws SupSocketException;

	public GaInvite sendInivte(GaInvite invite, int timestamp)
			throws SupSocketException;

	public String sendInivteFeedback(String inviteId, short state, String fromAccountId, String fromAccountName,
			String fromAccountAvatarUrl, int timestamp)
			throws SupSocketException;

	public void sendModifyApplyState(String channelId, short state, String txt, String fromAccountId,
			String fromAccountName, String fromAccountAvatarUrl, String toAccountId, int timestamp)
			throws SupSocketException;

	public void sendApply(GaApplyStateNotify msg, int timestamp)
			throws SupSocketException;

	public void sendActivityJoin(String groupName, String leaderId, String fromAccountName, int timestamp)
			throws SupSocketException;

	public void sendMsgFlow(MsgFlowBasicInfo msg, int timestamp)
			throws SupSocketException;
}
