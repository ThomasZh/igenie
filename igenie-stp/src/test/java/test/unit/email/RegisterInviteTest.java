package test.unit.email;

import java.util.UUID;

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

import com.oct.ga.comm.SupSocketException;
import com.redoct.ga.sup.mail.SupMailService;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class RegisterInviteTest
{
	private static String fromName = "Thomas";
	private static String toName = "Tomasino";
	private static String fromEmail = "thomas.zh@qq.com";
	private static String toEmail = "thomas.zh@icloud.com";

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization services starting...");

		String springXmlFile = "classpath:application-config.xml";
		// String springXmlFile = "classpath:mongodb-context.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		supMailroom = (SupMailService) applicationContext.getBean("supMailroom");

		logger.info("Initlization services success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");
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
	public void test01RegisterInvite() throws SupSocketException
	{
		logger.info("Inside RegisterInvite()");

		String inviteId = UUID.randomUUID().toString();
		supMailroom.sendFriendInvite(fromEmail, fromName, toEmail, toName, inviteId);

		logger.info("Finish RegisterInvite()");
	}

	private static SupMailService supMailroom;
	private static final Logger logger = LoggerFactory.getLogger(RegisterInviteTest.class);

}
