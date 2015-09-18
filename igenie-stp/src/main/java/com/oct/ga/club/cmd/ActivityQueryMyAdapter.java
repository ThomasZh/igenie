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
import com.oct.ga.comm.cmd.club.ActivityQueryMyReq;
import com.oct.ga.comm.cmd.club.ActivityQueryMyResp;
import com.oct.ga.comm.domain.club.ActivityNameListInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaActivityService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ActivityQueryMyAdapter
		extends StpReqCommand
{
	public ActivityQueryMyAdapter()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_MY_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ActivityQueryMyReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ActivityQueryMyResp respCmd = null;

		try {
			GaActivityService activityService = (GaActivityService) context.getBean("clubActivityService");
			List<ActivityNameListInfo> array = activityService.queryMyList(this.getMyAccountId());

			JSONArray jsonArray = JSONArray.fromObject(array);
			String json = jsonArray.toString();
			logger.debug("json: " + json);

			respCmd = new ActivityQueryMyResp(ErrorCode.SUCCESS, json);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
			
			respCmd = new ActivityQueryMyResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ActivityQueryMyReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryMyAdapter.class);

}
