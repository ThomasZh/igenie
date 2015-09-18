package com.redoct.ga.sup.account.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class CreateAccountReq
		extends SupReqCommand
{
	public CreateAccountReq()
	{
		super();

		this.setTag(SupCommandTag.CREATE_ACCOUNT_REQ);
	}

	public CreateAccountReq(String nickname, String avatarUrl, String desc)
	{
		this();

		this.setNickname(nickname);
		this.setAvatarUrl(avatarUrl);
		this.setDesc(desc);
	}

	public CreateAccountReq(long sequence, String nickname, String avatarUrl, String desc)
	{
		this(nickname, avatarUrl, desc);

		this.setSequence(sequence);
	}

	@Override
	public CreateAccountReq decode(TlvObject tlv)
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

		TlvObject tNickname = tlv.getChild(i++);
		nickname = new String(tNickname.getValue(), "UTF-8");
		logger.debug("nickname: " + nickname);

		TlvObject tAvatarUrl = tlv.getChild(i++);
		avatarUrl = new String(tAvatarUrl.getValue(), "UTF-8");
		logger.debug("avatarUrl: " + avatarUrl);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tNickname = new TlvObject(i++, nickname);
		TlvObject tAvatarUrl = new TlvObject(i++, avatarUrl);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tNickname);
		tlv.push(tAvatarUrl);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String nickname;
	private String avatarUrl;
	private String desc;

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getAvatarUrl()
	{
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl)
	{
		this.avatarUrl = avatarUrl;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	private final static Logger logger = LoggerFactory.getLogger(CreateAccountReq.class);

}
