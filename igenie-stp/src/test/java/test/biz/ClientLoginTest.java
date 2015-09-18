package test.biz;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.cmd.auth.LoginReq;
import com.oct.ga.comm.cmd.gatekeeper.GK_ARQ;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.stp.parser.StpCommandParser;

public class ClientLoginTest
		extends IoHandlerAdapter
{
	private IoConnector connector;
	private static IoSession session;

	public ClientLoginTest()
	{
	}

	public void connect2Gatekeeper(String hostname, int port)
	{
		connector = new NioSocketConnector();

		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		connector.getSessionConfig().setReadBufferSize(65535); // 64k

		connector.setHandler(new GatekeeperHandler());

		ConnectFuture connFuture = connector.connect(new InetSocketAddress(hostname, port));
		connFuture.awaitUninterruptibly();
		session = connFuture.getSession();
		System.out.println("connected to gatekeeper(" + hostname + ":" + port + ")");
	}

	public void connect2Stp(String hostname, int port)
	{
		connector = new NioSocketConnector();

		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		connector.getSessionConfig().setReadBufferSize(65535); // 64k

		connector.setHandler(new StpEventHandler());

		ConnectFuture connFuture = connector.connect(new InetSocketAddress(hostname, port));
		connFuture.awaitUninterruptibly();
		session = connFuture.getSession();
		System.out.println("connected to stp(" + hostname + ":" + port + ")");
	}

	public static void main(String[] args)
			throws IOException, InterruptedException
	{
		String hostname = "182.92.71.66";
		int port = 13101;
		if (args.length == 1)
			hostname = args[0];
		else if (args.length == 2) {
			hostname = args[0];
			port = Integer.parseInt(args[1]);
		}
		ClientLoginTest client = new ClientLoginTest();
		client.connect2Gatekeeper(hostname, port);

		String deviceId = "bb55ae96-abba-45f3-b475-d4d963cb370d";
		String appId = "8ed18148-5d33-43f1-90f1-98b8bcf323d7"; // iGenie
		String vendorId = "46098b55-27c7-45f3-afed-4794fb0ccd4d"; // 4k
		String version = "Romania_v0.1.0";
		GK_ARQ arq = new GK_ARQ();
		arq.setDeviceId(deviceId);
		arq.setAppId(appId);
		arq.setVendorId(vendorId);
		arq.setVersion(version);

		TlvObject tArq = StpCommandParser.encode(arq);
		session.write(tArq);

		Thread.sleep(1000);// 1s

		client.connector.dispose(true);

		GatekeeperArgs gkArgs = GenericSingleton.getInstance(GatekeeperArgs.class);
		client.connect2Stp(gkArgs.getIp(), gkArgs.getPort());

		String email = "tomasino.zhang@qq.com";
		String ecryptPwd = EcryptUtil.md5("t");
		String apnsToken = null;
		LoginReq loginCmd = new LoginReq("MacOS_v10.10", gkArgs.getToken(), deviceId, email, ecryptPwd, apnsToken);
		TlvObject tLogin = StpCommandParser.encode(loginCmd);
		session.write(tLogin);

		Thread.sleep(1000);// 1s

		client.connector.dispose(true);
	}

	private final static Logger logger = LoggerFactory.getLogger(ClientLoginTest.class);
}
