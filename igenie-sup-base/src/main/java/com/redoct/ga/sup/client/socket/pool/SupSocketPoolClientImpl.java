package com.redoct.ga.sup.client.socket.pool;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.cmd.RespCommand;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.client.socket.StpSocketEventHandler;
import com.redoct.ga.sup.client.socket.SupSocketClient;
import com.redoct.ga.sup.client.socket.SupSocketEventHandler;

public class SupSocketPoolClientImpl
		implements SupSocketClient
{
	/**
	 * currently received command mapped by message
	 */
	private RespCommand response;

	/**
	 * for outer use to call execute method
	 */
	public RespCommand getResponse()
	{
		return response;
	}
	
	@Override
	public SupRespCommand send(InetSocketAddress addr, SupReqCommand request)
			throws SupSocketException
	{
		IoSession session = null;
		SupSocketEventHandler eventHandler = null;

		try {
			session = pool.getResource(addr);
			eventHandler = (SupSocketEventHandler) session.getHandler();

			if (session.isConnected()) {
				logger.debug("sup client connected");

				WriteFuture writeFuture = session.write(request.encode());
				writeFuture.awaitUninterruptibly();

				if (writeFuture.getException() != null) {
					session.getConfig().setUseReadOperation(false);
					return null;
				}

				session.getConfig().setUseReadOperation(true);

				final ReadFuture readFuture = session.read();
				readFuture.awaitUninterruptibly();// read response
													// message

				if (readFuture.getException() != null) {
					session.getConfig().setUseReadOperation(false);
					return null;
				}

				//TlvObject tlv = (TlvObject) readFuture.getMessage();
				//SupRespCommand respCmd = (SupRespCommand) SupCommandParser.decode(tlv);
				SupRespCommand respCmd = eventHandler.getResponse();

				// stop blocking inbound messages
				session.getConfig().setUseReadOperation(false);

				pool.returnResourceObject(addr, session);

				return respCmd;
			} else {
				logger.warn("fail to connect: " + addr.getHostName() + ":" + addr.getPort() + ")");
				if (session != null)
					session.close(true);
				pool.returnBrokenResource(addr, session);
				throw new SupSocketException("fail to connect: " + addr.getHostName() + ":" + addr.getPort() + ")");
			}
		} catch (Exception e) {
			if (session != null)
				session.close(true);
			pool.returnBrokenResource(addr, session);
			throw new SupSocketException("unknown failure: ", e);
		}
	}

	@Override
	public RespCommand sendStpCommand(InetSocketAddress addr, ReqCommand request)
			throws SupSocketException
	{
		IoSession session = null;
		StpSocketEventHandler eventHandler = null;

		try {
			session = pool.getResource(addr);
			eventHandler = (StpSocketEventHandler) session.getHandler();

			if (session.isConnected()) {
				logger.debug("sup client connected");

				WriteFuture writeFuture = session.write(request.encode());
				writeFuture.awaitUninterruptibly();

				if (writeFuture.getException() != null) {
					session.getConfig().setUseReadOperation(false);
					return null;
				}

				session.getConfig().setUseReadOperation(true);

				final ReadFuture readFuture = session.read();
				readFuture.awaitUninterruptibly();// read response
													// message

				if (readFuture.getException() != null) {
					session.getConfig().setUseReadOperation(false);
					return null;
				}
				// stop blocking inbound messages
				session.getConfig().setUseReadOperation(false);

				RespCommand respCmd = eventHandler.getResponse();
				pool.returnResourceObject(addr, session);

				return respCmd;
			} else {
				logger.warn("fail to connect: " + addr.getHostName() + ":" + addr.getPort() + ")");
				pool.returnBrokenResource(addr, session);
				throw new SupSocketException("fail to connect: " + addr.getHostName() + ":" + addr.getPort() + ")");
			}
		} catch (Exception e) {
			logger.error(LogErrorMessage.getFullInfo(e));
			pool.returnBrokenResource(addr, session);
			throw new SupSocketException("unknown failure: ", e);
		}
	}

	// //////////////////////////////////////////////////////////////

	private SupSocketConnectionPool pool;

	public SupSocketConnectionPool getPool()
	{
		return pool;
	}

	public void setPool(SupSocketConnectionPool pool)
	{
		this.pool = pool;
	}

	private final static Logger logger = LoggerFactory.getLogger(SupSocketPoolClientImpl.class);
}
