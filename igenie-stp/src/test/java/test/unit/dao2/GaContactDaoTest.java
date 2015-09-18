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

import com.oct.ga.addrbook.dao.GaContactDao;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.Contact;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class GaContactDaoTest {

	private static GaContactDao dao;
	private static Contact contact;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Initlization dao");

		String springXmlFile = "classpath:application-config.xml";
		// String springXmlFile = "classpath:mongodb-context.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		dao = (GaContactDao) applicationContext.getBean("gaContactDao");

		System.out.println("Initlization contact");
		int timestamp = (int) (System.currentTimeMillis() / 1000);
		contact = new Contact();
		String contactId = UUID.randomUUID().toString();
		contact.setContactId(contactId);
		contact.setNickname("you");
		contact.setEmail("your@email.com");
		contact.setTelephone("12345678900");
		contact.setFacePhoto(null);
		contact.setMyAccountID(UUID.randomUUID().toString());
		contact.setAccountId(UUID.randomUUID().toString());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Remove");
		dao.remove(contact.getContactId());
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test01Add() {
		System.out.println("Inside testAdd()");
		dao.add(contact);
		System.out.println("contact: " + contact.getContactId());
		System.out.println("Finish testAdd()");
	}

	@Test
	public void test02QuieryById() {
		System.out.println("Inside testQuieryById()");
		Contact friendContact = dao.queryContact(contact.getContactId());
		System.out.println("contact email: " + friendContact.getEmail());
		System.out.println("contact state: " + friendContact.getState());
		assertEquals(contact.getEmail(), friendContact.getEmail());
		System.out.println("Finish testQuieryById()");
	}

	@Test
	public void test03QueryContactByAccountId() {
		System.out.println("Inside testQueryContactByAccountId()");
		Contact friendContact = dao.queryContactByAccountId(contact.getMyAccountID(), contact.getAccountId());
		System.out.println("contact email: " + friendContact.getEmail());
		System.out.println("contact state: " + friendContact.getState());
		assertEquals(contact.getEmail(), friendContact.getEmail());
		System.out.println("Finish testQueryContactByAccountId()");
	}

	@Test
	public void test04Update() {
		System.out.println("Inside testUpdate()");
		contact.setState(GlobalArgs.CONTACT_STATE_FRIEND);
		dao.update(contact);
		System.out.println("Finish testUpdate()");
	}

	@Test
	public void test05QueryLastUpdateContact() {
		System.out.println("Inside queryLastUpdateContact()");
		List<Contact> friends = dao.queryLastUpdateContact(contact.getMyAccountID(), 0);
		for (Contact contact3 : friends) {
			System.out.println("contact email: " + contact3.getEmail());
			System.out.println("contact state: " + contact3.getState());
			assertEquals(contact.getEmail(), contact3.getEmail());
		}
		assertTrue(friends.size() > 0);

		System.out.println("Finish queryLastUpdateContact()");
	}
}
