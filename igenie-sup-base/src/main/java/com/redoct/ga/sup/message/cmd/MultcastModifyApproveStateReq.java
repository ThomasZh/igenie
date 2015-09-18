package com.redoct.ga.sup.message.cmd;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;
import com.redoct.ga.sup.SupCommandTag;
import com.redoct.ga.sup.SupReqCommand;

public class MultcastModifyApproveStateReq
		extends SupReqCommand
{
	public MultcastModifyApproveStateReq()
	{
		super();

		this.setTag(SupCommandTag.MULTCAST_MODIFY_APPLY_STATE_REQ);
	}

	public MultcastModifyApproveStateReq(String channelId, short approveState, String txt, String fromAccountId,
			String fromAccountName, String fromAccountAvatarUrl, String toAccountId)
	{
		this();

		this.setChannelId(channelId);
		this.setApproveState(approveState);
		this.setTxt(txt);
		this.setFromAccountId(fromAccountId);
		this.setFromAccountName(fromAccountName);
		this.setFromAccountAvatarUrl(fromAccountAvatarUrl);
		this.setToAccountId(toAccountId);
	}

	@Override
	public MultcastModifyApproveStateReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 8;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		this.setSequence(TlvByteUtil.byte2Long(tSequence.getValue()));
		logger.debug("sequence: " + this.getSequence());

		TlvObject tChannelId = tlv.getChild(i++);
		channelId = new String(tChannelId.getValue(), "UTF-8");
		logger.debug("channelId: " + channelId);

		TlvObject tApproveState = tlv.getChild(i++);
		approveState = TlvByteUtil.byte2Short(tApproveState.getValue());
		logger.debug("approveState: " + approveState);

		TlvObject tTxt = tlv.getChild(i++);
		txt = new String(tTxt.getValue(), "UTF-8");
		logger.debug("txt: " + txt);

		TlvObject tFromAccountId = tlv.getChild(i++);
		fromAccountId = new String(tFromAccountId.getValue(), "UTF-8");
		logger.debug("fromAccountId: " + fromAccountId);

		TlvObject tFromAccountName = tlv.getChild(i++);
		fromAccountName = new String(tFromAccountName.getValue(), "UTF-8");
		logger.debug("fromAccountName: " + fromAccountName);

		TlvObject tFromAccountAvatarUrl = tlv.getChild(i++);
		fromAccountAvatarUrl = new String(tFromAccountAvatarUrl.getValue(), "UTF-8");
		logger.debug("fromAccountAvatarUrl: " + fromAccountAvatarUrl);

		TlvObject tToAccountId = tlv.getChild(i++);
		toAccountId = new String(tToAccountId.getValue(), "UTF-8");
		logger.debug("toAccountId: " + toAccountId);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 8, TlvByteUtil.long2Byte(this.getSequence()));
		TlvObject tChannelId = new TlvObject(i++, channelId);
		TlvObject tApproveState = new TlvObject(i++, 2, TlvByteUtil.short2Byte(approveState));
		TlvObject tTxt = new TlvObject(i++, txt);
		TlvObject tFromAccountId = new TlvObject(i++, fromAccountId);
		TlvObject tFromAccountName = new TlvObject(i++, fromAccountName);
		TlvObject tFromAccountAvatarUrl = new TlvObject(i++, fromAccountAvatarUrl);
		TlvObject tToAccountId = new TlvObject(i++, toAccountId);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tChannelId);
		tlv.push(tApproveState);
		tlv.push(tTxt);
		tlv.push(tFromAccountId);
		tlv.push(tFromAccountName);
		tlv.push(tFromAccountAvatarUrl);
		tlv.push(tToAccountId);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String channelId;
	private short approveState;
	private String txt;
	private String fromAccountId;
	private String fromAccountName;
	private String fromAccountAvatarUrl;
	private String toAccountId;

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getToAccountId()
	{
		return toAccountId;
	}

	public void setToAccountId(String toAccountId)
	{
		this.toAccountId = toAccountId;
	}

	public short getApproveState()
	{
		return approveState;
	}

	public void setApproveState(short approveState)
	{
		this.approveState = approveState;
	}

	public String getTxt()
	{
		return txt;
	}

	public void setTxt(String txt)
	{
		this.txt = txt;
	}

	public String getFromAccountId()
	{
		return fromAccountId;
	}

	public void setFromAccountId(String fromAccountId)
	{
		this.fromAccountId = fromAccountId;
	}

	public String getFromAccountName()
	{
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName)
	{
		this.fromAccountName = fromAccountName;
	}

	public String getFromAccountAvatarUrl()
	{
		return fromAccountAvatarUrl;
	}

	public void setFromAccountAvatarUrl(String fromAccountAvatarUrl)
	{
		this.fromAccountAvatarUrl = fromAccountAvatarUrl;
	}

	private final static Logger logger = LoggerFactory.getLogger(MultcastModifyApproveStateReq.class);

}
