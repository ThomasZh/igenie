package com.oct.ga.comm.cmd.apply;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class ModifyApproveStateReq
		extends ReqCommand
{
	public ModifyApproveStateReq()
	{
		super();

		this.setTag(Command.MODIFY_APPROVE_STATE_REQ);
	}

	public ModifyApproveStateReq(int sequence, String activityId, String accountId, short approveState, String txt)
	{
		this();

		this.setSequence(sequence);
		this.setActivityId(activityId);
		this.setAccountId(accountId);
		this.setApproveState(approveState);
		this.setTxt(txt);
	}

	@Override
	public ModifyApproveStateReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		this.setTag(tlv.getTag());

		int childCount = 5;
		logger.debug("from tlv:(tag=" + this.getTag() + ", child=" + childCount + ") to command");
		TlvParser.decodeChildren(tlv, childCount);

		int i = 0;
		TlvObject tSequence = tlv.getChild(i++);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		TlvObject tActivityId = tlv.getChild(i++);
		activityId = new String(tActivityId.getValue(), "UTF-8");
		logger.debug("activityId: " + activityId);

		TlvObject tAccountId = tlv.getChild(i++);
		accountId = new String(tAccountId.getValue(), "UTF-8");
		logger.debug("accountId: " + accountId);

		TlvObject tApproveState = tlv.getChild(i++);
		approveState = TlvByteUtil.byte2Short(tApproveState.getValue());
		logger.debug("approveState: " + approveState);

		TlvObject tTxt = tlv.getChild(i++);
		txt = new String(tTxt.getValue(), "UTF-8");
		logger.debug("txt: " + txt);

		return this;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		int i = 0;

		TlvObject tSequence = new TlvObject(i++, 4, TlvByteUtil.int2Byte(this.getSequence()));
		TlvObject tActivityId = new TlvObject(i++, activityId);
		TlvObject tAccountId = new TlvObject(i++, accountId);
		TlvObject tApproveState = new TlvObject(i++, 2, TlvByteUtil.short2Byte(approveState));
		TlvObject tTxt = new TlvObject(i++, txt);

		TlvObject tlv = new TlvObject(this.getTag());
		tlv.push(tSequence);
		tlv.push(tActivityId);
		tlv.push(tAccountId);
		tlv.push(tApproveState);
		tlv.push(tTxt);

		logger.debug("from command to tlv package:(tag=" + this.getTag() + ", child=" + i + ", length="
				+ tlv.getLength() + ")");

		return tlv;
	}

	private String activityId;
	private String accountId;
	private short approveState;
	private String txt;

	public String getActivityId()
	{
		return activityId;
	}

	public void setActivityId(String activityId)
	{
		this.activityId = activityId;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
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

	private final static Logger logger = LoggerFactory.getLogger(ModifyApproveStateReq.class);

}
