package com.oct.ga.inlinecast;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;
import com.oct.ga.comm.tlv.TlvObject;

/**
 * STP socket transfer Object
 * 
 * @author thomas
 * 
 */
public class InlinecastSocketHandler
{
	private IoConnector clientConnector;
	private static IoSession clientSession;

	public void sendTo(String stp, int port, TlvObject tlv)
			throws UnsupportedEncodingException, InterruptedException
	{
		clientConnector = new NioSocketConnector();
		clientConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		clientConnector.getSessionConfig().setReadBufferSize(GlobalArgs.BUFFER_SIZE); // 4k
		clientConnector.setHandler(new InlinecastEventHandler());

		ConnectFuture connFuture = clientConnector.connect(new InetSocketAddress(stp, port));
		connFuture.awaitUninterruptibly();
		clientSession = connFuture.getSession();
		logger.info("TCP client started.");

		// LoginAdapter loginCmd = new LoginAdapter("MacOSX",
		// PropArgs.ADMIN_CLIENT_VERSION, PropArgs.ADMIN_DEVICE_ID,
		// PropArgs.ADMIN_EMAIL, PropArgs.ADMIN_PWD, PropArgs.ADMIN_APNS_TOKEN);
		// TlvObject tLogin = BaseCommandParser.encode(loginCmd);
		// clientSession.write(tLogin);

		WriteFuture future = clientSession.write(tlv);
		// Wait until the message is completely written out to the
		// O/S buffer.
		future.awaitUninterruptibly();
		if (future.isWritten()) {
			logger.info("Send a transmit object to " + stp + ":" + port + " ok.");
		} else {
			// The messsage couldn't be written out completely for
			// some
			// reason.
			// (e.g. Connection is closed)
			logger.warn("Send a transmit object to " + stp + ":" + port + " error.");
		}

		// Thread.sleep(1000);// 1s,to sure receive completely

		clientSession.close(true);
		clientConnector.dispose(true);
	}

	private final static Logger logger = LoggerFactory.getLogger(InlinecastSocketHandler.class);
}
