package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.group.GroupMemberMasterInfo;

public interface GaGroupService
{
	public void createGroup(String groupId, String groupName, short channelType, int timestamp, String creatorId);

	public void createGroup(String groupId, String groupName, short channelType, int timestamp, String creatorId,
			short depth);

	public void modifyGroupName(String groupId, String groupName, int timestamp);

	public String queryGroupName(String groupId);

	public short queryChannelType(String groupId);

	public short queryDepth(String groupId);

	public short queryState(String groupId);

	public boolean isActive(String groupId);

	public void updateSate(String groupId, short state, int timestamp);

	// //////////////////////////////////////////////////////////////////////

	public void joinAsMember(String groupId, String userId, int timestamp);

	public void modifyMemberState(String groupId, String userId, short state, int timestamp);

	public void joinAsLeader(String groupId, String userId, int timestamp);

	public void applyWaitJoin(String groupId, String userId, int timestamp, String leaderId);

	public void acceptToJoin(String groupId, String userId, int timestamp);

	public void rejectToJoin(String groupId, String userId, int timestamp);

	public void quit(String groupId, String userId, int timestamp);

	public void kickout(String groupId, String userId, int timestamp, String leaderId);

	public boolean isMember(String groupId, String userId);

	public boolean isLeader(String groupId, String userId);

	/**
	 * @return id,name,rank,state
	 * @throws SupSocketException 
	 */
	public GroupMemberDetailInfo queryMember(String groupId, String userId) throws SupSocketException;

	/**
	 * @return id,name,rank,state
	 * @throws SupSocketException 
	 */
	public List<GroupMemberDetailInfo> queryMembers(String groupId) throws SupSocketException;

	/**
	 * @return id,name,rank,state
	 * @throws SupSocketException 
	 */
	public List<GroupMemberDetailInfo> queryLastChangedMembers(String groupId, int lastTryTime) throws SupSocketException;

	/**
	 * @return id,name,rank,state
	 * @throws SupSocketException 
	 */
	public List<GroupMemberMasterInfo> queryLastChangedMembersMasterInfo(String groupId, int lastTryTime) throws SupSocketException;

	public List<String> queryMemberIds(String groupId);

	public List<String> queryActiveMemberIds(String groupId);

	/**
	 * one club has only one leader
	 */
	public String queryLeaderId(String groupId);

	/**
	 * one club has only one leader
	 * 
	 * @return id,name,rank,state
	 * @throws SupSocketException
	 */
	public GroupMemberDetailInfo queryLeader(String groupId)
			throws SupSocketException;

	/**
	 * promote member to leader
	 * 
	 * @param groupId
	 * @param userId
	 * @param timestamp
	 */
	public void promoteToLeader(String groupId, String userId, int timestamp);

	public short queryMemberRank(String groupId, String userId);

	public short queryMemberState(String groupId, String userId);

	// //////////////////////////////////////////////////////////////////////
	// Member available summary information

	public short queryMemberAvailableNum(String groupId);

	public void modifyMemberAvailableNum(String groupId, short num, int timestamp);

	public void recountMemberAvailableNum(String groupId, int timestamp);

	// //////////////////////////////////////////////////////////////////////
	// Member summary information

	public int queryMemberNum(String groupId);

	public void modifyMemberNum(String groupId, int num, int timestamp);

	public void recountMemberNum(String groupId, int timestamp);

	// //////////////////////////////////////////////////////////////////////
	// Children summary information

	public int queryChildNum(String groupId);

	public void modifyChildNum(String groupId, int num, int timestamp);

	// //////////////////////////////////////////////////////////////////////
	// Attachment summary information

	public int queryAttachmentNum(String groupId);

	public void modifyAttachmentNum(String groupId, int num, int timestamp);

	// //////////////////////////////////////////////////////////////////////
	// Note summary information

	public int queryNoteNum(String groupId);

	public void modifyNoteNum(String groupId, int num, int timestamp);

	// //////////////////////////////////////////////////////////////////////
	// Do Not Disturb

	public void setDndMode(String groupId, String accountId, short mode, int timestamp);

	public short queryDndMode(String groupId, String accountId);

}
