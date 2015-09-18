package com.redoct.ga.sup;

import java.io.UnsupportedEncodingException;

import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.account.cmd.ApplyVerificationCodeReq;
import com.redoct.ga.sup.account.cmd.ApplyVerificationCodeResp;
import com.redoct.ga.sup.account.cmd.CreateAccountReq;
import com.redoct.ga.sup.account.cmd.CreateAccountResp;
import com.redoct.ga.sup.account.cmd.CreateLoginReq;
import com.redoct.ga.sup.account.cmd.CreateLoginResp;
import com.redoct.ga.sup.account.cmd.CreateLostPwdEkeyReq;
import com.redoct.ga.sup.account.cmd.CreateLostPwdEkeyResp;
import com.redoct.ga.sup.account.cmd.ModifyAccountBasicInfoReq;
import com.redoct.ga.sup.account.cmd.ModifyAccountBasicInfoResp;
import com.redoct.ga.sup.account.cmd.ModifyAccountId4LoginReq;
import com.redoct.ga.sup.account.cmd.ModifyAccountId4LoginResp;
import com.redoct.ga.sup.account.cmd.ModifyPwdReq;
import com.redoct.ga.sup.account.cmd.ModifyPwdResp;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoByLoginNameReq;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoByLoginNameResp;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoReq;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoResp;
import com.redoct.ga.sup.account.cmd.QueryAccountDetailsReq;
import com.redoct.ga.sup.account.cmd.QueryAccountDetailsResp;
import com.redoct.ga.sup.account.cmd.QueryAccountMasterInfoReq;
import com.redoct.ga.sup.account.cmd.QueryAccountMasterInfoResp;
import com.redoct.ga.sup.account.cmd.QueryAllAccountBasicReq;
import com.redoct.ga.sup.account.cmd.QueryAllAccountBasicResp;
import com.redoct.ga.sup.account.cmd.QueryLoginNameReq;
import com.redoct.ga.sup.account.cmd.QueryLoginNameResp;
import com.redoct.ga.sup.account.cmd.QueryLostPwdEkeyInfoReq;
import com.redoct.ga.sup.account.cmd.QueryLostPwdEkeyInfoResp;
import com.redoct.ga.sup.account.cmd.QueryVerificationCodeReq;
import com.redoct.ga.sup.account.cmd.QueryVerificationCodeResp;
import com.redoct.ga.sup.account.cmd.VerifyLoginExistReq;
import com.redoct.ga.sup.account.cmd.VerifyLoginExistResp;
import com.redoct.ga.sup.account.cmd.VerifyLoginReq;
import com.redoct.ga.sup.account.cmd.VerifyLoginResp;
import com.redoct.ga.sup.device.cmd.ModifyDeviceClientVersionReq;
import com.redoct.ga.sup.device.cmd.ModifyDeviceClientVersionResp;
import com.redoct.ga.sup.device.cmd.ModifyDeviceOsVersionReq;
import com.redoct.ga.sup.device.cmd.ModifyDeviceOsVersionResp;
import com.redoct.ga.sup.device.cmd.QueryDeviceBasicInfoReq;
import com.redoct.ga.sup.device.cmd.QueryDeviceBasicInfoResp;
import com.redoct.ga.sup.mail.cmd.SendForgotPwdEmailReq;
import com.redoct.ga.sup.mail.cmd.SendForgotPwdEmailResp;
import com.redoct.ga.sup.mail.cmd.SendFriendInviteEmailReq;
import com.redoct.ga.sup.mail.cmd.SendFriendInviteEmailResp;
import com.redoct.ga.sup.mail.cmd.SendHtmlEmailReq;
import com.redoct.ga.sup.mail.cmd.SendHtmlEmailResp;
import com.redoct.ga.sup.message.cmd.MultcastActivityJoinReq;
import com.redoct.ga.sup.message.cmd.MultcastActivityJoinResp;
import com.redoct.ga.sup.message.cmd.MultcastApplyStateReq;
import com.redoct.ga.sup.message.cmd.MultcastApplyStateResp;
import com.redoct.ga.sup.message.cmd.MultcastInviteFeedbackReq;
import com.redoct.ga.sup.message.cmd.MultcastInviteFeedbackResp;
import com.redoct.ga.sup.message.cmd.MultcastInviteReq;
import com.redoct.ga.sup.message.cmd.MultcastInviteResp;
import com.redoct.ga.sup.message.cmd.MultcastMessageReq;
import com.redoct.ga.sup.message.cmd.MultcastMessageResp;
import com.redoct.ga.sup.message.cmd.MultcastModifyApproveStateReq;
import com.redoct.ga.sup.message.cmd.MultcastModifyApproveStateResp;
import com.redoct.ga.sup.message.cmd.MultcastTaskLogReq;
import com.redoct.ga.sup.message.cmd.MultcastTaskLogResp;
import com.redoct.ga.sup.session.cmd.ActiveStpSessionByTicketReq;
import com.redoct.ga.sup.session.cmd.ActiveStpSessionByTicketResp;
import com.redoct.ga.sup.session.cmd.ApplyGateTokenReq;
import com.redoct.ga.sup.session.cmd.ApplyGateTokenResp;
import com.redoct.ga.sup.session.cmd.ApplySessionTicketReq;
import com.redoct.ga.sup.session.cmd.ApplySessionTicketResp;
import com.redoct.ga.sup.session.cmd.InactiveStpSessionByTicketReq;
import com.redoct.ga.sup.session.cmd.InactiveStpSessionByTicketResp;
import com.redoct.ga.sup.session.cmd.InactiveStpSessionReq;
import com.redoct.ga.sup.session.cmd.InactiveStpSessionResp;
import com.redoct.ga.sup.session.cmd.QueryGateSessionReq;
import com.redoct.ga.sup.session.cmd.QueryGateSessionResp;
import com.redoct.ga.sup.session.cmd.QueryStpSessionByTicketReq;
import com.redoct.ga.sup.session.cmd.QueryStpSessionByTicketResp;
import com.redoct.ga.sup.session.cmd.QueryStpSessionReq;
import com.redoct.ga.sup.session.cmd.QueryStpSessionResp;
import com.redoct.ga.sup.session.cmd.RemoveStpSessionByTicketReq;
import com.redoct.ga.sup.session.cmd.RemoveStpSessionByTicketResp;
import com.redoct.ga.sup.session.cmd.RemoveStpSessionReq;
import com.redoct.ga.sup.session.cmd.RemoveStpSessionResp;
import com.redoct.ga.sup.session.cmd.VerifyGateTokenReq;
import com.redoct.ga.sup.session.cmd.VerifyGateTokenResp;
import com.redoct.ga.sup.sms.cmd.SendVerificationCodeReq;
import com.redoct.ga.sup.sms.cmd.SendVerificationCodeResp;

