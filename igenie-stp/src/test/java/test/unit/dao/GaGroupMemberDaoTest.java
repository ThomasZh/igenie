package test.unit.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
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
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.group.GroupMemberMasterInfo;
import com.oct.ga.group.dao.GaGroupMemberDao;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class GaGroupMemberDaoTest
{
	private static String groupId = UUID.randomUUID().toString();
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static String userId = "10";

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization dao starting...");

		String springXmlFile = "classpath:application-config.xml";
		// String springXmlFile = "classpath:mongodb-context.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		dao = (GaGroupMemberDao) applicationContext.getBean("gaGroupMemberDao");

		logger.info("Initlization dao success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");

		dao.remove(groupId, userId);
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

		dao.add(groupId, userId, GlobalArgs.MEMBER_RANK_NORMAL, GlobalArgs.INVITE_STATE_ACCPET, timestamp);

		logger.info("Finish add()");
	}

	@Test
	public void test02QueryTaskMemberInfo()
	{
		logger.info("Inside QueryTaskMemberInfo()");

		GroupMemberDetailInfo member = dao.queryMember(groupId, userId);
		assertEquals(GlobalArgs.INVITE_STATE_ACCPET, member.getState());
		assertEquals(GlobalArgs.MEMBER_RANK_NORMAL, member.getRank());

		logger.info("Finish QueryTaskMemberInfo()");
	}

	@Test
	public void test03UpdateRank()
	{
		logger.info("Inside UpdateRank()");

		dao.updateRank(groupId, userId, GlobalArgs.MEMBER_RANK_LEADER, timestamp);
		String leaderId = dao.queryLeaderId(groupId);
		assertEquals(userId, leaderId);

		logger.info("Finish UpdateRank()");
	}

	@Test
	public void test04UpdateState()
	{
		logger.info("Inside UpdateState()");

		dao.updateState(groupId, userId, GlobalArgs.INVITE_STATE_KICKOFF, timestamp);

		logger.info("Finish UpdateState()");
	}

	@Test
	public void test05QueryLastChangeTaskMember()
	{
		logger.info("Inside queryLastChangeTaskMember()");

		List<GroupMemberMasterInfo> members = dao.queryLastChangedMembersMasterInfo(groupId, 0);
		assertTrue(members.size() > 0);
		for (GroupMemberMasterInfo member : members) {
			assertEquals(GlobalArgs.INVITE_STATE_KICKOFF, member.getState());
		}

		logger.info("Finish queryLastChangeTaskMember()");
	}

	@Test
	public void test06QueryTaskMember()
	{
		logger.info("Inside QueryTaskMember()");

		List<GroupMemberDetailInfo> members = dao.queryMembers(groupId);
		assertTrue(members.size() == 0);

		logger.info("Finish QueryTaskMember()");
	}

	private static GaGroupMemberDao dao;
	private static final Logger logger = LoggerFactory.getLogger(GaGroupMemberDaoTest.class);
}
