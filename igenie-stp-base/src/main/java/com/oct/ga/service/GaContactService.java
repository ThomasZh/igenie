package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.Contact;

public interface GaContactService
{
	public void add(Contact contact, int timestamp);

	public void update(Contact contact, int timestamp);

	public Contact query(String contactId);

	public Contact queryByAccountId(String myAccountId, String friendAccountId);

	public List<Contact> queryLastUpdateContacts(String accountId, int lastTryTime);
}
