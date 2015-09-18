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
import com.oct.ga.comm.cmd.club.ActivityQueryImagesPaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQueryImagesPaginationResp;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.club.ActivityExtendInfo;
import com.oct.ga.comm.domain.club.ActivityMasterInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityQueryImagesPaginationAdapter
		extends StpReqCommand
{
	public ActivityQueryImagesPaginationAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_HISTORY_PAGINATION_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityQueryImagesPaginationReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityQueryImagesPaginationResp respCmd = null;
		String clubId = reqCmd.getClubId();
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			Page<ActivityExtendInfo> avtivities = activityService.queryImagesPagination(clubId, pageNum, pageSize);
			List<ActivityExtendInfo> array = avtivities.getPageItems();

			for (ActivityMasterInfo activity : array) {
				short memberRank = groupService.queryMemberRank(activity.getId(), this.getMyAccountId());
				activity.setMemberRank(memberRank);
			}

			JSONArray jsonArray = JSONArray.fromObject(array);
			String json = jsonArray.toString();
			logger.debug("json: " + json);

			respCmd = new ActivityQueryImagesPaginationResp(ErrorCode.SUCCESS, json);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ActivityQueryImagesPaginationResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityQueryImagesPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryImagesPaginationAdapter.class);

}
