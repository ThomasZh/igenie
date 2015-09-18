package test.biz;

import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.session.GaSessionInfo;
import com.oct.ga.session.GaSessionService;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class StpSessionTest
{
	private static GaSessionService sessionService;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization sessionService starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);

		sessionService = (GaSessionService) applicationContext.getBean("gaSessionService");
		sessionService.init();

		logger.info("Initlization sessionService success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
	}

	@Before
	public void setUp()
			throws Exception
	{
	}

	@After
	public void tearDown()
			throws Exception
	{
	}

	@Test
	public void test()
	{
		// String deviceId = "319BEF00-C403-4F39-80D3-9C1E22AF90BC";
		// String deviceId = "804FE81D-A779-43CB-86A8-C2CD87AF2970";
		String deviceId = "F34B71F3-BF63-4531-8153-B4E8FA374197";
		GaSessionInfo gaSession = sessionService.getSession(deviceId);

		logger.debug("DeviceId: " + deviceId);
		logger.debug("AccountId: " + gaSession.getAccountId());
		logger.debug("AccountName: " + gaSession.getAccountName());
		logger.debug("ApnsToken: " + gaSession.getApnsToken());
		logger.debug("GateSession: " + gaSession.getGateToken());
		logger.debug("IoSessionId: " + gaSession.getIoSessionId());
		logger.debug("OsVersion: " + gaSession.getOsVersion());
		logger.debug("SessionToken: " + gaSession.getSessionToken());
		logger.debug("StpId: " + gaSession.getStpId());
		logger.debug("");

		String stpId = gaSession.getStpId();
		StpServerInfoJsonBean stp = sessionService.getStp(stpId);

		logger.debug("ServerIp: " + stp.getServerIp());
		logger.debug("Port: " + stp.getPort());
		logger.debug("MinVersion: " + stp.getMinVersion());
		logger.debug("MaxVersion: " + stp.getMaxVersion());
		logger.debug("");

		String accountId = "33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e";
		Set<String> deviceSet = sessionService.getDeviceList(accountId);
		if (deviceSet == null)
			return;
		Iterator<String> it = deviceSet.iterator();
		while (it.hasNext()) {
			String id = it.next();
			logger.debug("DeviceId: " + id);
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(StpSessionTest.class);
}
