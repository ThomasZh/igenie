package test.unit.dao;

import static org.junit.Assert.assertEquals;

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

import com.oct.ga.badgenum.dao.GaBadgeNumDao;
import com.oct.ga.comm.domain.AccountBadgeNumJsonBean;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class GaBadegNumDaoTest
{
	private static String accountId = UUID.randomUUID().toString();

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization gaBadgeNumDao starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		badgeNumDao = (GaBadgeNumDao) applicationContext.getBean("gaBadgeNumDao");

		logger.info("Initlization gaBadgeNumDao success!");
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
	public void test01Add()
	{
		logger.info("Inside AddOrginal()");

		badgeNumDao.add(accountId);

		logger.info("Finish Add()");
	}

	@Test
	public void test02Exist()
	{
		logger.info("Inside exist()");

		boolean exist = badgeNumDao.isExist(accountId);
		logger.debug("accountId exist: " + exist);
		assertEquals(true, exist);

		logger.info("Finish exist()");
	}

	@Test
	public void test03Update()
	{
		logger.info("Inside Update()");

		badgeNumDao.updateMessageNum(accountId, (short) 1);
		badgeNumDao.updateTaskLogNum(accountId, (short) 2);
		badgeNumDao.updateInviteNum(accountId, (short) 3);

		logger.info("Finish Update()");
	}

	@Test
	public void test04Query()
	{
		logger.info("Inside query()");

		AccountBadgeNumJsonBean badgeNum = badgeNumDao.select(accountId);
		logger.debug("getMessageNum: " + badgeNum.getMessageNum());
		logger.debug("getTaskLogNum: " + badgeNum.getTaskLogNum());
		logger.debug("getInviteNum: " + badgeNum.getInviteNum());

		logger.info("Finish query()");
	}

	private static GaBadgeNumDao badgeNumDao;

	private static final Logger logger = LoggerFactory.getLogger(GaBadegNumDaoTest.class);
}
