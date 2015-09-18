/**
 * 
 */
package test.unit.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;

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
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.domain.moment.GaMomentPhotoObject;
import com.oct.ga.moment.dao.GaMomentDao;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class GaMomentDaoTest
{
	private static String superId = UUID.randomUUID().toString();
	private static String channelId = UUID.randomUUID().toString();
	private static String momentId = UUID.randomUUID().toString();
	private static String userId = "10";
	private static String desc = "for test add to tomasino's task(🐟)";
	private static int timestamp = DatetimeUtil.currentTimestamp();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization gaMomentDao starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		dao = (GaMomentDao) applicationContext.getBean("gaMomentDao");

		logger.info("Initlization gaMomentDao success!");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");

		dao.removeMoment(channelId);
		dao.removeMomentPhoto(momentId);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp()
			throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown()
			throws Exception
	{
	}

	@Test
	public void test01Add()
	{
		logger.info("Inside testAdd()");

		dao.addMoment(superId, channelId, momentId, userId, desc, 2, timestamp);

		String photoId = "c3e8c697a46f621a134b1d9d868135e3";
		String photoUrl = "http://tripc2c-project-pic.b0.upaiyun.com/2014/12/25/" + photoId + ".jpg";
		dao.addMomentPhoto(superId, channelId, momentId, 0, photoId, photoUrl, timestamp);

		photoId = "d2bb64e02c85a961b276fb48f3bcd376";
		photoUrl = "http://tripc2c-project-pic.b0.upaiyun.com/2014/12/25/" + photoId + ".jpg";
		dao.addMomentPhoto(superId, channelId, momentId, 1, photoId, photoUrl, timestamp);

		logger.info("Finish testAdd()");
	}

	@Test
	public void test02QueryMomentPagination()
	{
		logger.info("Inside QueryMomentPagination()");

		short pageNum = 1;
		short pageSize = 10;
		Page<GaMomentObject> pages = dao.queryPagination(channelId, pageNum, pageSize);
		List<GaMomentObject> moments = pages.getPageItems();
		assertTrue(moments.size() > 0);

		for (GaMomentObject moment : moments) {
			logger.debug("moment id: " + moment.getMomentId());
			assertEquals(momentId, moment.getMomentId());
			logger.debug("userId: " + moment.getUserId());
			assertEquals(userId, moment.getUserId());
			logger.debug("moment desc: " + moment.getDesc());
			assertEquals(desc, moment.getDesc());
			logger.debug("timestamp: " + moment.getTimestamp());
			assertEquals(timestamp, moment.getTimestamp());

			List<String> photos = dao.queryMeomentPhotos(moment.getMomentId());
			assertEquals(photos.size(), 2);
			moment.setPhotos(photos);
		}

		JSONArray jsonArray = JSONArray.fromObject(moments);
		String json = jsonArray.toString();
		logger.debug("json: " + json);

		logger.info("Finish QueryMomentPagination()");
	}

	@Test
	public void test03QueryMomentPhotoFlowPagination()
	{
		logger.info("Inside QueryMomentPhotoFlowPagination()");

		short pageNum = 1;
		short pageSize = 10;
		Page<GaMomentPhotoObject> pages = dao.queryMomentPhotoFlowPagination(channelId, pageNum, pageSize);
		List<GaMomentPhotoObject> momentPhotos = pages.getPageItems();
		assertTrue(momentPhotos.size() > 0);

		for (GaMomentPhotoObject photo : momentPhotos) {
			GaMomentObject moment = dao.queryMoment(photo.getMomentId());
			logger.debug("moment id: " + moment.getMomentId());
			photo.setDesc(moment.getDesc());
			logger.debug("moment desc: " + moment.getDesc());
			photo.setUserId(moment.getUserId());
			logger.debug("userId: " + moment.getUserId());
		}

		JSONArray jsonArray = JSONArray.fromObject(momentPhotos);
		String json = jsonArray.toString();
		logger.debug("json: " + json);

		logger.info("Finish QueryMomentPhotoFlowPagination()");
	}

	@Test
	public void test04QueryClubMomentPhotoFlowPagination()
	{
		logger.info("Inside QueryClubMomentPhotoFlowPagination()");

		short pageNum = 1;
		short pageSize = 10;
		Page<GaMomentPhotoObject> pages = dao.queryClubMomentPhotoFlowPagination(superId, pageNum, pageSize);
		List<GaMomentPhotoObject> momentPhotos = pages.getPageItems();
		assertTrue(momentPhotos.size() > 0);

		for (GaMomentPhotoObject photo : momentPhotos) {
			GaMomentObject moment = dao.queryMoment(photo.getMomentId());
			logger.debug("moment id: " + moment.getMomentId());
			photo.setDesc(moment.getDesc());
			logger.debug("moment desc: " + moment.getDesc());
			photo.setUserId(moment.getUserId());
			logger.debug("userId: " + moment.getUserId());
		}

		JSONArray jsonArray = JSONArray.fromObject(momentPhotos);
		String json = jsonArray.toString();
		logger.debug("json: " + json);

		logger.info("Finish QueryClubMomentPhotoFlowPagination()");
	}

	@Test
	public void test05CountPhotoNum()
	{
		logger.info("Inside CountPhotoNum()");

		int num = dao.countPhotoNum(channelId);
		assertEquals(2, num);

		logger.info("Finish CountPhotoNum()");
	}

	private static GaMomentDao dao;
	private static final Logger logger = LoggerFactory.getLogger(GaMomentDaoTest.class);
}
