package com.oct.ga.group.dao;

public interface GaGroupDao
{
	public void add(String groupId, String groupName, short channelType, int timestamp, String creatorId);

	public void add(String groupId, String groupName, short channelType, int timestamp, String creatorId, short depth);

	public void update(String groupId, String chatName, int timestamp);

	public String queryGroupName(String groupId);

	public short queryChannelType(String groupId);
	
	public short queryDepth(String groupId);
	
	public short queryState(String groupId);

	public void updateSate(String groupId, short state, int timestamp);

	public boolean isActive(String groupId);
	
	public void remove(String groupId);

	// //////////////////////////////////////////////////////////////////////
	// Member available summary information

	public short queryMemberAvailableNum(String groupId);

	public void updateMemberAvailableNum(String groupId, short num, int timestamp);
	
	// //////////////////////////////////////////////////////////////////////
	// Member summary information

	public int queryMemberNum(String groupId);

	public void updateMemberNum(String groupId, int num, int timestamp);

	// //////////////////////////////////////////////////////////////////////
	// Children summary information

	public int queryChildNum(String groupId);

	public void updateChildNum(String groupId, int num, int timestamp);

	// //////////////////////////////////////////////////////////////////////
	// Attachment summary information

	public int queryAttachmentNum(String groupId);

	public void updateAttachmentNum(String groupId, int num, int timestamp);

	// //////////////////////////////////////////////////////////////////////
	// Note summary information

	public int queryNoteNum(String groupId);

	public void updateNoteNum(String groupId, int num, int timestamp);

}
