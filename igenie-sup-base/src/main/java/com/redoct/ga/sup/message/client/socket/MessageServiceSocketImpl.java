package com.redoct.ga.sup.message.client.socket;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.domain.gatekeeper.SupServerInfo;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.MessageOriginalMulticast;
import com.oct.ga.comm.domain.msgflow.MsgFlowBasicInfo;
import com.redoct.ga.sup.client.socket.SupSocketClient;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;
import com.redoct.ga.sup.message.SupMessageService;
import com.redoct.ga.sup.message.cmd.MultcastActivityJoinReq;
import com.redoct.ga.sup.message.cmd.MultcastActivityJoinResp;
import com.redoct.ga.sup.message.cmd.MultcastApplyStateReq;
import com.redoct.ga.sup.message.cmd.MultcastApplyStateResp;
import com.redoct.ga.sup.message.cmd.MultcastInviteFeedbackReq;
import com.redoct.ga.sup.message.cmd.MultcastInviteFeedbackResp;
import com.redoct.ga.sup.message.cmd.MultcastInviteReq;
import com.redoct.ga.sup.message.cmd.MultcastInviteResp;
import com.redoct.ga.sup.message.cmd.MultcastMessageReq;
import com.redoct.ga.sup.message.cmd.MultcastMessageResp;
import com.redoct.ga.sup.message.cmd.MultcastModifyApproveStateReq;
import com.redoct.ga.sup.message.cmd.MultcastModifyApproveStateResp;
import com.redoct.ga.sup.message.cmd.MultcastTaskLogReq;
import com.redoct.ga.sup.message.cmd.MultcastTaskLogResp;

public class MessageServiceSocketImpl
		implements SupMessageService
{
	@Override
	public void send(MessageOriginalMulticast msg, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_MESSAGE_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup message server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				MultcastMessageReq reqCmd = new MultcastMessageReq(timestamp, msg);
				logger.info("request cmd: " + reqCmd.getTag());

				MultcastMessageResp respCmd = (MultcastMessageResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public GaInvite sendInivte(GaInvite invite, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_MESSAGE_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup message server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				MultcastInviteReq reqCmd = new MultcastInviteReq(invite);
				logger.info("request cmd: " + reqCmd.getTag());

				MultcastInviteResp respCmd = (MultcastInviteResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					GaInvite rsInvite = respCmd.getInvite();
					return rsInvite;
				} else {
					throw new SupSocketException("unknow failure.");
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public String sendInivteFeedback(String inviteId, short state, String fromAccountId, String fromAccountName,
			String fromAccountAvatarUrl, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_MESSAGE_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup message server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				MultcastInviteFeedbackReq reqCmd = new MultcastInviteFeedbackReq(inviteId, state, fromAccountId,
						fromAccountName, fromAccountAvatarUrl);
				logger.info("request cmd: " + reqCmd.getTag());

				MultcastInviteFeedbackResp respCmd = (MultcastInviteFeedbackResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					String rsInviteId = respCmd.getInviteId();
					return rsInviteId;
				} else {
					throw new SupSocketException("unknow failure.");
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public void sendModifyApplyState(String channelId, short state, String txt, String fromAccountId,
			String fromAccountName, String fromAccountAvatarUrl, String toAccountId, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_MESSAGE_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup message server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				MultcastModifyApproveStateReq reqCmd = new MultcastModifyApproveStateReq(channelId, state, txt,
						fromAccountId, fromAccountName, fromAccountAvatarUrl, toAccountId);
				logger.info("request cmd: " + reqCmd.getTag());

				MultcastModifyApproveStateResp respCmd = (MultcastModifyApproveStateResp) socketClient.send(addr,
						reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public void sendApply(GaApplyStateNotify msg, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_MESSAGE_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup message server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				MultcastApplyStateReq reqCmd = new MultcastApplyStateReq(msg);
				logger.info("request cmd: " + reqCmd.getTag());

				MultcastApplyStateResp respCmd = (MultcastApplyStateResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public void sendActivityJoin(String groupName, String leaderId, String fromAccountName, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_MESSAGE_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup message server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				MultcastActivityJoinReq reqCmd = new MultcastActivityJoinReq(groupName, leaderId, fromAccountName,
						timestamp);
				logger.info("request cmd: " + reqCmd.getTag());

				MultcastActivityJoinResp respCmd = (MultcastActivityJoinResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public void sendMsgFlow(MsgFlowBasicInfo msg, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_MESSAGE_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup message server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				MultcastTaskLogReq reqCmd = new MultcastTaskLogReq(msg);
				logger.info("request cmd: " + reqCmd.getTag());

				MultcastTaskLogResp respCmd = (MultcastTaskLogResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	// /////////////////////////////////////////////////////

	private SupSocketClient socketClient;

	public SupSocketClient getSocketClient()
	{
		return socketClient;
	}

	public void setSocketClient(SupSocketClient socketClient)
	{
		this.socketClient = socketClient;
	}

	private final static Logger logger = LoggerFactory.getLogger(MessageServiceSocketImpl.class);

}
