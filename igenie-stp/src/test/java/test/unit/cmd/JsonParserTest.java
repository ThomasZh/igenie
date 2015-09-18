package test.unit.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.invite.SyncInviteResp;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;

public class JsonParserTest
{
	public static void main(String[] args)
			throws UnsupportedEncodingException
	{
		String jsonInviteList = "[{'inviteId':'ac9e0213-9e62-401f-8afe-b1e24f11488d','inviteType':147,'fromUserId':'55712d4b-0a86-4b7b-94d6-d9630127ad06','fromUserName':'As','toUserSemiId':'33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e','channelType':0,'channelId':'','channelName':'','expiry':1424331519,'timestamp':1423726719},{'inviteId':'bc46bde9-f324-41ff-b5e5-086545028a7a','inviteType':147,'fromUserId':'93ae8bfb-92d2-4c78-b96f-5063b0e0ae0e','fromUserName':'Bb','toUserSemiId':'33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e','channelType':0,'channelId':'','channelName':'','expiry':1424331618,'timestamp':1423726818}]";
		Gson gson = new Gson();
		List<GaInvite> inviteList = gson.fromJson(jsonInviteList, new TypeToken<List<GaInvite>>()
		{
		}.getType());
		System.out.println("size: " + inviteList.size());

		List<GaInviteFeedback> inviteFeedbacklist = new ArrayList<GaInviteFeedback>();

		SyncInviteResp respCmd = new SyncInviteResp(Command.INVITE_SYNC_RESP, inviteList, inviteFeedbacklist);
		TlvObject tlv = CommandParser.encode(respCmd);
		SyncInviteResp respCmd2 = (SyncInviteResp) CommandParser.decode(tlv);
	}

	private void a()
			throws UnsupportedEncodingException
	{
		String jsonInviteList = "[{'inviteId':'ac9e0213-9e62-401f-8afe-b1e24f11488d','inviteType':147,'fromUserId':'55712d4b-0a86-4b7b-94d6-d9630127ad06','fromUserName':'As','toUserSemiId':'33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e','channelType':0,'channelId':'','channelName':'','expiry':1424331519,'timestamp':1423726719},{'inviteId':'bc46bde9-f324-41ff-b5e5-086545028a7a','inviteType':147,'fromUserId':'93ae8bfb-92d2-4c78-b96f-5063b0e0ae0e','fromUserName':'Bb','toUserSemiId':'33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e','channelType':0,'channelId':'','channelName':'','expiry':1424331618,'timestamp':1423726818}]";
		System.out.println("invites json: " + jsonInviteList);
		String jsonInviteFeedbackList = null;
		System.out.println("inviteFeedbacks json: " + jsonInviteFeedbackList);

		TlvObject tResultFlag = new TlvObject(1, TlvByteUtil.short2Byte(ErrorCode.SUCCESS));
		TlvObject tInviteList = new TlvObject(2, jsonInviteList);
		TlvObject tInviteFeedbackList = new TlvObject(3, jsonInviteFeedbackList);
		TlvObject tlv = new TlvObject(Command.INVITE_SYNC_RESP);
		tlv.push(tResultFlag);
		tlv.push(tInviteList);
		tlv.push(tInviteFeedbackList);
		System.out.println("child: " + tlv.getChildCount());
		System.out.println("length: " + tlv.getLength());

		SyncInviteResp respCmd = (SyncInviteResp) CommandParser.decode(tlv);
		List<GaInvite> invitelist = respCmd.getInviteList();
		System.out.println("size: " + invitelist.size());
	}

}
