package test.unit.sevice;

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
import com.oct.ga.comm.domain.template.TemplateDefineJsonBean;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.service.GaTemplateService;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class GaTemplateServiceTest
{
	private static String taskId = UUID.randomUUID().toString();
	private static String pid = UUID.randomUUID().toString();
	private static String userId = UUID.randomUUID().toString();
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static ApplicationContext applicationContext;
	private static final String EMPTY_TEMPLATE_ID = "00000000-0000-0000-0000-000000000000";
	private static TaskProExtInfo taskinfo;
	private static String templateId = null;
	private static String taskId2 = UUID.randomUUID().toString();

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization services starting...");

		String springXmlFile = "classpath:application-config.xml";
		applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		templateService = (GaTemplateService) applicationContext.getBean("gaTemplateService");
		taskService = (GaTaskService) applicationContext.getBean("gaTaskService");
		groupService = (GaGroupService) applicationContext.getBean("gaGroupService");

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
	public void test01AddTask()
	{
		logger.info("Inside testAddTask()");

		taskinfo = new TaskProExtInfo();
		taskinfo.setId(taskId);
		taskinfo.setName("my project demo");
		taskinfo.setDesc("project create by user");
		taskinfo.setChannelType(GlobalArgs.CHANNEL_TYPE_TASK);
		taskinfo.setColor((short) 1);
		taskinfo.setCreateAccountId(userId);
		taskinfo.setPid(pid);
		taskinfo.setTemplateId(EMPTY_TEMPLATE_ID);
		taskinfo.setStartTime(timestamp);
		taskinfo.setEndTime(timestamp);
		taskinfo.setState(GlobalArgs.CLUB_ACTIVITY_STATE_OPENING);
		taskinfo.setLocDesc("locDesc");
		taskinfo.setLocX("locX");
		taskinfo.setLocY("locY");
		taskService.add(taskinfo, timestamp);

		logger.info("Finish testAddTask()");
	}

	@Test
	public void test02MakeProject2Template()
	{
		logger.info("Inside testMakeProject2Template()");

		templateService.makeProject2Template(applicationContext, taskinfo, userId, EMPTY_TEMPLATE_ID, "template name");

		logger.info("Finish testMakeProject2Template()");
	}

	@Test
	public void test03MakTemplate2Project()
	{
		logger.info("Inside testMakTemplate2Project()");

		templateId = "9e6bc02c-48af-4c1e-844e-11a096b9ca0c";
		TemplateDefineJsonBean projectTemplate = templateService.queryMaxVersion(templateId);
		templateService.makeTemplate2Project(applicationContext, projectTemplate, userId, timestamp);
		groupService.createGroup(taskId2, "project from template", GlobalArgs.CHANNEL_TYPE_TASK, timestamp, userId);

		logger.info("Finish testMakTemplate2Project()");
	}

	private static GaTemplateService templateService;
	private static GaTaskService taskService;
	private static GaGroupService groupService;

	private static final Logger logger = LoggerFactory.getLogger(GaTemplateServiceTest.class);
}
