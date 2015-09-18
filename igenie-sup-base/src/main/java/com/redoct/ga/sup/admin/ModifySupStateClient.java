/**
 * 
 */
package com.redoct.ga.sup.admin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.cmd.admin.ModifySupServerStateReq;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;

/**
 * service transfer protocol client:
 * 
 * design to talk to stp server/service, currently for web app use
 * 
 * @author thomas
 * 
 */
public class ModifySupStateClient
{
	public static void main(String[] args)
			throws IOException, InterruptedException
	{
		String hostname = "localhost";
		int port = 13105;
		String supId = null;
		short state = GlobalArgs.TRUE;

		if (args.length == 0 || args.length == 1 || args.length > 4) {
			System.out.println("Usage: modifySupState.sh supId state");
			System.out.println("       stpIp default is localhost(127.0.0.1)");
			System.out.println("       stpPort defalut is 13105");
			System.out.println("       state: 1 is active, 0 is inactive");
			System.out.println("or");
			System.out.println("Usage: modifySupState.sh stpIp supId state");
			System.out.println("       stpPort defalut is 13105");
			System.out.println("       state: 1 is active, 0 is inactive");
			System.out.println("or");
			System.out.println("Usage: modifySupState.sh stpIp stpPort supId state");
			System.out.println("       state: 1 is active, 0 is inactive");

			System.exit(0);
		} else if (args.length == 2) {
			supId = args[0];
			state = Short.parseShort(args[1]);
		} else if (args.length == 3) {
			hostname = args[0];
			supId = args[1];
			state = Short.parseShort(args[2]);
		} else if (args.length == 4) {
			hostname = args[0];
			port = Integer.parseInt(args[1]);
			supId = args[2];
			state = Short.parseShort(args[3]);
		}
		ModifySupStateClient client = new ModifySupStateClient(hostname, port);

		int sequence = DatetimeUtil.currentTimestamp();
		ModifySupServerStateReq reqCmd = new ModifySupServerStateReq(sequence, supId, state);

		client.send(reqCmd);

		Thread.sleep(1000);// 1s

		client.session.close(true);
		client.connector.dispose(true);

		System.exit(0);
	}

	/**
	 * init stp client base values
	 */
	public ModifySupStateClient(String hostname, int port)
	{
		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		connector.getSessionConfig().setReadBufferSize(16384); // 16k

		eventHandler = new SupStateHandler();
		connector.setHandler(eventHandler);

		start(hostname, port);
	}

	/**
	 * after registerCommand, call this
	 */
	public void start(String ip, int port)
	{
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(ip, port));
		connFuture.awaitUninterruptibly();// setup connection...

		session = connFuture.getSession();

		logger.debug(">>> stp client started...");
	}

	/**
	 * send request to server and wait for response
	 * 
	 * @param request
	 * @return response
	 * @throws InterruptedException
	 * @throws UnsupportedEncodingException
	 */
	public RespCommand send(StpCommand request)
			throws InterruptedException, UnsupportedEncodingException
	{
		WriteFuture writeFuture = session.write(request.encode());
		writeFuture.awaitUninterruptibly();

		if (writeFuture.getException() != null) {
			session.getConfig().setUseReadOperation(false);
			return null;
		}

		session.getConfig().setUseReadOperation(true);

		final ReadFuture readFuture = session.read();
		readFuture.awaitUninterruptibly();// read response message

		if (readFuture.getException() != null) {
			session.getConfig().setUseReadOperation(false);
			return null;
		}
		// stop blocking inbound messages
		session.getConfig().setUseReadOperation(false);

		return eventHandler.getResponse();
	}

	private IoConnector connector;
	private IoSession session;
	private SupStateHandler eventHandler;

	private final static Logger logger = LoggerFactory.getLogger(ModifySupStateClient.class);
}
