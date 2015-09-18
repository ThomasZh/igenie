package com.oct.ga.addrbook;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.addrbook.dao.GaContactDao;
import com.oct.ga.comm.domain.Contact;
import com.oct.ga.service.GaContactService;

public class ContactServiceImpl
		implements GaContactService
{
	@Override
	public void add(Contact contact, int timestamp)
	{
		this.contactDao.add(contact);
	}

	@Override
	public void update(Contact contact, int timestamp)
	{
		this.contactDao.update(contact);
	}

	@Override
	public Contact query(String contactId)
	{
		return this.contactDao.queryContact(contactId);
	}

	@Override
	public Contact queryByAccountId(String myAccountId, String friendAccountId)
	{
		return this.contactDao.queryContactByAccountId(myAccountId, friendAccountId);
	}

	@Override
	public List<Contact> queryLastUpdateContacts(String accountId, int lastTryTime)
	{
		return this.contactDao.queryLastUpdateContact(accountId, lastTryTime);
	}

	private void updateMyStateInFriendsContact(String accountId, String email, int timestamp)
	{
		this.contactDao.updateMyStateInFriendsContact(accountId, email, timestamp);
	}

	private void updateMyInfoInFriendsContact(String accountId, String accountName, String facePhoto, int timestamp)
	{
		contactDao.updateMyInfoInFriendsContact(accountId, accountName, facePhoto, timestamp);
	}

	// ********** end of api **************

	private GaContactDao contactDao;

	public GaContactDao getContactDao()
	{
		return contactDao;
	}

	public void setContactDao(GaContactDao contactDao)
	{
		this.contactDao = contactDao;
	}

	private final static Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

}
