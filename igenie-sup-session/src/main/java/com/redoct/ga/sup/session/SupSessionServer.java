package com.redoct.ga.sup.session;

import java.io.IOException;
import java.net.InetSocketAddress;
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

/**
 * service unit provider for account, java socket application server implemented
 * by mina.
 * 
 * @author thomas
 */
public class SupSessionServer
{
	public static void main(String[] args)
			throws IOException
	{
		logger.info("service unit provider for session server is starting...");
		initSpringApp();

		logger.info("init session service manager.");
		SessionCacheManager sessionCacheManager = GenericSingleton.getInstance(SessionCacheManager.class);
		sessionCacheManager.init();

		startMinaServer();
		logger.info("service unit provider for session server start success!");
	}

	private static void initSpringApp()
	{
		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		ApplicationContextUtil.setApplicationContext(applicationContext);
		logger.info("init ApplicationContext");
	}

	private static void startMinaServer()
			throws IOException
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();

		IoAcceptor acceptor = new NioSocketAcceptor();

		// 杩�婊ゅ��锛����瀹�涔����璁�锛�
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		// 璁剧疆��版��灏�琚�璇诲�����缂���插�哄ぇ灏�
		acceptor.getSessionConfig().setReadBufferSize(GlobalArgs.BUFFER_SIZE); // 4k
		// 8���������娌℃��璇诲��灏辫�剧疆涓虹┖��查�����锛�骞跺��eventHandler.sessionIdle涓�绉婚�や��璇�
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 480);

		// 杩���ユ��璁剧疆
		// get a reference to the filter chain from the acceptor
		DefaultIoFilterChainBuilder filterChainBuilder = acceptor.getFilterChain();
		filterChainBuilder.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));

		// eventHandler to process message package
		acceptor.setHandler(new BaseEventHandler());
		acceptor.bind(new InetSocketAddress(gcv.getSupPort()));
		logger.info("service unit provider for session Server is listenig at port: " + gcv.getSupPort());
	}

	private final static Logger logger = LoggerFactory.getLogger(SupSessionServer.class);

}
