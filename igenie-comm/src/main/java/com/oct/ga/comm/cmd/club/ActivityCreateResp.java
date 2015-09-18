package com.oct.ga.comm.cmd.club;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ActivityCreateResp extends RespCommand
{
	public ActivityCreateResp()
	{
		this.setTag(Command.ACTIVITY_CREATE_RESP);
	}

	public ActivityCreateResp(short respState, String clubActivityId)
	{
		this();

		this.setRespState(respState);
		this.clubActivityId = clubActivityId;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tResultFlag = new TlvObject(2, 2, TlvByteUtil.short2Byte(this.getRespState()));
		TlvObject tClubActivityId = new TlvObject(3, clubActivityId);

		TlvObject tlv = new TlvObject(Command.ACTIVITY_CREATE_RESP);
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tClubActivityId);

		logger.info("from command to tlv package:(tag=" + Command.ACTIVITY_CREATE_RESP + ", child=3, length="
				+ tlv.getLength() + ")");

		return tlv;
	}

    @Override
    public ActivityCreateResp decode(TlvObject tlv) throws UnsupportedEncodingException{
        TlvParser.decodeChildren(tlv, 3);

        TlvObject tSequence = tlv.getChild(0);
        this.setSequence(TlvByteUtil.byte2Int(tSequence.getValue()));
        TlvObject tResultFlag = tlv.getChild(1);
        this.setRespState(TlvByteUtil.byte2Short(tResultFlag.getValue()));
        TlvObject tClubActivityId = tlv.getChild(2);
        this.setClubActivityId(new String(tClubActivityId.getValue(), "UTF-8"));

        return this;
    }


    public String getClubActivityId() {
        return clubActivityId;
    }

    public void setClubActivityId(String clubActivityId) {
        this.clubActivityId = clubActivityId;
    }

    private String clubActivityId;

	private final static Logger logger = LoggerFactory.getLogger(ActivityCreateResp.class);

}
