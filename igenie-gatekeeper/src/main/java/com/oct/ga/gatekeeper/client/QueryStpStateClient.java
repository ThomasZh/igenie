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
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.cmd.gatekeeper.QueryStpStatesReq;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;

public class QueryStpStateClient
{
	public static void main(String[] args)
			throws IOException, InterruptedException
	{
		String hostname = "127.0.0.1";
		int port = 13101;

		if (args.length > 2) {
			System.out.println("Usage: queryState.sh");
			System.out.println("       gatekeeperIp default is localhost(127.0.0.1)");
			System.out.println("       gatekeeperPort defalut is 13101");
			System.out.println("or");
			System.out.println("Usage: queryState.sh hostname");
			System.out.println("       gatekeeperPort defalut is 13101");
			System.out.println("or");
			System.out.println("Usage: queryState.sh hostname port");

			System.exit(0);
		} else if (args.length == 1) {
			hostname = args[0];
		} else if (args.length == 2) {
			hostname = args[0];
			port = Integer.parseInt(args[1]);
		}
		QueryStpStateClient client = new QueryStpStateClient(hostname, port);

		int sequence = DatetimeUtil.currentTimestamp();
		QueryStpStatesReq reqCmd = new QueryStpStatesReq(sequence);

		client.send(reqCmd);

		Thread.sleep(3000);// 3s

		client.connector.dispose(true);
	}

	/**
	 * init stp client base values
	 */
	public QueryStpStateClient(String hostname, int port)
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

	private final static Logger logger = LoggerFactory.getLogger(QueryStpStateClient.class);
}
