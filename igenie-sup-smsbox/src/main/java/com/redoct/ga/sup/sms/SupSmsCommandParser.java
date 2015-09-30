package com.redoct.ga.sup.sms;

import java.io.UnsupportedEncodingException;

import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommand;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.sms.adapter.SendVerificationCodeAdapter;
import com.redoct.ga.sup.sms.cmd.SendVerificationCodeResp;

public class SupSmsCommandParser
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
		case SupCommandTag.SEND_SMS_VERIFICATION_REQ:
			return new SendVerificationCodeAdapter().decode(tlv);
		case SupCommandTag.SEND_SMS_VERIFICATION_RESP:
			return new SendVerificationCodeResp().decode(tlv);

		default:
			throw new UnsupportedEncodingException("This tlv pkg=[" + tlv.getTag() + "] has no implementation");
		}
	}

}
