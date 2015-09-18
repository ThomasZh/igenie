package com.redoct.ga.sup.session;

import java.io.UnsupportedEncodingException;

import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommand;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.session.adapter.ActiveStpSessionByTicketAdapter;
import com.redoct.ga.sup.session.adapter.ApplyGateTokenAdapter;
import com.redoct.ga.sup.session.adapter.ApplySessionTicketAdapter;
import com.redoct.ga.sup.session.adapter.InactiveStpSessionAdapter;
import com.redoct.ga.sup.session.adapter.QueryGateSessionAdapter;
import com.redoct.ga.sup.session.adapter.QueryStpSessionAdapter;
import com.redoct.ga.sup.session.adapter.RemoveStpSessionAdapter;
import com.redoct.ga.sup.session.adapter.VerifyGateTokenAdapter;
import com.redoct.ga.sup.session.cmd.ActiveStpSessionByTicketResp;
import com.redoct.ga.sup.session.cmd.ApplyGateTokenResp;
import com.redoct.ga.sup.session.cmd.ApplySessionTicketResp;
import com.redoct.ga.sup.session.cmd.InactiveStpSessionResp;
import com.redoct.ga.sup.session.cmd.QueryGateSessionResp;
import com.redoct.ga.sup.session.cmd.QueryStpSessionResp;
import com.redoct.ga.sup.session.cmd.RemoveStpSessionResp;
import com.redoct.ga.sup.session.cmd.VerifyGateTokenResp;

public class SupSessionCommandParser
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
		case SupCommandTag.APPLY_GATE_TOKEN_REQ:
			return new ApplyGateTokenAdapter().decode(tlv);
		case SupCommandTag.APPLY_GATE_TOKEN_RESP:
			return new ApplyGateTokenResp().decode(tlv);
		case SupCommandTag.VERIFY_GATE_TOKEN_REQ:
			return new VerifyGateTokenAdapter().decode(tlv);
		case SupCommandTag.VERIFY_GATE_TOKEN_RESP:
			return new VerifyGateTokenResp().decode(tlv);
		case SupCommandTag.APPLY_SESSION_TICKET_REQ:
			return new ApplySessionTicketAdapter().decode(tlv);
		case SupCommandTag.APPLY_SESSION_TICKET_RESP:
			return new ApplySessionTicketResp().decode(tlv);
		case SupCommandTag.QUERY_STP_SESSION_REQ:
			return new QueryStpSessionAdapter().decode(tlv);
		case SupCommandTag.QUERY_STP_SESSION_RESP:
			return new QueryStpSessionResp().decode(tlv);
		case SupCommandTag.REMOVE_STP_SESSION_REQ:
			return new RemoveStpSessionAdapter().decode(tlv);
		case SupCommandTag.REMOVE_STP_SESSION_RESP:
			return new RemoveStpSessionResp().decode(tlv);
		case SupCommandTag.INACTIVE_STP_SESSION_REQ:
			return new InactiveStpSessionAdapter().decode(tlv);
		case SupCommandTag.INACTIVE_STP_SESSION_RESP:
			return new InactiveStpSessionResp().decode(tlv);
		case SupCommandTag.ACTIVE_STP_SESSION_BY_TICKET_REQ:
			return new ActiveStpSessionByTicketAdapter().decode(tlv);
		case SupCommandTag.ACTIVE_STP_SESSION_BY_TICKET_RESP:
			return new ActiveStpSessionByTicketResp().decode(tlv);
		case SupCommandTag.QUERY_GATE_SESSION_REQ:
			return new QueryGateSessionAdapter().decode(tlv);
		case SupCommandTag.QUERY_GATE_SESSION_RESP:
			return new QueryGateSessionResp().decode(tlv);

		default:
			throw new UnsupportedEncodingException("This tlv pkg=[" + tlv.getTag() + "] has no implementation");
		}
	}

}
