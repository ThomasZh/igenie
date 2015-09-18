package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupRespCommand;

public class QueryAllAccountBasicResp
		extends SupRespCommand
{
	public QueryAllAccountBasicResp()
	{
		super();

		this.setTag(SupCommandTag.QUERY_ALL_ACCOUNT_BASIC_RESP);
	}

	public QueryAllAccountBasicResp(long sequence, short state)
	{
		this();

		this.setSequence(sequence);
		this.setRespState(state);
	}

	public QueryAllAccountBasicResp(long sequence, short state, List<AccountBasic> array)
	{
		this(sequence, state);

		this.setArray(array);
	}

	@Override
	public QueryAllAccountBasicResp decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 3;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tState = tlv.getChild(i++);
		this.setRespState(TlvByteUtil.byte2Short(tState.getValue()));
		logger.debug("respState: " + this.getRespState());

		TlvObject tJson = tlv.getChild(i++);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);
		if (json != null) {
			Gson gson = new Gson();
			array = gson.fromJson(json, new TypeToken<List<AccountMaster>>()
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

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tResultFlag = new TlvObject(i++, 2, TlvByteUtil.short2Byte(this.getRespState()));
		Gson gson = new Gson();
		String json = gson.toJson(array);
		TlvObject tJson = new TlvObject(i++, json);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tResultFlag);
		tlv.push(tJson);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private List<AccountBasic> array;

	public List<AccountBasic> getArray()
	{
		return array;
	}

	public void setArray(List<AccountBasic> array)
	{
		this.array = array;
	}

	private final static Logger logger = LoggerFactory.getLogger(QueryAllAccountBasicResp.class);

}
