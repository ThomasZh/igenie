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

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.group.dao.GaGroupDao;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class GaGroupDaoTest
{
	private static String groupId = UUID.randomUUID().toString();
	private static String groupName = "test101";
	private static short channelType = GlobalArgs.CHANNEL_TYPE_ACTIVITY;
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static String creatorId = "10";
	private static short depth = 0;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization dao starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		dao = (GaGroupDao) applicationContext.getBean("gaGroupDao");

		logger.info("Initlization dao success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");
		
		dao.remove(groupId);
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
		logger.info("Inside add()");

		dao.add(groupId, groupName, channelType, timestamp, creatorId, depth);

		logger.info("Finish add()");
	}

	@Test
	public void test02QueryName()
	{
		logger.info("Inside QueryName()");

		String channelName = dao.queryGroupName(groupId);
		assertEquals(groupName, channelName);

		logger.info("Finish QueryName()");
	}

	@Test
	public void test03ChangeName()
	{
		logger.info("Inside ChangeName()");

		groupName = "test102";
		dao.update(groupId, groupName, timestamp);
		String channelName = dao.queryGroupName(groupId);
		assertEquals(groupName, channelName);

		logger.info("Finish ChangeName()");
	}

	@Test
	public void test04QueryType()
	{
		logger.info("Inside QueryType()");

		short groupType = dao.queryChannelType(groupId);
		assertEquals(channelType, groupType);

		logger.info("Finish QueryType()");
	}

	@Test
	public void test05IsActive()
	{
		logger.info("Inside IsActive()");

		boolean isActive = dao.isActive(groupId);
		assertEquals(true, isActive);

		dao.updateSate(groupId, GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED, timestamp);
		isActive = dao.isActive(groupId);
		assertEquals(false, isActive);

		logger.info("Finish IsActive()");
	}

	@Test
	public void test06QuerySummary()
	{
		logger.info("Inside QuerySummary()");

		dao.updateMemberNum(groupId, 5, timestamp);
		int num = dao.queryMemberNum(groupId);
		assertEquals(5, num);

		dao.updateChildNum(groupId, 6, timestamp);
		num = dao.queryChildNum(groupId);
		assertEquals(6, num);

		dao.updateAttachmentNum(groupId, 7, timestamp);
		num = dao.queryAttachmentNum(groupId);
		assertEquals(7, num);

		dao.updateNoteNum(groupId, 8, timestamp);
		num = dao.queryNoteNum(groupId);
		assertEquals(8, num);

		logger.info("Finish QuerySummary()");
	}

	private static GaGroupDao dao;
	private static final Logger logger = LoggerFactory.getLogger(GaGroupDaoTest.class);

}
