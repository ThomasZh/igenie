package com.redoct.ga.sup.session.client.socket;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.gatekeeper.SupServerInfo;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.client.socket.SupSocketClient;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;
import com.redoct.ga.sup.session.SupSessionService;
import com.redoct.ga.sup.session.cmd.ActiveStpSessionByTicketReq;
import com.redoct.ga.sup.session.cmd.ActiveStpSessionByTicketResp;
import com.redoct.ga.sup.session.cmd.ApplyGateTokenReq;
import com.redoct.ga.sup.session.cmd.ApplyGateTokenResp;
import com.redoct.ga.sup.session.cmd.ApplySessionTicketReq;
import com.redoct.ga.sup.session.cmd.ApplySessionTicketResp;
import com.redoct.ga.sup.session.cmd.InactiveStpSessionByTicketReq;
import com.redoct.ga.sup.session.cmd.InactiveStpSessionReq;
import com.redoct.ga.sup.session.cmd.QueryGateSessionReq;
import com.redoct.ga.sup.session.cmd.QueryGateSessionResp;
import com.redoct.ga.sup.session.cmd.QueryStpSessionByTicketReq;
import com.redoct.ga.sup.session.cmd.QueryStpSessionByTicketResp;
import com.redoct.ga.sup.session.cmd.QueryStpSessionReq;
import com.redoct.ga.sup.session.cmd.QueryStpSessionResp;
import com.redoct.ga.sup.session.cmd.RemoveStpSessionByTicketReq;
import com.redoct.ga.sup.session.cmd.RemoveStpSessionReq;
import com.redoct.ga.sup.session.cmd.VerifyGateTokenReq;
import com.redoct.ga.sup.session.domain.GateSession;
import com.redoct.ga.sup.session.domain.StpSession;