public class SupCommandParser
{
	// ///////////////////////////////////////////////////////////////////////////////////////
	// encode to send...

	public static TlvObject encode(SupCommand cmd)
			throws UnsupportedEncodingException
	{
		return cmd.encode();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// decode to handle

	public static SupCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		switch (tlv.getTag()) {
		case SupCommandTag.VERIFY_LOGIN_EXIST_REQ:
			return new VerifyLoginExistReq().decode(tlv);
		case SupCommandTag.VERIFY_LOGIN_EXIST_RESP:
			return new VerifyLoginExistResp().decode(tlv);
		case SupCommandTag.VERIFY_LOGIN_REQ:
			return new VerifyLoginReq().decode(tlv);
		case SupCommandTag.VERIFY_LOGIN_RESP:
			return new VerifyLoginResp().decode(tlv);
		case SupCommandTag.MODIFY_PWD_REQ:
			return new ModifyPwdReq().decode(tlv);
		case SupCommandTag.MODIFY_PWD_RESP:
			return new ModifyPwdResp().decode(tlv);
		case SupCommandTag.CREATE_ACCOUNT_REQ:
			return new CreateAccountReq().decode(tlv);
		case SupCommandTag.CREATE_ACCOUNT_RESP:
			return new CreateAccountResp().decode(tlv);
		case SupCommandTag.CREATE_LOGIN_REQ:
			return new CreateLoginReq().decode(tlv);
		case SupCommandTag.CREATE_LOGIN_RESP:
			return new CreateLoginResp().decode(tlv);
		case SupCommandTag.MODIFY_ACCOUNT_LOGIN_REQ:
			return new ModifyAccountId4LoginReq().decode(tlv);
		case SupCommandTag.MODIFY_ACCOUNT_LOGIN_RESP:
			return new ModifyAccountId4LoginResp().decode(tlv);
		case SupCommandTag.MODIFY_ACCOUNT_INFO_REQ:
			return new ModifyAccountBasicInfoReq().decode(tlv);
		case SupCommandTag.MODIFY_ACCOUNT_INFO_RESP:
			return new ModifyAccountBasicInfoResp().decode(tlv);
		case SupCommandTag.CREATE_LOST_PWD_EKEY_REQ:
			return new CreateLostPwdEkeyReq().decode(tlv);
		case SupCommandTag.CREATE_LOST_PWD_EKEY_RESP:
			return new CreateLostPwdEkeyResp().decode(tlv);
		case SupCommandTag.QUERY_LOST_PWD_EKEY_INFO_REQ:
			return new QueryLostPwdEkeyInfoReq().decode(tlv);
		case SupCommandTag.QUERY_LOST_PWD_EKEY_INFO_RESP:
			return new QueryLostPwdEkeyInfoResp().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_INFO_REQ:
			return new QueryAccountBasicInfoReq().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_INFO_RESP:
			return new QueryAccountBasicInfoResp().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_INFO_BY_LOGIN_NAME_REQ:
			return new QueryAccountBasicInfoByLoginNameReq().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_INFO_BY_LOGIN_NAME_RESP:
			return new QueryAccountBasicInfoByLoginNameResp().decode(tlv);
		case SupCommandTag.APPLY_VERIFICATION_CODE_REQ:
			return new ApplyVerificationCodeReq().decode(tlv);
		case SupCommandTag.APPLY_VERIFICATION_CODE_RESP:
			return new ApplyVerificationCodeResp().decode(tlv);
		case SupCommandTag.QUERY_VERIFICATION_CODE_REQ:
			return new QueryVerificationCodeReq().decode(tlv);
		case SupCommandTag.QUERY_VERIFICATION_CODE_RESP:
			return new QueryVerificationCodeResp().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_MASTER_INFO_REQ:
			return new QueryAccountMasterInfoReq().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNT_MASTER_INFO_RESP:
			return new QueryAccountMasterInfoResp().decode(tlv);
		case SupCommandTag.QUERY_ALL_ACCOUNT_BASIC_REQ:
			return new QueryAllAccountBasicReq().decode(tlv);
		case SupCommandTag.QUERY_ALL_ACCOUNT_BASIC_RESP:
			return new QueryAllAccountBasicResp().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNTS_DETAIL_REQ:
			return new QueryAccountDetailsReq().decode(tlv);
		case SupCommandTag.QUERY_ACCOUNTS_DETAIL_RESP:
			return new QueryAccountDetailsResp().decode(tlv);

		case SupCommandTag.QUERY_DEVICE_INFO_REQ:
			return new QueryDeviceBasicInfoReq().decode(tlv);
		case SupCommandTag.QUERY_DEVICE_INFO_RESP:
			return new QueryDeviceBasicInfoResp().decode(tlv);
		case SupCommandTag.MODIFY_DEVICE_CLIENT_VERSION_REQ:
			return new ModifyDeviceClientVersionReq().decode(tlv);
		case SupCommandTag.MODIFY_DEVICE_CLIENT_VERSION_RESP:
			return new ModifyDeviceClientVersionResp().decode(tlv);
		case SupCommandTag.MODIFY_DEVICE_OS_VERSION_REQ:
			return new ModifyDeviceOsVersionReq().decode(tlv);
		case SupCommandTag.MODIFY_DEVICE_OS_VERSION_RESP:
			return new ModifyDeviceOsVersionResp().decode(tlv);

		case SupCommandTag.APPLY_GATE_TOKEN_REQ:
			return new ApplyGateTokenReq().decode(tlv);
		case SupCommandTag.APPLY_GATE_TOKEN_RESP:
			return new ApplyGateTokenResp().decode(tlv);
		case SupCommandTag.VERIFY_GATE_TOKEN_REQ:
			return new VerifyGateTokenReq().decode(tlv);
		case SupCommandTag.VERIFY_GATE_TOKEN_RESP:
			return new VerifyGateTokenResp().decode(tlv);
		case SupCommandTag.APPLY_SESSION_TICKET_REQ:
			return new ApplySessionTicketReq().decode(tlv);
		case SupCommandTag.APPLY_SESSION_TICKET_RESP:
			return new ApplySessionTicketResp().decode(tlv);
		case SupCommandTag.QUERY_STP_SESSION_REQ:
			return new QueryStpSessionReq().decode(tlv);
		case SupCommandTag.QUERY_STP_SESSION_RESP:
			return new QueryStpSessionResp().decode(tlv);
		case SupCommandTag.REMOVE_STP_SESSION_REQ:
			return new RemoveStpSessionReq().decode(tlv);
		case SupCommandTag.REMOVE_STP_SESSION_RESP:
			return new RemoveStpSessionResp().decode(tlv);
		case SupCommandTag.INACTIVE_STP_SESSION_REQ:
			return new InactiveStpSessionReq().decode(tlv);
		case SupCommandTag.INACTIVE_STP_SESSION_RESP:
			return new InactiveStpSessionResp().decode(tlv);
		case SupCommandTag.ACTIVE_STP_SESSION_BY_TICKET_REQ:
			return new ActiveStpSessionByTicketReq().decode(tlv);
		case SupCommandTag.ACTIVE_STP_SESSION_BY_TICKET_RESP:
			return new ActiveStpSessionByTicketResp().decode(tlv);
		case SupCommandTag.QUERY_GATE_SESSION_REQ:
			return new QueryGateSessionReq().decode(tlv);
		case SupCommandTag.QUERY_GATE_SESSION_RESP:
			return new QueryGateSessionResp().decode(tlv);
		case SupCommandTag.QUERY_LOGIN_NAME_REQ:
			return new QueryLoginNameReq().decode(tlv);
		case SupCommandTag.QUERY_LOGIN_NAME_RESP:
			return new QueryLoginNameResp().decode(tlv);
		case SupCommandTag.QUERY_STP_SESSION_BY_TICKET_REQ:
			return new QueryStpSessionByTicketReq().decode(tlv);
		case SupCommandTag.QUERY_STP_SESSION_BY_TICKET_RESP:
			return new QueryStpSessionByTicketResp().decode(tlv);
		case SupCommandTag.REMOVE_STP_SESSION_BY_TICKET_REQ:
			return new RemoveStpSessionByTicketReq().decode(tlv);
		case SupCommandTag.REMOVE_STP_SESSION_BY_TICKET_RESP:
			return new RemoveStpSessionByTicketResp().decode(tlv);
		case SupCommandTag.INACTIVE_STP_SESSION_BY_TICKET_REQ:
			return new InactiveStpSessionByTicketReq().decode(tlv);
		case SupCommandTag.INACTIVE_STP_SESSION_BY_TICKET_RESP:
			return new InactiveStpSessionByTicketResp().decode(tlv);

		case SupCommandTag.SEND_HTML_EMAIL_REQ:
			return new SendHtmlEmailReq().decode(tlv);
		case SupCommandTag.SEND_HTML_EMAIL_RESP:
			return new SendHtmlEmailResp().decode(tlv);
		case SupCommandTag.SEND_FORGOT_PWD_EMAIL_REQ:
			return new SendForgotPwdEmailReq().decode(tlv);
		case SupCommandTag.SEND_FORGOT_PWD_EMAIL_RESP:
			return new SendForgotPwdEmailResp().decode(tlv);
		case SupCommandTag.SEND_FRIEND_INVTE_EMAIL_REQ:
			return new SendFriendInviteEmailReq().decode(tlv);
		case SupCommandTag.SEND_FRIEND_INVTE_EMAIL_RESP:
			return new SendFriendInviteEmailResp().decode(tlv);

		case SupCommandTag.SEND_SMS_VERIFICATION_REQ:
			return new SendVerificationCodeReq().decode(tlv);
		case SupCommandTag.SEND_SMS_VERIFICATION_RESP:
			return new SendVerificationCodeResp().decode(tlv);

		case SupCommandTag.MULTCAST_MESSAGE_REQ:
			return new MultcastMessageReq().decode(tlv);
		case SupCommandTag.MULTCAST_MESSAGE_RESP:
			return new MultcastMessageResp().decode(tlv);
		case SupCommandTag.MULTCAST_INVITE_REQ:
			return new MultcastInviteReq().decode(tlv);
		case SupCommandTag.MULTCAST_INVITE_RESP:
			return new MultcastInviteResp().decode(tlv);
		case SupCommandTag.MULTCAST_INVITE_FEEDBACK_REQ:
			return new MultcastInviteFeedbackReq().decode(tlv);
		case SupCommandTag.MULTCAST_INVITE_FEEDBACK_RESP:
			return new MultcastInviteFeedbackResp().decode(tlv);
		case SupCommandTag.MULTCAST_MODIFY_APPLY_STATE_REQ:
			return new MultcastModifyApproveStateReq().decode(tlv);
		case SupCommandTag.MULTCAST_MODIFY_APPLY_STATE_RESP:
			return new MultcastModifyApproveStateResp().decode(tlv);
		case SupCommandTag.MULTCAST_APPLY_STATE_REQ:
			return new MultcastApplyStateReq().decode(tlv);
		case SupCommandTag.MULTCAST_APPLY_STATE_RESP:
			return new MultcastApplyStateResp().decode(tlv);
		case SupCommandTag.MULTCAST_ACTIVITY_JOIN_REQ:
			return new MultcastActivityJoinReq().decode(tlv);
		case SupCommandTag.MULTCAST_ACTIVITY_JOIN_RESP:
			return new MultcastActivityJoinResp().decode(tlv);
		case SupCommandTag.MULTCAST_TASK_LOG_REQ:
			return new MultcastTaskLogReq().decode(tlv);
		case SupCommandTag.MULTCAST_TASK_LOG_RESP:
			return new MultcastTaskLogResp().decode(tlv);

		default:
			throw new UnsupportedEncodingException("This tlv pkg=[" + tlv.getTag() + "] has no implementation");
		}
	}

}
