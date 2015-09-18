/**
 * 
 */
package test.unit.dao2;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;
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
import com.oct.ga.comm.domain.msg.MessageExtendUnicast;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.message.dao.GaMessageDao;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class GaMessageDaoTest
{

	private static GaMessageDao dao;
	private static MessageExtendUnicast msg;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		System.out.println("Initlization dao");

		String springXmlFile = "classpath:application-config.xml";
		// String springXmlFile = "classpath:mongodb-context.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		dao = (GaMessageDao) applicationContext.getBean("gaMessageDao");

		System.out.println("Initlization message");

		int timestamp = (int) (System.currentTimeMillis() / 1000);
		msg = new MessageExtendUnicast();
		String messageId = UUID.randomUUID().toString();
		msg.set_id(messageId);
		msg.setContentType(GlobalArgs.CONTENT_TYPE_TXT);
		msg.setFromAccountId(UUID.randomUUID().toString());
		msg.setFromAccountName("accountName");
		msg.setChannelType(GlobalArgs.CHANNEL_TYPE_TASK);
		msg.setChannelId(UUID.randomUUID().toString());
		msg.setContent("MessageTxt");
		msg.setTimestamp(timestamp);
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		System.out.println("RemoveExtend");
		dao.removeExtend(msg.get_id());

		System.out.println("RemoveOriginal");
		dao.removeOriginal(msg.get_id());
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
	public void test01AddOriginal()
			throws SQLException
	{
		System.out.println("Inside testAddOriginal()");
		dao.addOriginal(msg);
		System.out.println("Finish testAddOriginal()");
	}

	@Test
	public void test02AddExtend()
			throws SQLException
	{
		System.out.println("Inside testAddExtend()");
		dao.addExtend(msg.get_id(), msg.getToAccountId(), 0, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
		// dao.addExtend(msg); //
		System.out.println("Finish testAddExtend()");
	}

	@Test
	public void test03QueryNotRead()
			throws SQLException
	{
		System.out.println("Inside testQueryNotRead()");

		List<MessageInlinecast> msgs = dao.queryByState(msg.getChannelId(), 0, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
		for (MessageInlinecast msg2 : msgs) {
			System.out.println("messageId: " + msg2.get_id());
			assertEquals(msg.get_id(), msg2.get_id());
			assertEquals(msg.getContentType(), msg2.getContentType());
			assertEquals(msg.getFromAccountId(), msg2.getFromAccountId());
			assertEquals(msg.getFromAccountName(), msg2.getFromAccountName());
			assertEquals(msg.getChannelType(), msg2.getChannelType());
			assertEquals(msg.getChannelId(), msg2.getChannelId());
			assertEquals(msg.getContent(), msg2.getContent());
			System.out.println("timestamp: " + msg2.getTimestamp());
			assertEquals(msg.getTimestamp(), msg2.getTimestamp());
			System.out.println("state: " + msg2.getSyncState());
			assertEquals(msg.getSyncState(), GlobalArgs.SYNC_STATE_NOT_RECEIVED);
		}
		assertEquals(1, msgs.size());

		System.out.println("Finish testQueryNotRead()");
	}

	@Test
	public void test04UpdateExtend()
			throws SQLException
	{
		System.out.println("Inside testUpdateExtend()");

		int newTimestamp = (int) (System.currentTimeMillis() / 1000);
		System.out.println("Inside testUpdateExtend()");
		dao.updateExtendState(msg.get_id(), msg.getToAccountId(), newTimestamp, GlobalArgs.SYNC_STATE_RECEIVED);

		System.out.println("Finish testUpdateExtend()");
	}

	@Test
	public void test05QueryAll()
			throws SQLException
	{
		System.out.println("Inside testQueryAll()");

		List<MessageInlinecast> msgs2 = dao.queryByState(msg.getChannelId(), 0, GlobalArgs.SYNC_STATE_NOT_RECEIVED);
		for (MessageInlinecast msg3 : msgs2) {
			System.out.println("messageId: " + msg3.get_id());
			assertEquals(msg.get_id(), msg3.get_id());
			assertEquals(msg.getContentType(), msg3.getContentType());
			assertEquals(msg.getFromAccountId(), msg3.getFromAccountId());
			assertEquals(msg.getFromAccountName(), msg3.getFromAccountName());
			assertEquals(msg.getChannelType(), msg3.getChannelType());
			assertEquals(msg.getChannelId(), msg3.getChannelId());
			assertEquals(msg.getContent(), msg3.getContent());
			System.out.println("timestamp: " + msg3.getTimestamp());
			// assertEquals(msg.getTimestamp(), msg3.getTimestamp());
			System.out.println("state: " + msg3.getSyncState());
			assertEquals(msg3.getSyncState(), GlobalArgs.SYNC_STATE_RECEIVED);
		}
		assertEquals(1, msgs2.size());

		System.out.println("Finish testQueryAll()");
	}

	@Test
	public void test06()
			throws SQLException
	{
		for (int i = 0; i < 100; i++)
			test05QueryAll();
	}

}
