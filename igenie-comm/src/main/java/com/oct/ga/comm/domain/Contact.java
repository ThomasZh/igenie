package com.oct.ga.comm.domain;

import java.io.Serializable;

import com.oct.ga.comm.GlobalArgs;

public class Contact
		implements Serializable
{
	public Contact()
	{
		this.setState(GlobalArgs.CONTACT_STATE_NOT_FRIEND);
	}

	public Contact(String friendId, String nickname, String email, String telephone, String facePhote,
			String myAccountId, String accountId)
	{
		this.setContactId(friendId);
		this.setNickname(nickname);
		this.setEmail(email);
		this.setTelephone(telephone);
		this.setFacePhoto(facePhote);
		this.setMyAccountID(myAccountId);
		this.setAccountId(accountId);
		this.setState(GlobalArgs.CONTACT_STATE_NOT_FRIEND);
	}

	private String contactId;
	private String nickname;
	private String firstname;
	private String email; // toLowerCase
	private String telephone;
	private String facePhoto;
	private String myAccountID;
	private String accountId;
	private short state;

	public String getContactId()
	{
		return contactId;
	}

	public void setContactId(String FriendID)
	{
		this.contactId = FriendID;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String Nickame)
	{
		this.nickname = Nickame;
	}

	public String getEmail()
	{
		return email.toLowerCase();
	}

	public void setEmail(String email)
	{
		if (email != null)
			this.email = email.toLowerCase();
	}

	public String getTelephone()
	{
		return telephone;
	}

	public void setTelephone(String Telephone)
	{
		this.telephone = Telephone;
	}

	public String getFacePhoto()
	{
		return facePhoto;
	}

	public void setFacePhoto(String facePhoto)
	{
		this.facePhoto = facePhoto;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short State)
	{
		this.state = State;
	}

	public String getMyAccountID()
	{
		return myAccountID;
	}

	public void setMyAccountID(String MyAccountID)
	{
		this.myAccountID = MyAccountID;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String AccountID)
	{
		this.accountId = AccountID;
	}

	private static final long serialVersionUID = 8844768709816457637L;

}
