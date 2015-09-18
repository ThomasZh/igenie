package com.redoct.ga.sup.message;

import java.io.UnsupportedEncodingException;

import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.SupCommand;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.message.adapter.MultcastActivityJoinAdapter;
import com.redoct.ga.sup.message.adapter.MultcastApplyStateAdapter;
import com.redoct.ga.sup.message.adapter.MultcastInviteAdapter;
import com.redoct.ga.sup.message.adapter.MultcastInviteFeedbackAdapter;
import com.redoct.ga.sup.message.adapter.MultcastMessageAdapter;
import com.redoct.ga.sup.message.adapter.MultcastModifyApproveStateAdapter;
import com.redoct.ga.sup.message.adapter.MultcastMsgFlowAdapter;
import com.redoct.ga.sup.message.cmd.MultcastActivityJoinResp;
import com.redoct.ga.sup.message.cmd.MultcastApplyStateResp;
import com.redoct.ga.sup.message.cmd.MultcastInviteFeedbackResp;
import com.redoct.ga.sup.message.cmd.MultcastInviteResp;
import com.redoct.ga.sup.message.cmd.MultcastMessageResp;
import com.redoct.ga.sup.message.cmd.MultcastModifyApproveStateResp;
import com.redoct.ga.sup.message.cmd.MultcastTaskLogResp;

public class SupMsgCommandParser
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
		case SupCommandTag.MULTCAST_MESSAGE_REQ:
			return new MultcastMessageAdapter().decode(tlv);
		case SupCommandTag.MULTCAST_MESSAGE_RESP:
			return new MultcastMessageResp().decode(tlv);
		case SupCommandTag.MULTCAST_INVITE_REQ:
			return new MultcastInviteAdapter().decode(tlv);
		case SupCommandTag.MULTCAST_INVITE_RESP:
			return new MultcastInviteResp().decode(tlv);
		case SupCommandTag.MULTCAST_INVITE_FEEDBACK_REQ:
			return new MultcastInviteFeedbackAdapter().decode(tlv);
		case SupCommandTag.MULTCAST_INVITE_FEEDBACK_RESP:
			return new MultcastInviteFeedbackResp().decode(tlv);
		case SupCommandTag.MULTCAST_MODIFY_APPLY_STATE_REQ:
			return new MultcastModifyApproveStateAdapter().decode(tlv);
		case SupCommandTag.MULTCAST_MODIFY_APPLY_STATE_RESP:
			return new MultcastModifyApproveStateResp().decode(tlv);
		case SupCommandTag.MULTCAST_APPLY_STATE_REQ:
			return new MultcastApplyStateAdapter().decode(tlv);
		case SupCommandTag.MULTCAST_APPLY_STATE_RESP:
			return new MultcastApplyStateResp().decode(tlv);
		case SupCommandTag.MULTCAST_ACTIVITY_JOIN_REQ:
			return new MultcastActivityJoinAdapter().decode(tlv);
		case SupCommandTag.MULTCAST_ACTIVITY_JOIN_RESP:
			return new MultcastActivityJoinResp().decode(tlv);
		case SupCommandTag.MULTCAST_TASK_LOG_REQ:
			return new MultcastMsgFlowAdapter().decode(tlv);
		case SupCommandTag.MULTCAST_TASK_LOG_RESP:
			return new MultcastTaskLogResp().decode(tlv);

		default:
			throw new UnsupportedEncodingException("This tlv pkg=[" + tlv.getTag() + "] has no implementation");
		}
	}

}
