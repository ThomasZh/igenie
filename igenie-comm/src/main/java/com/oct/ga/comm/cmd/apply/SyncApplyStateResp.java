package com.oct.ga.comm.cmd.apply;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.domain.apply.GaApplyStateNotify;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class SyncApplyStateResp
		extends RespCommand
{
	public SyncApplyStateResp()
	{
		this.setTag(Command.SYNC_APPLY_STATE_RESP);
	}

	public SyncApplyStateResp(int sequence, short respState, List<GaApplyStateNotify> notifies)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(respState);
		this.setNotifies(notifies);
	}

	@Override
	public SyncApplyStateResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;

		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tResultState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tResultState.getValue()));
		logger.debug("resultState: " + this.getRespState());

		TlvObject tNotifies = tlv.getChild(i++);
		String jsonNotifies = new String(tNotifies.getValue(), "UTF-8");
		logger.debug("notifies json: " + jsonNotifies);
		Gson gson = new Gson();
		if (jsonNotifies != null && jsonNotifies.length() > 0) {
			notifies = gson.fromJson(jsonNotifies, new TypeToken<List<GaApplyStateNotify>>()
			{
			}.getType());
		}

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));

		Gson gson = new Gson();
		TlvObject tNotifies = null;
		if (notifies != null && notifies.size() > 0) {
			String jsonNotifies = gson.toJson(notifies);
			logger.debug("notifies json: " + jsonNotifies);
			tNotifies = new TlvObject(i++, jsonNotifies);
		} else {
			tNotifies = new TlvObject(i++, "");
		}

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tNotifies);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private List<GaApplyStateNotify> notifies;

	public List<GaApplyStateNotify> getNotifies()
	{
		return notifies;
	}

	public void setNotifies(List<GaApplyStateNotify> notifies)
	{
		this.notifies = notifies;
	}

	private final static Logger logger = LoggerFactory.getLogger(SyncApplyStateResp.class);

}
