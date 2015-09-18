package test.unit.cmd;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.invite.ConfirmReceivedInviteReq;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvByteUtilPrinter;
import com.oct.ga.comm.tlv.TlvObject;

public class ConfirmReceivedInviteReqTest
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
		String[] inviteIds = new String[2];
		inviteIds[0] = UUID.randomUUID().toString();
		inviteIds[1] = UUID.randomUUID().toString();
		String[] inviteFeedbackIds = new String[2];
		inviteFeedbackIds[0] = UUID.randomUUID().toString();
		inviteFeedbackIds[1] = UUID.randomUUID().toString();

		ConfirmReceivedInviteReq reqCmd = new ConfirmReceivedInviteReq(inviteIds, inviteFeedbackIds);
		TlvObject reqTlv = CommandParser.encode(reqCmd);

		byte[] b = reqTlv.toBytes();
		TlvByteUtilPrinter.hexDump("tlv package", b);
		byte[] b2 = TlvByteUtil.sub(b, 6);
		TlvObject tlv = new TlvObject(Command.INVITE_CONFIRM_RECEIVED_REQ, b2);
		ConfirmReceivedInviteReq reqCmd2 = (ConfirmReceivedInviteReq) CommandParser.decode(tlv);

		String[] inviteids = reqCmd2.getInviteIds();
		if (inviteids != null) {
			for (String id : inviteids)
				System.out.println("invite id: " + id);
		}
		String[] inviteFeedbackids = reqCmd2.getInviteFeedbackIds();
		if (inviteFeedbackids != null) {
			for (String id : inviteFeedbackids)
				System.out.println("inviteFeedback id: " + id);
		}
	}

}
