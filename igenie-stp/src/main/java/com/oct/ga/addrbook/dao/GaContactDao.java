package com.oct.ga.addrbook.dao;

import java.util.List;

import com.oct.ga.comm.domain.Contact;

public interface GaContactDao
{

	public Contact queryContactByAccountId(String myAccountId, String contactAccountID);

	public void add(Contact data);

	public void update(Contact data);

	public Contact queryContact(String contactId);

	public List<Contact> queryLastUpdateContact(String myAccountId, int timestamp);

	public void remove(String contactId);

	public void updateMyStateInFriendsContact(String accountId, String email, int timestamp);

	public void updateMyInfoInFriendsContact(String accountId, String accountName, String facePhoto, int timestamp);
}
