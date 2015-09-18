package com.oct.ga.gatekeeper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;
import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.session.SessionService3MapImpl;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;

// I am DNS.
// 
// GK_ARQ_Adapter: deviceId,appId,vendorId
// GK_ACF: sessionToken,STPServer:port
// Round robin: step by next
// No heart bit in 1 minute, STPServer was down.
// Dynamic load STPServer, 1 minute load stpserver.list.properties
// 
// load balance algorithm
// 1. Random
// 2. Weighted Random
// 3. Round robin
// 4. Weighted Round Robin
// 5. Single2StateResp Time
// 6. Least Connection
// 7. CPU able
// 8. Flash DNS
public class GateKeeper
{
	public static void main(String[] args)
			throws IOException
	{
		logger.info("GateKeeper is starting...");

		logger.info("init ApplicationContext.");
		initSpringApp();

		logger.info("init session service manager.");
		SessionService3MapImpl sessionService = GenericSingleton.getInstance(SessionService3MapImpl.class);
		sessionService.init();

		logger.info("init stp list monitor.");
		// new Thread(new ServerListMonitor()).start();
		loadStpServersState();

		logger.info("init sup list monitor.");
		loadSupServerState();

		startMinaServer();

		logger.info("GateKeeper start success!");
	}

	private static void initSpringApp()
	{
		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		ApplicationContextUtil.setApplicationContext(applicationContext);
		logger.info("inited ApplicationContext");
	}

	private static void startMinaServer()
			throws IOException
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();

		IoAcceptor acceptor = new NioSocketAcceptor();

		// ���������Զ���Э�飩
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		// ������ݽ�����ȡ�Ļ������С
		acceptor.getSessionConfig().setReadBufferSize(GlobalArgs.BUFFER_SIZE); // 4k
		// 10����û�ж�д������Ϊ����ͨ��
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

		// ���ӳ�����
		// get a reference to the filter chain from the acceptor
		DefaultIoFilterChainBuilder filterChainBuilder = acceptor.getFilterChain();
		filterChainBuilder.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));

		acceptor.setHandler(new GateKeeperEventHandler());
		acceptor.bind(new InetSocketAddress(gcv.getGatekeeperPort()));
		logger.info("GateKeeper is listenig at port: " + gcv.getGatekeeperPort());
	}

	private static void loadStpServersState()
			throws IOException
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();

		ServerListCache serverListCache = GenericSingleton.getInstance(ServerListCache.class);
		String pathname = gcv.getStpServerListPath();
		List<StpServerInfoJsonBean> newStpArray = serverListCache.loadFromFile(pathname);
		logger.info("Load server list from " + pathname);

		SessionService3MapImpl sessionService = GenericSingleton.getInstance(SessionService3MapImpl.class);
		for (StpServerInfoJsonBean stp : newStpArray) {
			sessionService.putStp(stp.getStpId(), stp);
		}
	}

	private static void loadSupServerState()
			throws IOException
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();

		SupSocketConnectionManager socketConnectionManager = GenericSingleton
				.getInstance(SupSocketConnectionManager.class);
		String pathname = gcv.getSupServerListPath();
		socketConnectionManager.loadFromFile(pathname);
		logger.info("Load sup server list from " + pathname);
	}

	private final static Logger logger = LoggerFactory.getLogger(GateKeeper.class);

}
