package test.unit.gson;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.desc.GaDescCell;
import com.oct.ga.comm.domain.desc.GaDescChapter;

public class ActivityDescTest
{

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
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
	public void test()
	{
		Gson gson = new Gson();
		List<GaDescChapter> chapters = new ArrayList<GaDescChapter>();

		GaDescChapter chapter1 = new GaDescChapter();
		chapter1.setSeq((short) 1);
		chapter1.setTitle("1.触摸Touch");

		List<GaDescCell> cells1 = new ArrayList<GaDescCell>();
		GaDescCell cell1 = new GaDescCell();
		cell1.setSeq((short) 1);
		cell1.setType(GlobalArgs.CONTENT_TYPE_TXT);
		cell1.setTxt("法国：5分钟，制作人：Jean-Baptiste Chandelier，类别：滑翔伞/Paraglide");
		cells1.add(cell1);

		GaDescCell cell2 = new GaDescCell();
		cell2.setSeq((short) 2);
		cell2.setType(GlobalArgs.CONTENT_TYPE_PIC);
		cell2.setTxt("http://img3.douban.com/view/event_poster/raw/public/f30f88f3f822d1e.jpg");
		cells1.add(cell2);

		chapter1.setCells(cells1);
		String json1 = gson.toJson(chapter1);
		System.out.println(json1);

		chapters.add(chapter1);

		GaDescChapter chapter2 = new GaDescChapter();
		chapter2.setSeq((short) 2);
		chapter2.setTitle("2.雪线独行");

		List<GaDescCell> cells2 = new ArrayList<GaDescCell>();
		GaDescCell cell3 = new GaDescCell();
		cell3.setSeq((short) 1);
		cell3.setType(GlobalArgs.CONTENT_TYPE_TXT);
		cell3.setTxt("美国：7分钟，制作人：Tyler Wikinson-Ray，类别：滑雪/Ski");
		cells2.add(cell3);

		GaDescCell cell4 = new GaDescCell();
		cell4.setSeq((short) 2);
		cell4.setType(GlobalArgs.CONTENT_TYPE_PIC);
		cell4.setTxt("http://img3.douban.com/view/photo/photo/public/p2246259653.jpg");
		cells2.add(cell4);

		chapter2.setCells(cells2);
		String json2 = gson.toJson(chapter2);
		System.out.println(json2);

		chapters.add(chapter2);

		String json = gson.toJson(chapters);
		System.out.println(json);

		fail("Not yet implemented");
	}

}
