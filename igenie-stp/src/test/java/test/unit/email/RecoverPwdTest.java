package test.unit.email;

import java.io.IOException;

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

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.SupSocketException;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.mail.SupMailService;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class RecoverPwdTest
{
	private static String userId = "10";
	private static String userName = "Tomasino";
	private static String email = "thomas.zh@qq.com";
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static String ekey = null;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization services starting...");

		String springXmlFile = "classpath:application-config.xml";
		// String springXmlFile = "classpath:mongodb-context.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		supMailroom = (SupMailService) applicationContext.getBean("supMailroom");
		accountService = (SupAccountService) applicationContext.getBean("supAccountService");

		logger.info("Initlization services success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");

		// TODO
		// authService.remove(ekey);
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
	public void test01FotgotPwd()
			throws IOException, InterruptedException, SupSocketException
	{
		logger.info("Inside FotgotPwd()");

		ekey = accountService.createEkey(GlobalArgs.ACCOUNT_LOGIN_BY_EMAIL, userId, timestamp);
		supMailroom.sendForgotPwd(email, userName, ekey);

		logger.info("Finish FotgotPwd()");
	}

	private static SupMailService supMailroom;
	private static SupAccountService accountService;
	private static final Logger logger = LoggerFactory.getLogger(RecoverPwdTest.class);

}
