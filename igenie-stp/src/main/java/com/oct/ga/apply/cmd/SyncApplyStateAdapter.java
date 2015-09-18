package com.oct.ga.apply.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.mina.core.future.WriteFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.apply.SyncApplyStateReq;
import com.oct.ga.comm.cmd.apply.SyncApplyStateResp;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.service.GaApplyService;
import com.oct.ga.service.GaBadgeNumService;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.stp.cmd.StpReqCommand;
import com.redoct.ga.sup.account.SupAccountService;

public class SyncApplyStateAdapter
		extends StpReqCommand
{
	public SyncApplyStateAdapter()
	{
		super();

		this.setTag(Command.SYNC_APPLY_STATE_REQ);
	}

	@Override
	public StpReqCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new SyncApplyStateReq().decode(tlv);
		this.sequence = reqCmd.getSequence();

		return this;
	}

	@Override
	public RespCommand execute(ApplicationContext context)
			throws Exception
	{
		String myAccountId = this.getMyAccountId();

		try {
			GaApplyService applyService = (GaApplyService) context.getBean("gaApplyService");
			SupAccountService accountService = (SupAccountService) context.getBean("supAccountService");
			GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

			List<GaApplyStateNotify> notifies = applyService.queryNotReceived(myAccountId);

			for (GaApplyStateNotify notify : notifies) {
				AccountBasic fromAccount = accountService.queryAccount(notify.getFromAccountId());
				notify.setFromAccountName(fromAccount.getNickname());
				notify.setFromAccountAvatarUrl(fromAccount.getAvatarUrl());

				String groupName = groupService.queryGroupName(notify.getChannelId());
				notify.setChannelName(groupName);
				notify.setChatId(notify.getChannelId());
			}

			SyncApplyStateResp respCmd = new SyncApplyStateResp(sequence, ErrorCode.SUCCESS, notifies);
			TlvObject tlvResp = CommandParser.encode(respCmd);

			WriteFuture future = session.write(tlvResp);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (future.isWritten()) {
				for (GaApplyStateNotify notify : notifies) {
					// TODO android testing, not changed sync state
					applyService.modifySyncStateToReceived(notify.getFromAccountId(), notify.getToAccountId(),
							notify.getChannelId(), currentTimestamp);
				}

				GaBadgeNumService badgeNumService = (GaBadgeNumService) context.getBean("gaBadgeNumService");
				short applyNum = badgeNumService.countApplyNum(myAccountId);
				badgeNumService.modifyApplyNum(myAccountId, applyNum);
			}

			return null;
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + this.getMyDeviceId() + "]|accountId=["
					+ this.getMyAccountId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			SyncApplyStateResp respCmd = new SyncApplyStateResp(sequence, ErrorCode.UNKNOWN_FAILURE, null);
			return respCmd;
		}
	}

	private SyncApplyStateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SyncApplyStateAdapter.class);

}
