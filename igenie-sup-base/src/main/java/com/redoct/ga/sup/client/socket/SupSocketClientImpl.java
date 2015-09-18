package com.redoct.ga.sup.client.socket;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommandParser;
import com.redoct.ga.sup.SupReqCommand;
import com.redoct.ga.sup.SupRespCommand;

public class SupSocketClientImpl
		implements SupSocketClient
{
	private IoConnector connector;
	private IoSession session;

	/**
	 * send request to sup server and wait for response
	 * 
	 * @param request
	 * @return response
	 */
	@Override
	public SupRespCommand send(InetSocketAddress addr, SupReqCommand request)
	{
		SupSocketEventHandler eventHandler = null;

		try {
			connector = new NioSocketConnector();
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
			connector.getSessionConfig().setReadBufferSize(GlobalArgs.BUFFER_SIZE); // 16k
			eventHandler = new SupSocketEventHandler();
			connector.setHandler(eventHandler);

			connector.setConnectTimeoutMillis(1000); // 设置超时
			ConnectFuture connectFuture = connector.connect(addr);
			logger.debug("sup client connect to(" + addr.getHostName() + ":" + addr.getPort() + ")");
			connectFuture.awaitUninterruptibly(); // 同步，等待，直到连接完成

			if (connectFuture.isDone()) {
				if (connectFuture.isConnected()) { // 若在指定时间内没连接成功，则抛出异常
					logger.debug("sup client connected");
					session = connectFuture.getSession();

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

					TlvObject tlv = (TlvObject) readFuture.getMessage();
					SupRespCommand respCmd = (SupRespCommand) SupCommandParser.decode(tlv);

					return respCmd;
				} else {
					logger.warn("fail to connect: " + addr.getHostName() + ":" + addr.getPort() + ")");
					// 不关闭的话，运行一段时间后抛出：too many open
					// files异常，导致无法创建新连接，服务器端挂掉
					connector.dispose(true);
					connector = null;

					return null;
				}
			}
			return null;
		} catch (Exception e) {
			logger.warn(LogErrorMessage.getFullInfo(e));
			return null;
		} finally {
			try {
				// execute every command, create socket connection.
				if (session != null) {
					session.close(true);
					session = null;
				}
				// 不关闭的话，运行一段时间后抛出：too many open files异常，导致无法创建新连接，服务器端挂掉
				if (connector != null) {
					connector.dispose(true);
					connector = null;
				}
				eventHandler = null;
			} catch (Exception e) {
				; // do nothing
			}
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(SupSocketClientImpl.class);

	@Override
	public RespCommand sendStpCommand(InetSocketAddress addr, ReqCommand request)
	{
		StpSocketEventHandler eventHandler = null;
		
		try {
			connector = new NioSocketConnector();
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
			connector.getSessionConfig().setReadBufferSize(GlobalArgs.BUFFER_SIZE); // 16k
			eventHandler = new StpSocketEventHandler();
			connector.setHandler(eventHandler);

			connector.setConnectTimeoutMillis(1000); // 设置超时
			ConnectFuture connectFuture = connector.connect(addr);
			logger.debug("sup client connect to(" + addr.getHostName() + ":" + addr.getPort() + ")");
			connectFuture.awaitUninterruptibly(); // 同步，等待，直到连接完成

			if (connectFuture.isDone()) {
				if (connectFuture.isConnected()) { // 若在指定时间内没连接成功，则抛出异常
					logger.debug("sup client connected");
					session = connectFuture.getSession();

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

					return respCmd;
				} else {
					logger.warn("fail to connect: " + addr.getHostName() + ":" + addr.getPort() + ")");
					// 不关闭的话，运行一段时间后抛出：too many open
					// files异常，导致无法创建新连接，服务器端挂掉
					connector.dispose(true);
					connector = null;

					return null;
				}
			}
			return null;
		} catch (Exception e) {
			logger.warn(LogErrorMessage.getFullInfo(e));
			return null;
		} finally {
			try {
				// execute every command, create socket connection.
				if (session != null) {
					session.close(true);
					session = null;
				}
				// 不关闭的话，运行一段时间后抛出：too many open files异常，导致无法创建新连接，服务器端挂掉
				if (connector != null) {
					connector.dispose(true);
					connector = null;
				}
				eventHandler = null;
			} catch (Exception e) {
				; // do nothing
			}
		}
	}
}
