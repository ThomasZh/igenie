package com.oct.ga.club.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ActivityQueryMemberReq;
import com.oct.ga.comm.cmd.club.ActivityQueryMemberResp;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityQueryMemberAdapter
		extends StpReqCommand
{
	public ActivityQueryMemberAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_MEMBER_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityQueryMemberReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityQueryMemberResp respCmd = null;
		String activityId = reqCmd.getActivityId();

		try {
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			List<GroupMemberDetailInfo> memberList = groupService.queryMembers(activityId);
			JSONArray jsonArray = JSONArray.fromObject(memberList);
			String json = jsonArray.toString();

			respCmd = new ActivityQueryMemberResp(ErrorCode.SUCCESS, json);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityQueryMemberResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityQueryMemberReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryMemberAdapter.class);

}
