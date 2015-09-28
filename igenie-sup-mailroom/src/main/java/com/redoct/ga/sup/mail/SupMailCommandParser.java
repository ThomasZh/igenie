package com.redoct.ga.sup.mail;

import java.io.UnsupportedEncodingException;

import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommand;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.mail.adapter.SendForgotPwdAdapter;
import com.redoct.ga.sup.mail.adapter.SendFriendInviteAdapter;
import com.redoct.ga.sup.mail.adapter.SendHtmlEmailAdapter;
import com.redoct.ga.sup.mail.cmd.SendForgotPwdEmailResp;
import com.redoct.ga.sup.mail.cmd.SendFriendInviteEmailResp;
import com.redoct.ga.sup.mail.cmd.SendHtmlEmailResp;

public class SupMailCommandParser
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
		case SupCommandTag.SEND_HTML_EMAIL_REQ:
			return new SendHtmlEmailAdapter().decode(tlv);
		case SupCommandTag.SEND_HTML_EMAIL_RESP:
			return new SendHtmlEmailResp().decode(tlv);
		case SupCommandTag.SEND_FORGOT_PWD_EMAIL_REQ:
			return new SendForgotPwdAdapter().decode(tlv);
		case SupCommandTag.SEND_FORGOT_PWD_EMAIL_RESP:
			return new SendForgotPwdEmailResp().decode(tlv);
		case SupCommandTag.SEND_FRIEND_INVTE_EMAIL_REQ:
			return new SendFriendInviteAdapter().decode(tlv);
		case SupCommandTag.SEND_FRIEND_INVTE_EMAIL_RESP:
			return new SendFriendInviteEmailResp().decode(tlv);

		default:
			throw new UnsupportedEncodingException("This tlv pkg=[" + tlv.getTag() + "] has no implementation");
		}
	}

}
