package test.unit.dao2;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.task.TaskNote;
import com.oct.ga.task.dao.GaTaskNoteDao;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class GaTaskNoteDaoTest
{

	private static GaTaskNoteDao dao;
	private static TaskNote taskNote;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		System.out.println("Initlization dao");

		String springXmlFile = "classpath:application-config.xml";
		// String springXmlFile = "classpath:mongodb-context.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		dao = (GaTaskNoteDao) applicationContext.getBean("gaTaskNoteDao");

		System.out.println("Initlization taskNote");

		int timestamp = (int) (System.currentTimeMillis() / 1000);
		taskNote = new TaskNote();
		taskNote.setNoteId(UUID.randomUUID().toString());
		taskNote.setAccountId(UUID.randomUUID().toString());
		taskNote.setAccountName("createName");
		taskNote.setTimestamp(timestamp);
		taskNote.setTaskId(UUID.randomUUID().toString());
		taskNote.setTxt("txt");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		System.out.println("Remove");
		dao.updateState(taskNote.getNoteId(), GlobalArgs.NOTE_STATE_DELETED, 0);
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
		dao.add(taskNote, 0);
		System.out.println("Finish testAdd()");
	}

	@Test
	public void test02QueryLastUpdate()
	{
		System.out.println("Inside queryLastUpdate()");
		List<TaskNote> taskNotes = dao.queryLastUpdate(taskNote.getTaskId(), 0);
		for (TaskNote taskNote2 : taskNotes) {
			System.out.println("accountName: " + taskNote2.getAccountName());
			assertEquals(taskNote2.getAccountName(), taskNote.getAccountName());
		}
		assertTrue(taskNotes.size() > 0);
		System.out.println("Finish queryLastUpdate()");
	}

	@Test
	public void test03Update()
	{
		System.out.println("Inside testUpdate()");
		int timestamp = (int) (System.currentTimeMillis() / 1000);
		taskNote.setTimestamp(timestamp);
		dao.update(taskNote, 0);
		System.out.println("Finish testUpdate()");
	}

	@Test
	public void test04QueryLastUpdate()
	{
		System.out.println("Inside queryLastUpdate()");
		List<TaskNote> taskNotes = dao.queryLastUpdate(taskNote.getTaskId(), 0);
		for (TaskNote taskNote2 : taskNotes) {
			System.out.println("accountName: " + taskNote2.getAccountName());
			assertEquals(taskNote2.getAccountName(), taskNote.getAccountName());
			assertTrue(taskNote2.getTimestamp() >= taskNote.getTimestamp());
		}
		assertTrue(taskNotes.size() > 0);
		System.out.println("Finish queryLastUpdate()");
	}
}
