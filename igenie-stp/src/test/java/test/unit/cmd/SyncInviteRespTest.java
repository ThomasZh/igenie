package test.unit.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.invite.SyncInviteResp;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvByteUtilPrinter;
import com.oct.ga.comm.tlv.TlvObject;

public class SyncInviteRespTest
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
			throws UnsupportedEncodingException
	{
		// String jsonInviteList =
		// "[{'inviteId':'ac9e0213-9e62-401f-8afe-b1e24f11488d','inviteType':147,'fromUserId':'55712d4b-0a86-4b7b-94d6-d9630127ad06','fromUserName':'As','toUserSemiId':'33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e','channelType':0,'channelId':'','channelName':'','expiry':1424331519,'timestamp':1423726719},{'inviteId':'bc46bde9-f324-41ff-b5e5-086545028a7a','inviteType':147,'fromUserId':'93ae8bfb-92d2-4c78-b96f-5063b0e0ae0e','fromUserName':'Bb','toUserSemiId':'33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e','channelType':0,'channelId':'','channelName':'','expiry':1424331618,'timestamp':1423726818}]";

		List<GaInvite> inviteList = new ArrayList<GaInvite>();
		GaInvite invite = new GaInvite();
		invite.setInviteId("ac9e0213-9e62-401f-8afe-b1e24f11488d");
		invite.setInviteType(GlobalArgs.INVITE_TYPE_FOLLOW_ME);
		invite.setFromAccountId("55712d4b-0a86-4b7b-94d6-d9630127ad06");
		invite.setFromAccountName("A");
		invite.setToUserSemiId("33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e");
		invite.setExpiry(1424331519);
		invite.setTimestamp(1423726719);
		inviteList.add(invite);
		List<GaInviteFeedback> inviteFeedbackList = null;

		SyncInviteResp respCmd = new SyncInviteResp(ErrorCode.SUCCESS, inviteList, inviteFeedbackList);
		TlvObject respTlv = CommandParser.encode(respCmd);

		byte[] b = respTlv.toBytes();
		TlvByteUtilPrinter.hexDump("tlv package", b);
		byte[] b2 = TlvByteUtil.sub(b, 6);
		TlvObject tlv = new TlvObject(Command.INVITE_SYNC_RESP, b2);
		SyncInviteResp respCmd2 = (SyncInviteResp) CommandParser.decode(tlv);

		List<GaInvite> invitelist = respCmd2.getInviteList();
		if (invitelist != null) {
			System.out.println("invitelist size: " + invitelist.size());
		} else {
			System.out.println("invitelist size: 0");
		}
		List<GaInviteFeedback> inviteFeedbacklist = respCmd2.getInviteFeedbackList();
		if (inviteFeedbacklist != null) {
			System.out.println("inviteFeedbacklist size: " + inviteFeedbacklist.size());
		} else {
			System.out.println("inviteFeedbacklist size: 0");
		}
	}
}