public class SessionServiceSocketImpl
		implements SupSessionService
{
	@Override
	public String applyGateToken(GateSession gateSession)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				ApplyGateTokenReq reqCmd = new ApplyGateTokenReq(gateSession);
				logger.info("request cmd: " + reqCmd.getTag());

				SupRespCommand respCmd = socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					ApplyGateTokenResp applyGateTokenResp = (ApplyGateTokenResp) respCmd;
					String gateToken = applyGateTokenResp.getGateToken();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return gateToken;
					else
						throw new SupSocketException("unknow failure.");
				} else {
					throw new SupSocketException("unknow failure.");
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public boolean verifyGateToken(String gateToken, String deviceId)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				VerifyGateTokenReq reqCmd = new VerifyGateTokenReq(gateToken, deviceId);
				logger.info("request cmd: " + reqCmd.getTag());

				SupRespCommand respCmd = socketClient.send(addr, reqCmd);
				if (respCmd != null && respCmd.getRespState() == ErrorCode.SUCCESS) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					return true;
				} else
					return false;
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public StpSession queryStpSession(String accountId)
			throws SupSocketException
	{
		SupSocketConnectionManager socketConnectionManager = GenericSingleton
				.getInstance(SupSocketConnectionManager.class);
		SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

		if (supServer == null) {
			throw new SupSocketException("sup account server is not avilable");
		} else {
			InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

			QueryStpSessionReq reqCmd = new QueryStpSessionReq(accountId);
			logger.info("request cmd: " + reqCmd.getTag());

			SupRespCommand respCmd = socketClient.send(addr, reqCmd);
			if (respCmd != null) {
				logger.info("response cmd: " + respCmd.getTag());
				logger.debug("response state: " + respCmd.getRespState());
				QueryStpSessionResp queryStpSessionResp = (QueryStpSessionResp) respCmd;
				StpSession stpSession = queryStpSessionResp.getStpSession();
				if (respCmd != null && respCmd.getRespState() == ErrorCode.SUCCESS)
					return stpSession;
				else
					throw new SupSocketException("unknow failure.");
			} else
				throw new SupSocketException("unknow failure.");
		}
	}

	@Override
	public GateSession queryGateSession(String deviceId)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				QueryGateSessionReq reqCmd = new QueryGateSessionReq(deviceId);
				logger.info("request cmd: " + reqCmd.getTag());

				SupRespCommand respCmd = socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					QueryGateSessionResp queryGateSessionResp = (QueryGateSessionResp) respCmd;
					GateSession gateSession = queryGateSessionResp.getGateSession();
					if (respCmd != null && respCmd.getRespState() == ErrorCode.SUCCESS)
						return gateSession;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public boolean removeStpSession(String accountId)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				RemoveStpSessionReq reqCmd = new RemoveStpSessionReq(accountId);
				logger.info("request cmd: " + reqCmd.getTag());

				SupRespCommand respCmd = socketClient.send(addr, reqCmd);
				logger.info("response cmd: " + respCmd.getTag());
				logger.debug("response state: " + respCmd.getRespState());
				if (respCmd.getRespState() == ErrorCode.SUCCESS)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public boolean inactiveStpSession(String accountId)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				InactiveStpSessionReq reqCmd = new InactiveStpSessionReq(accountId);
				logger.info("request cmd: " + reqCmd.getTag());

				SupRespCommand respCmd = socketClient.send(addr, reqCmd);
				if (respCmd != null && respCmd.getRespState() == ErrorCode.SUCCESS) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					return true;
				} else
					return false;
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public StpSession activeStpSession(String sessionTicket, long ioSessionId)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				ActiveStpSessionByTicketReq reqCmd = new ActiveStpSessionByTicketReq(sessionTicket, ioSessionId);
				logger.info("request cmd: " + reqCmd.getTag());

				SupRespCommand respCmd = socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					ActiveStpSessionByTicketResp activeStpSessionByTicketResp = (ActiveStpSessionByTicketResp) respCmd;
					StpSession stpSession = activeStpSessionByTicketResp.getStpSession();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return stpSession;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public String applySessionTicket(StpSession stpSession)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				ApplySessionTicketReq reqCmd = new ApplySessionTicketReq(stpSession);
				logger.info("request cmd: " + reqCmd.getTag());

				SupRespCommand respCmd = socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					ApplySessionTicketResp applySessionTicketResp = (ApplySessionTicketResp) respCmd;
					String sessionTicket = applySessionTicketResp.getSessionTicket();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return sessionTicket;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public StpSession queryStpSessionByTicket(String sessionTicket)
			throws SupSocketException
	{
		SupSocketConnectionManager socketConnectionManager = GenericSingleton
				.getInstance(SupSocketConnectionManager.class);
		SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

		if (supServer == null) {
			throw new SupSocketException("sup account server is not avilable");
		} else {
			InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

			QueryStpSessionByTicketReq reqCmd = new QueryStpSessionByTicketReq(sessionTicket);
			logger.info("request cmd: " + reqCmd.getTag());

			SupRespCommand respCmd = socketClient.send(addr, reqCmd);
			if (respCmd != null) {
				logger.info("response cmd: " + respCmd.getTag());
				logger.debug("response state: " + respCmd.getRespState());
				QueryStpSessionByTicketResp queryStpSessionResp = (QueryStpSessionByTicketResp) respCmd;
				StpSession stpSession = queryStpSessionResp.getStpSession();
				if (respCmd != null && respCmd.getRespState() == ErrorCode.SUCCESS)
					return stpSession;
				else
					throw new SupSocketException("unknow failure.");
			} else
				throw new SupSocketException("unknow failure.");
		}
	}

	@Override
	public boolean removeStpSessionByTicket(String sessionTicket)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				RemoveStpSessionByTicketReq reqCmd = new RemoveStpSessionByTicketReq(sessionTicket);
				logger.info("request cmd: " + reqCmd.getTag());

				SupRespCommand respCmd = socketClient.send(addr, reqCmd);
				logger.info("response cmd: " + respCmd.getTag());
				logger.debug("response state: " + respCmd.getRespState());
				if (respCmd.getRespState() == ErrorCode.SUCCESS)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public boolean inactiveStpSessionByTicket(String sessionTicket)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SESSION_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				InactiveStpSessionByTicketReq reqCmd = new InactiveStpSessionByTicketReq(sessionTicket);
				logger.info("request cmd: " + reqCmd.getTag());

				SupRespCommand respCmd = socketClient.send(addr, reqCmd);
				if (respCmd != null && respCmd.getRespState() == ErrorCode.SUCCESS) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					return true;
				} else
					return false;
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
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

	private final static Logger logger = LoggerFactory.getLogger(SessionServiceSocketImpl.class);

}
