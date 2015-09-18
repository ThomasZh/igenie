/**
 * 
 */
package com.oct.ga.gatekeeper.client;

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
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.cmd.gatekeeper.ModifyStpStateReq;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;

/**
 * service transfer protocol client:
 * 
 * design to talk to stp server/service, currently for web app use
 * 
 * @author liwenzhi
 * 
 */
public class ModifyStpStateClient
{
	public static void main(String[] args)
			throws IOException, InterruptedException
	{
		String hostname = "localhost";
		int port = 13101;
		String stpId = null;
		short state = ErrorCode.SUCCESS;

		if (args.length == 0 || args.length == 1 || args.length > 4) {
			System.out.println("Usage: reportState.sh stpId state");
			System.out.println("       gatekeeperIp default is localhost(127.0.0.1)");
			System.out.println("       gatekeeperPort defalut is 13101");
			System.out.println("       state: 100 is active, 200 is inactive");
			System.out.println("or");
			System.out.println("Usage: reportState.sh gatekeeperIp stpId state");
			System.out.println("       gatekeeperPort defalut is 13101");
			System.out.println("       state: 100 is active, 200 is inactive");
			System.out.println("or");
			System.out.println("Usage: reportState.sh gatekeeperIp gatekeeperPort stpId state");
			System.out.println("       state: 100 is active, 200 is inactive");

			System.exit(0);
		} else if (args.length == 2) {
			stpId = args[0];
			state = Short.parseShort(args[1]);
		} else if (args.length == 3) {
			hostname = args[0];
			stpId = args[1];
			state = Short.parseShort(args[2]);
		} else if (args.length == 4) {
			hostname = args[0];
			port = Integer.parseInt(args[1]);
			stpId = args[2];
			state = Short.parseShort(args[3]);
		}
		ModifyStpStateClient client = new ModifyStpStateClient(hostname, port);

		int sequence = DatetimeUtil.currentTimestamp();
		ModifyStpStateReq reqCmd = new ModifyStpStateReq(sequence, stpId, state);

		client.send(reqCmd);

		Thread.sleep(1000);// 1s

		client.connector.dispose(true);
	}

	/**
	 * init stp client base values
	 */
	public ModifyStpStateClient(String hostname, int port)
	{
		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		connector.getSessionConfig().setReadBufferSize(4096); // 4k

		messageHandler = new StpStateHandler();
		connector.setHandler(messageHandler);

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

		return messageHandler.getResponse();
	}

	private IoConnector connector;
	private IoSession session;
	private StpStateHandler messageHandler;

	private final static Logger logger = LoggerFactory.getLogger(ModifyStpStateClient.class);
}
