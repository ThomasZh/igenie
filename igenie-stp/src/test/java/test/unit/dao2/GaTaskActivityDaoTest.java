package test.unit.dao2;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.task.dao.GaTaskActivityDao;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class GaTaskActivityDaoTest
{

	private static GaTaskActivityDao dao;
	private static NotifyTaskLog activity;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		System.out.println("Initlization dao");

		String springXmlFile = "classpath:application-config.xml";
		// String springXmlFile = "classpath:mongodb-context.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		dao = (GaTaskActivityDao) applicationContext.getBean("gaTaskActivityDao");

		System.out.println("Initlization message");

		int timestamp = (int) (System.currentTimeMillis() / 1000);
		activity = new NotifyTaskLog();
		String activityId = UUID.randomUUID().toString();
		activity.set_id(activityId);
		activity.setFromAccountId(UUID.randomUUID().toString());
		activity.setFromAccountName("fromAccountName");
		activity.setToAccountId(UUID.randomUUID().toString());
		activity.setToAccountName("toAccountName");
		activity.setChannelId(UUID.randomUUID().toString());// taskId
		activity.setChannelName("taskName");
		activity.setCommandTag(Command.INVITE_REQ);
		activity.setTimestamp(timestamp);
		activity.setActivityState(GlobalArgs.INVITE_STATE_ACCPET);
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
		System.out.println("Inside testAdd()");
		dao.add(activity);
		System.out.println("Finish testAdd()");
	}

	@Test
	public void test02QueryLastChangeTaskActivity()
			throws SQLException
	{
		System.out.println("Inside queryLastChangeTaskActivity()");

		NotifyTaskLog activity2 = dao.queryLastOneByProject(activity.getChannelId(), "accountId", 0);

		System.out.println("activityId: " + activity2.get_id());
		assertEquals(activity.get_id(), activity2.get_id());
		assertEquals(activity.getFromAccountId(), activity2.getFromAccountId());
		assertEquals(activity.getFromAccountName(), activity2.getFromAccountName());
		assertEquals(activity.getToAccountId(), activity2.getToAccountId());
		assertEquals(activity.getToAccountName(), activity2.getToAccountName());
		assertEquals(activity.getChannelId(), activity2.getChannelId());
		assertEquals(activity.getChannelName(), activity2.getChannelName());
		System.out.println("timestamp: " + activity2.getTimestamp());
		assertEquals(activity.getTimestamp(), activity2.getTimestamp());
		System.out.println("state: " + activity2.getActivityState());
		assertEquals(activity2.getActivityState(), GlobalArgs.INVITE_STATE_ACCPET);

		System.out.println("Finish queryLastChangeTaskActivity()");
	}

}
