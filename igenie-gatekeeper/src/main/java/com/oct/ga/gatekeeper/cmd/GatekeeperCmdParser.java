package com.oct.ga.gatekeeper.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.StpCommand;
import com.oct.ga.comm.cmd.admin.ModifySupServerStateResp;
import com.oct.ga.comm.cmd.gatekeeper.GK_ACF;
import com.oct.ga.comm.cmd.gatekeeper.QueryStpStatesResp;
import com.oct.ga.comm.parser.CommandParser;
import com.oct.ga.comm.tlv.TlvObject;
import com.redoct.ga.sup.admin.adapter.ModifySupServerStateAdapter;

public class GatekeeperCmdParser
		extends CommandParser
{
	// ///////////////////////////////////////////////////////////////////////////////////////
	// encode to send...

	public static TlvObject encode(StpCommand cmd)
			throws UnsupportedEncodingException
	{
		return cmd.encode();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// decode to handle

	public static StpCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		switch (tlv.getTag()) {
		case Command.GK_ARQ:
			return new GK_ARQ_Adapter().decode(tlv);
		case Command.GK_ACF:
			return new GK_ACF().decode(tlv);
		case Command.GK_MODIFY_STP_STATE_REQ:
			return new ModifyStpStateAdapter().decode(tlv);
		case Command.GK_QUERY_STP_STATES_REQ:
			return new QueryStpStatesAdapter().decode(tlv);
		case Command.GK_QUERY_STP_STATES_RESP:
			return new QueryStpStatesResp().decode(tlv);
		case Command.MODIFY_SUP_STATE_REQ:
			return new ModifySupServerStateAdapter().decode(tlv);
		case Command.MODIFY_SUP_STATE_RESP:
			return new ModifySupServerStateResp().decode(tlv);

		default:
			throw new UnsupportedEncodingException("This tlv pkg=[" + tlv.getTag() + "] has no implementation");
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(GatekeeperCmdParser.class);
}
