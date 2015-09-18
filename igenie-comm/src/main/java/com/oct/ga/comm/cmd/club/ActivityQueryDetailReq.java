package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityQueryDetailReq
		extends ReqCommand
{
	public ActivityQueryDetailReq()
	{
		super();

		this.setTag(Command.ACTIVITY_QUERY_DETAIL_REQ);
	}

    @Override
    public TlvObject encode(){//android client use
        TlvObject tlv = new TlvObject(Command.ACTIVITY_QUERY_DETAIL_REQ);
        TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
        TlvObject tActivityId = new TlvObject(2,activityId);
        tlv.push(tSequence);
        tlv.push(tActivityId);

        return tlv;
    }

	@Override
	public ActivityQueryDetailReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.ACTIVITY_QUERY_DETAIL_REQ + ", child=2) to command");

		TlvParser.decodeChildren(tlv, 2);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tActivityId = tlv.getChild(1);
		activityId = new String(tActivityId.getValue(), "UTF-8");
		logger.debug("activityId: " + activityId);

		return this;
	}

	private String activityId;

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	private final static Logger logger = LoggerFactory.getLogger(ActivityQueryDetailReq.class);

}
