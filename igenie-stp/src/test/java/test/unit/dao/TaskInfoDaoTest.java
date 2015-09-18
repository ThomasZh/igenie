package test.unit.dao;

import static org.junit.Assert.assertTrue;

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
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.task.dao.GaTaskInfoDao;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class TaskInfoDaoTest
{
	private static String taskId = UUID.randomUUID().toString();
	private static String pid = UUID.randomUUID().toString();
	private static String userId = UUID.randomUUID().toString();
	private static int timestamp = DatetimeUtil.currentTimestamp();

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization DAO starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		dao = (GaTaskInfoDao) applicationContext.getBean("gaTaskInfoDao");

		logger.info("Initlization DAO success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");
		dao.remove(taskId);
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
		logger.info("Inside testAdd()");

		TaskProExtInfo taskinfo = new TaskProExtInfo();
		taskinfo.setId(taskId);
		taskinfo.setName("project name");
		taskinfo.setDesc("project desc");
		taskinfo.setChannelType(GlobalArgs.CHANNEL_TYPE_TASK);
		taskinfo.setColor((short) 1);
		taskinfo.setCreateAccountId(userId);
		taskinfo.setPid(pid);
		taskinfo.setTemplateId("00000000-0000-0000-0000-000000000000");
		taskinfo.setStartTime(timestamp);
		taskinfo.setEndTime(timestamp);
		taskinfo.setState(GlobalArgs.CLUB_ACTIVITY_STATE_OPENING);
		taskinfo.setLocDesc("locDesc");
		taskinfo.setLocX("locX");
		taskinfo.setLocY("locY");
		dao.add(taskinfo, timestamp);

		logger.info("Finish testAdd()");
	}

	@Test
	public void test02IsExist()
	{
		logger.info("Inside testIsExist()");

		boolean isExist = dao.isExist(taskId);
		assertTrue(isExist);

		logger.info("Finish testIsExist()");
	}

	@Test
	public void test03QuieryById()
	{
		logger.info("Inside testQuieryById()");

		TaskProExtInfo taskinfo = dao.query(taskId);
		logger.debug("taskName: " + taskinfo.getName());

		logger.info("Finish testQuieryById()");
	}

	@Test
	public void test04Update()
	{
		logger.info("Inside testUpdate()");

		dao.updateCompletedTime(taskId, timestamp);

		logger.info("Finish testUpdate()");
	}

	@Test
	public void test05QueryLastUpdateTask()
	{
		logger.info("Inside queryLastUpdateTask()");

		TaskProExtInfo taskinfo = dao.queryLastUpdate(taskId, 0);
		logger.debug("taskId: " + taskinfo.getId());
		logger.debug("taskState: " + taskinfo.getState());

		logger.info("Finish queryLastUpdateTask()");
	}

	private static GaTaskInfoDao dao;
	private static final Logger logger = LoggerFactory.getLogger(TaskInfoDaoTest.class);

}
