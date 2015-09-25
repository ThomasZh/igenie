package com.oct.ga.stp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.restexpress.RestExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.codec.TlvPackageCodecFactory;
import com.oct.ga.jpush.JPushMessageQueue;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;

public class StpServer {
	public static void main(String[] args) throws IOException {
		logger.info("STP server is starting...");

		initSpringApp();

		logger.info("init session service manager.");
		// SessionService3MapImpl sessionService =
		// GenericSingleton.getInstance(SessionService3MapImpl.class);
		// sessionService.init();

		startMessageService();
		logger.debug(">>> message service started!");

		logger.info("init sup list monitor.");
		loadSupServerState();

		startMinaServer();
		startTlvWrapperHttpServer();

		logger.info("STP server start success!");
	}

	private static void initSpringApp() {
		String springXmlFile = "classpath:tlv-wrapper-context.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		ApplicationContextUtil.setApplicationContext(applicationContext);
		logger.info("init ApplicationContext");
	}

	private static void startMinaServer() throws IOException {
		GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext()
				.getBean("globalConfigurationVariables");

		IoAcceptor acceptor = new NioSocketAcceptor();

		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));

		acceptor.getSessionConfig().setReadBufferSize(GlobalArgs.BUFFER_SIZE);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 480);
		// get a reference to the filter chain from the acceptor
		DefaultIoFilterChainBuilder filterChainBuilder = acceptor.getFilterChain();
		filterChainBuilder.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));

		// eventHandler to process message package
		acceptor.setHandler(new BaseEventHandler());
		acceptor.bind(new InetSocketAddress(gcv.getStpPort()));
		logger.info("STP Server is listenig at port: " + gcv.getStpPort());
	}

	/**
	 * start message sending threads
	 */
	private static void startMessageService() {
		// ApnsMessageQueue apnsMq = (ApnsMessageQueue)
		// ApplicationContextUtil.getContext().getBean("apnsMessageQueue");
		// apnsMq.start();

		JPushMessageQueue jpushMq = (JPushMessageQueue) ApplicationContextUtil.getContext()
				.getBean("jpushMessageQueue");
		jpushMq.start();
	}

	private static void loadSupServerState() throws IOException {
		GlobalConfigurationVariables gcv = (GlobalConfigurationVariables) ApplicationContextUtil.getContext()
				.getBean("globalConfigurationVariables");
		SupSocketConnectionManager socketConnectionManager = GenericSingleton
				.getInstance(SupSocketConnectionManager.class);
		String pathname = gcv.getSupServerListPath();
		socketConnectionManager.loadFromFile(pathname);
		logger.info("Load sup server list from " + pathname);
	}

	private static void startTlvWrapperHttpServer() {
		RestExpress restExpress = (RestExpress) ApplicationContextUtil.getContext().getBean("tlvWrapperRestExpress");
		restExpress.uri("/legacy-api", new TlvWrapperController()).noSerialization();
		restExpress.bind();
		logger.info("Start rest http server, name: {}, port: {}", restExpress.getName(), restExpress.getPort());
	}

	private final static Logger logger = LoggerFactory.getLogger(StpServer.class);

}
