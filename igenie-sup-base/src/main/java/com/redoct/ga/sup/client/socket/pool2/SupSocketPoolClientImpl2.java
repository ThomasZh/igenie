package com.redoct.ga.sup.client.socket.pool2;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.cmd.RespCommand;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;
import com.redoct.ga.sup.client.socket.SupSocketClient;

public class SupSocketPoolClientImpl2
		implements SupSocketClient
{
	@Override
	public SupRespCommand send(InetSocketAddress addr, SupReqCommand request)
			throws SupSocketException
	{
		IoSession session = null;

		try {
			session = pool.getResource(addr);

			if (session.isConnected()) {
				logger.debug("sup client connected");

				WriteFuture writeFuture = session.write(request.encode());
				writeFuture.awaitUninterruptibly();

				Thread.sleep(30);

				RespCmdQueue respCmdQueue = GenericSingleton.getInstance(RespCmdQueue.class);
				SupRespCommand respCmd = respCmdQueue.pop(request.getSequence());
				if (respCmd != null) {
					logger.debug("got respCmd=[" + request.getSequence() + "] from command queue");
				} else {
					logger.warn("got respCmd=[" + request.getSequence() + "] from command queue again");

					// got response command again
					Thread.sleep(100);
					respCmd = respCmdQueue.pop(request.getSequence());

					if (respCmd == null) {
						logger.warn("got respCmd=[" + request.getSequence() + "] from command queue 3rd times");

						// got response command 3th times
						Thread.sleep(300);
						respCmd = respCmdQueue.pop(request.getSequence());

						if (respCmd == null) {
							logger.warn("got respCmd=[" + request.getSequence() + "] from command queue 4th times");

							// got response command 4th times
							Thread.sleep(500);
							respCmd = respCmdQueue.pop(request.getSequence());

							if (respCmd == null) {
								logger.warn("got respCmd=[" + request.getSequence() + "] from command queue 5th times");

								// got response command 4th times
								Thread.sleep(1000);
								respCmd = respCmdQueue.pop(request.getSequence());

								if (respCmd == null) {
									logger.error("There is no respCmd=[" + request.getSequence() + "] in command queue");
								}
							}
						}
					}
				}

				pool.returnResource(addr, session);

				return respCmd;
			} else {
				logger.warn("fail to connect: " + addr.getHostName() + ":" + addr.getPort() + ")");
				try {
					if (session != null)
						session.close(true);
				} catch (Exception e) {
				}
				throw new SupSocketException("fail to connect: " + addr.getHostName() + ":" + addr.getPort() + ")");
			}
		} catch (Exception e) {
			try {
				if (session != null)
					session.close(true);
			} catch (Exception e2) {
			}
			throw new SupSocketException("unknown failure: ", e);
		}
	}

	@Override
	public RespCommand sendStpCommand(InetSocketAddress addr, ReqCommand request)
			throws SupSocketException
	{
		IoSession session = null;

		try {
			session = pool.getResource(addr);

			if (session.isConnected()) {
				logger.debug("sup client connected");

				WriteFuture writeFuture = session.write(request.encode());
				writeFuture.awaitUninterruptibly();

				pool.returnResource(addr, session);

				return null;
			} else {
				logger.warn("fail to connect: " + addr.getHostName() + ":" + addr.getPort() + ")");
				try {
					if (session != null)
						session.close(true);
				} catch (Exception e) {
				}

				// getResource, try to connect again
				session = pool.getResource(addr);
				if (session.isConnected()) {
					logger.debug("sup client connected");

					WriteFuture writeFuture = session.write(request.encode());
					writeFuture.awaitUninterruptibly();

					pool.returnResource(addr, session);

					return null;
				} else {
					try {
						if (session != null)
							session.close(true);
					} catch (Exception e) {
					}
					throw new SupSocketException("fail to connect: " + addr.getHostName() + ":" + addr.getPort() + ")");
				}
			}
		} catch (Exception e) {
			logger.error(LogErrorMessage.getFullInfo(e));
			try {
				if (session != null)
					session.close(true);
			} catch (Exception e2) {
			}
			throw new SupSocketException("unknown failure: ", e);
		}
	}

	// //////////////////////////////////////////////////////////////

	private SupSocketConnectionPool2 pool;

	public SupSocketConnectionPool2 getPool()
	{
		return pool;
	}

	public void setPool(SupSocketConnectionPool2 pool)
	{
		this.pool = pool;
	}

	private final static Logger logger = LoggerFactory.getLogger(SupSocketPoolClientImpl2.class);
}
