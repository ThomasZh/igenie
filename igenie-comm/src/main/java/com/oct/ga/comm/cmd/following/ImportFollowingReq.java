package com.oct.ga.comm.cmd.following;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ImportFollowingReq
		extends ReqCommand
{
	public ImportFollowingReq()
	{
		super();

		this.setTag(Command.IMPORT_FOLLOWING_REQ);
	}

	public ImportFollowingReq(String accountId)
	{
		this();

		this.accountId = accountId;
	}

	@Override
	public ImportFollowingReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.IMPORT_FOLLOWING_REQ + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		// accountId
		TlvObject tAccountId = tlv.getChild(1);
		accountId = new String(tAccountId.getValue(), "UTF-8");
		logger.debug("accountId: " + accountId);

		return this;
	}

	private String accountId;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ImportFollowingReq.class);

}
