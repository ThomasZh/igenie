package com.oct.ga.comm.cmd.moment;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.parser.JsonParser;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvObjectExt;
import com.oct.ga.comm.tlv.TlvParser;

public class AddMomentReq
		extends ReqCommand
{
	public AddMomentReq()
	{
		super();

		this.setTag(Command.ADD_MOMENT_REQ);
	}

	public AddMomentReq(String channelId, String momentId, String desc, String json)
	{
		this();

		this.channelId = channelId;
		this.momentId = momentId;
		this.desc = desc;
		this.json = json;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		TlvObjectExt root = new TlvObjectExt(Command.ADD_MOMENT_REQ);

		TlvObject tSequence = new TlvObject(1, 4, TlvByteUtil.int2Byte(sequence));
		TlvObject tChannelId = new TlvObject(2, channelId);
		TlvObject tMomentId = new TlvObject(3, momentId);
		TlvObject tDesc = new TlvObject(4, desc);
		TlvObject tJson = new TlvObject(5, this.json);

		root.plus(tSequence).plus(tChannelId).plus(tMomentId).plus(tDesc).plus(tJson);

		return root;
	}

	@Override
	public AddMomentReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.ADD_MOMENT_REQ + ", child=5) to command");

		TlvParser.decodeChildren(tlv, 5);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tChannelId = tlv.getChild(1);
		channelId = new String(tChannelId.getValue(), "UTF-8");
		logger.debug("channelId: " + channelId);

		TlvObject tMomentId = tlv.getChild(2);
		momentId = new String(tMomentId.getValue(), "UTF-8");
		logger.debug("momentId: " + momentId);

		TlvObject tDesc = tlv.getChild(3);
		desc = new String(tDesc.getValue(), "UTF-8");
		logger.debug("desc: " + desc);

		TlvObject tJson = tlv.getChild(4);
		String json = new String(tJson.getValue(), "UTF-8");
		logger.debug("json: " + json);

		setPhotos(JsonParser.json2StringArray(json));

		return this;
	}

	private String momentId;
	private String channelId;
	private String desc;
	/**
	 * club serialized json, composed by client
	 */
	private String json;
	private String[] photos;

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getJson()
	{
		return json;
	}

	public void setJson(String json)
	{
		this.json = json;
	}

	public String getMomentId()
	{
		return momentId;
	}

	public void setMomentId(String momentId)
	{
		this.momentId = momentId;
	}

	public String[] getPhotos()
	{
		return photos;
	}

	public void setPhotos(String[] photos)
	{
		this.photos = photos;
	}

	private final static Logger logger = LoggerFactory.getLogger(AddMomentReq.class);

}
