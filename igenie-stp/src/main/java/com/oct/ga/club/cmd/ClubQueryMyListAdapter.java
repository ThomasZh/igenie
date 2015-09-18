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
import com.oct.ga.comm.cmd.club.ClubQueryMyListReq;
import com.oct.ga.comm.cmd.club.ClubQueryMyListResp;
import com.oct.ga.comm.domain.club.ClubBaseInfo;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaClubService;
import com.oct.ga.stp.cmd.StpReqCommand;

public class ClubQueryMyListAdapter
		extends StpReqCommand
{
	public ClubQueryMyListAdapter()
	{
		super();

		this.setTag(Command.CLUB_QUERY_MYLIST_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new ClubQueryMyListReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		ClubQueryMyListResp respCmd = null;

		try {
			GaClubService clubService = (GaClubService) context.getBean("clubClubService");
//			GaFtpService ftpService = (GaFtpService) context.getBean("gaFtpService");

			List<ClubBaseInfo> clubs = clubService.queryNameListByUserId(this.getMyAccountId());

//			for (ClubBaseInfo club : clubs) {
				// change fileTransId => url
//				String titleBkImageUrl = ftpService.queryFileUrl(club.getTitleBkImage());
//				club.setTitleBkImage(titleBkImageUrl);
//				logger.debug("title background image url: " + club.getTitleBkImage());
//			}

			JSONArray jsonArray = JSONArray.fromObject(clubs);
			String json = jsonArray.toString();
			logger.debug("json: " + json);

			respCmd = new ClubQueryMyListResp(ErrorCode.SUCCESS, json);
			respCmd.setSequence(sequence);
			return respCmd;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			respCmd = new ClubQueryMyListResp(ErrorCode.UNKNOWN_FAILURE, null);
			respCmd.setSequence(sequence);
			return respCmd;
		}
	}

	private ClubQueryMyListReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(ClubQueryMyListAdapter.class);

}
