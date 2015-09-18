package test.unit.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.oct.ga.comm.domain.msg.GaInvite;

public class ClubTest
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
		ClubMasterInfo info = new ClubMasterInfo();

		info.setCreatorId("creatorId");
		info.setDesc("desc");
		info.setId("id");
		info.setMemberNum(1);
		info.setName("name");

		String[] ids = new String[2];
		ids[0] = "001";
		ids[1] = "002";
		info.setSharingUserIds(ids);

		info.setSubscriberNum(1);
		info.setTitleBkImage("imageUrl");

		String json = info.encode();
		System.out.println(json);

		ClubMasterInfo info2 = info.decode(json);
		System.out.println("creator id: " + info2.getCreatorId());
		System.out.println("desc: " + info2.getDesc());
		System.out.println("id: " + info2.getId());
		System.out.println("member num: " + info2.getMemberNum());
		System.out.println("name: " + info2.getName());
		System.out.println("describer num: " + info2.getSubscriberNum());
		System.out.println("image url: " + info2.getTitleBkImage());
		for (String id : info2.getSharingUserIds()) {
			System.out.println("sharing to user id: " + id);
		}
	}

	//
	// Converts a collection of string object into JSON string.
	//
	@Test
	public void test2()
	{
		List<String> names = new ArrayList<String>();
		names.add("Alice");
		names.add("Bob");
		names.add("Carol");
		names.add("Mallory");

		Gson gson = new Gson();
		String jsonNames = gson.toJson(names);
		System.out.println("jsonNames = " + jsonNames);
	}

	//
	// Converts a collection AccountBaseInfo object into JSON string
	//
	@Test
	public void test3()
	{
		AccountBasic a = new AccountBasic();
		AccountBasic b = new AccountBasic();
		AccountBasic c = new AccountBasic();
		AccountBasic d = new AccountBasic();

		List<AccountBasic> list = new ArrayList<AccountBasic>();
		list.add(a);
		list.add(b);
		list.add(c);
		list.add(d);

		Gson gson = new Gson();
		String jsonStudents = gson.toJson(list);
		System.out.println("jsonStudents = " + jsonStudents);
	}

	//
	// Converts JSON string into a collection of AccountBaseInfo object.
	//
	@Test
	public void test4()
	{
		String json = "";

		Type type = new TypeToken<List<AccountBasic>>()
		{
		}.getType();
		Gson gson = new Gson();
		List<AccountBasic> list = gson.fromJson(json, type);

		for (AccountBasic a : list) {
			System.out.println("account.getName() = " + a.getNickname());
		}
	}

	//
	// Converts JSON string into a collection of AccountBaseInfo object.
	//
	@Test
	public void test5()
	{
		List<GaInvite> inviteList = new ArrayList<GaInvite>();
		GaInvite invite = new GaInvite();
		inviteList.add(invite);
		
		Gson gson = new Gson();
		String jsonInviteList = gson.toJson(inviteList);
		System.out.println("invites json: " + jsonInviteList);
	}

}
