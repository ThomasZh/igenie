package com.oct.ga.comm.cmd.group;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class QueryMemberListReq
		extends ReqCommand
{
	public QueryMemberListReq()
	{
		super();

		this.setTag(Command.QUERY_MEMBER_LIST_REQ);
	}

    @Override
    public TlvObject encode() throws UnsupportedEncodingException{
        TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
        TlvObject tChannelType = new TlvObject(2,2,TlvByteUtil.short2Byte(channelType));
        TlvObject tChannelId = new TlvObject(3, channelId);

        TlvObject tlv = new TlvObject(Command.QUERY_MEMBER_LIST_REQ);
        tlv.push(tSequence);
        tlv.push(tChannelType);
        tlv.push(tChannelId);

        return tlv;
    }

	@Override
	public QueryMemberListReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.QUERY_MEMBER_LIST_REQ + ", child=3) to command");

		TlvParser.decodeChildren(tlv, 3);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tChannelType = tlv.getChild(1);
		channelType = TlvByteUtil.byte2Short(tChannelType.getValue());
		logger.debug("channelType: " + channelType);

		TlvObject tChannelId = tlv.getChild(2);
		channelId = new String(tChannelId.getValue(), "UTF-8");
		logger.debug("channelId: " + channelId);

		return this;
	}

	private short channelType;
	private String channelId;

	public short getChannelType()
	{
		return channelType;
	}

	public void setChannelType(short channelType)
	{
		this.channelType = channelType;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryMemberListReq.class);

}
