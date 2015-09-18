package com.oct.ga.comm.cmd.addrbook;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.cmd.Command;
import com.oct.ga.comm.cmd.ReqCommand;
import com.oct.ga.comm.domain.Contact;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.tlv.TlvByteUtil;
import com.oct.ga.comm.tlv.TlvObject;
import com.oct.ga.comm.tlv.TlvParser;

public class UploadContactReq
		extends ReqCommand
{
	public UploadContactReq()
	{
		super();

		this.setTag(Command.UPLOAD_CONTACT_REQ);
	}

	public UploadContactReq(Contact friend)
	{
		this();

		this.setContact(friend);
	}

	@Override
	public UploadContactReq decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		logger.info("from tlv:(tag=" + Command.UPLOAD_CONTACT_REQ + ", child=11) to command");

		TlvParser.decodeChildren(tlv, 11);

		TlvObject tSequence = tlv.getChild(0);
		sequence = TlvByteUtil.byte2Int(tSequence.getValue());
		logger.debug("sequence: " + sequence);

		// contactId
		TlvObject tContactId = tlv.getChild(1);
		String contactId = new String(tContactId.getValue(), "UTF-8");
		logger.debug("contactId: " + contactId);

		// nickname
		TlvObject tNickname = tlv.getChild(2);
		String nickname = new String(tNickname.getValue(), "UTF-8");
		logger.debug("nickname: " + nickname);

		// email
		TlvObject tEmail = tlv.getChild(3);
		String email = new String(tEmail.getValue(), "UTF-8");
		logger.debug("email: " + email);

		// telephone
		TlvObject tTelephone = tlv.getChild(4);
		String telephone = new String(tTelephone.getValue(), "UTF-8");
		logger.debug("telephone: " + telephone);

		// facePhoto
		TlvObject tFacePhoto = tlv.getChild(5);
		String facePhoto = new String(tFacePhoto.getValue(), "UTF-8");
		logger.debug("facePhoto: " + facePhoto);

		// myAccountId
		TlvObject tMyAccountId = tlv.getChild(6);
		String myAccountId = new String(tMyAccountId.getValue(), "UTF-8");
		logger.debug("myAccountId: " + myAccountId);

		// state
		TlvObject tState = tlv.getChild(7);
		short state = TlvByteUtil.byte2Short(tState.getValue());
		logger.debug("state: " + state);

		// createTime
		TlvObject tCreateTime = tlv.getChild(8);
		int createTime = TlvByteUtil.byte2Int(tCreateTime.getValue());
		logger.debug("createTime: " + createTime);

		// updateTime
		TlvObject tUpdateTime = tlv.getChild(9);
		int updateTime = TlvByteUtil.byte2Int(tUpdateTime.getValue());
		logger.debug("updateTime: " + updateTime);

		// myAccountId
		TlvObject tAccountId = tlv.getChild(10);
		String accountId = new String(tAccountId.getValue(), "UTF-8");
		logger.debug("myAccountId: " + accountId);

		Contact friend = new Contact();
		friend.setContactId(contactId);
		friend.setAccountId(accountId);
		friend.setEmail(email);
		friend.setFacePhoto(facePhoto);
		friend.setMyAccountID(myAccountId);
		friend.setNickname(nickname);
		friend.setState(state);
		friend.setTelephone(telephone);

		this.setContact(friend);

		return this;
	}

	private boolean isEmpty(String target)
	{
		if (target == null)
			return true;
		if (target.length() == 0)
			return true;

		return false;
	}

	private boolean isValid(AccountMaster account)
	{
		if (account == null)
			return false;
		if (account.getAccountId() == null)
			return false;

		return true;
	}

	private Contact contact;

	public Contact getContact()
	{
		return contact;
	}

	public void setContact(Contact friend)
	{
		this.contact = friend;
	}

	private final static Logger logger = LoggerFactory.getLogger(UploadContactReq.class);

}
