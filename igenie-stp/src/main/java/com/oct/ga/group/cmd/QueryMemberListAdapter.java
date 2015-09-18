package com.oct.ga.group.cmd;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.group.QueryMemberListReq;
import com.oct.ga.comm.cmd.group.QueryMemberListResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.group.GroupMemberMasterInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaClubService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class QueryMemberListAdapter
		extends StpReqCommand
{
	public QueryMemberListAdapter()
	{
		super();

		this.setTag(Command.QUERY_MEMBER_LIST_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryMemberListReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		QueryMemberListResp respCmd = null;
		short channelType = reqCmd.getChannelType();
		String channelId = reqCmd.getChannelId();

		try {
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");

			List<String> userIds = null;
			switch (channelType) {
			case GlobalArgs.CHANNEL_TYPE_CLUB:
				userIds = clubService.querySubscriberIds(channelId);
				break;
			case GlobalArgs.CHANNEL_TYPE_ACTIVITY:
			case GlobalArgs.CHANNEL_TYPE_TASK:
			case GlobalArgs.CHANNEL_TYPE_QUESTION:
			default:
				userIds = groupService.queryMemberIds(channelId);
				break;
			}

			List<GroupMemberMasterInfo> members = new ArrayList<GroupMemberMasterInfo>();
			for (String userId : userIds) {
				GroupMemberMasterInfo member = new GroupMemberMasterInfo();

				// id, firstName, imageFileTransId
				AccountBasic account = accountService.queryAccount(userId);

				member.setAccountId(account.getAccountId());
				member.setNickname(account.getNickname());
				member.setAvatarUrl(account.getAvatarUrl());

				short memberRank = groupService.queryMemberRank(channelId, userId);
				member.setRank(memberRank);
				short memberState = groupService.queryMemberState(channelId, userId);
				member.setState(memberState);

				members.add(member);
			}

			respCmd = new QueryMemberListResp(ErrorCode.SUCCESS, members);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new QueryMemberListResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private QueryMemberListReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryMemberListAdapter.class);

}
