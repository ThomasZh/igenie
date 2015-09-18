package com.oct.ga.inlinecast;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;
import com.oct.ga.comm.tlv.TlvObject;

public class InlinecastSocketManager
{
	private Map<String, IoSession> connectMap = new HashMap<String, IoSession>();

	public void sendto(String stpIp, int port, TlvObject tlv)
	{
		IoSession clientSession = this.getSocketHandler(stpIp, port);
		if (clientSession == null)
			return;// connect stp:ip error by socket/tcp.

		if (clientSession.isConnected()) {
			WriteFuture future = clientSession.write(tlv);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (future.isWritten()) {
				logger.info("Send a transmit object to " + stpIp + ":" + port + " ok.");
			} else {
				// The messsage couldn't be written out completely for
				// some reason. (e.g. Connection is closed)
				logger.warn("Send a transmit object to " + stpIp + ":" + port + " error.");

				clientSession.close(true);
			}
		} else { // reconnect stp:port
			IoConnector clientConnector = new NioSocketConnector();
			clientConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
			clientConnector.getSessionConfig().setReadBufferSize(GlobalArgs.BUFFER_SIZE); // 4k
			clientConnector.setHandler(new InlinecastEventHandler());

			ConnectFuture connFuture = clientConnector.connect(new InetSocketAddress(stpIp, port));
			connFuture.awaitUninterruptibly();
			clientSession = connFuture.getSession();
			logger.info("TCP client reconnect to " + stpIp + ":" + port);

			WriteFuture future = clientSession.write(tlv);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (future.isWritten()) {
				logger.info("Send a transmit object to " + stpIp + ":" + port + " ok.");
			} else {
				// The messsage couldn't be written out completely for
				// some
				// reason.
				// (e.g. Connection is closed)
				logger.warn("Send a transmit object to " + stpIp + ":" + port + " error.");

				clientSession.close(true);
			}
		}
	}

	private IoSession getSocketHandler(String stpIp, int port)
	{
		String key = stpIp + ":" + port;

		IoSession clientSession = connectMap.get(key);
		if (clientSession == null) {
			try {
				IoConnector clientConnector = new NioSocketConnector();
				clientConnector.getFilterChain()
						.addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
				clientConnector.getSessionConfig().setReadBufferSize(GlobalArgs.BUFFER_SIZE); // 4k
				clientConnector.setHandler(new InlinecastEventHandler());

				ConnectFuture connFuture = clientConnector.connect(new InetSocketAddress(stpIp, port));
				connFuture.awaitUninterruptibly();
				clientSession = connFuture.getSession();
				logger.info("TCP client connect to " + stpIp + ":" + port);

				connectMap.put(key, clientSession);
			} catch (Exception e) {
				logger.error("TCP client connect to " + stpIp + ":" + port + " ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE
						+ "]|" + LogErrorMessage.getFullInfo(e));

				return null;
			}
		}
		return clientSession;
	}

	private final static Logger logger = LoggerFactory.getLogger(InlinecastSocketManager.class);
}
