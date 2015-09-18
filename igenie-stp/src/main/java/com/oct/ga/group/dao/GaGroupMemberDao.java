package com.oct.ga.group.dao;

import java.util.List;

import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.group.GroupMemberMasterInfo;

public interface GaGroupMemberDao
{
	public void add(String groupId, String userId, short rank, short state, int timestamp);

	public void remove(String groupId, String userId);

	public boolean isExist(String groupId, String userId);

	public boolean isMember(String groupId, String userId);

	public boolean isLeader(String groupId, String userId);

	public short queryMemberRank(String groupId, String userId);

	public short queryMemberState(String groupId, String userId);

	public String queryLeaderId(String groupId);

	public List<String> queryMemberIds(String groupId);

	public List<String> queryActiveMemberIds(String groupId);

	public List<GroupMemberDetailInfo> queryMembers(String groupId);

	public List<GroupMemberDetailInfo> queryLastChangedMembers(String groupId, int lastTryTime);

	public List<GroupMemberMasterInfo> queryLastChangedMembersMasterInfo(String groupId, int lastTryTime);

	public GroupMemberDetailInfo queryMember(String groupId, String userId);

	public GroupMemberDetailInfo queryLeader(String groupId);

	public void updateState(String groupId, String userId, short state, int timestamp);

	public void updateRank(String groupId, String userId, short rank, int timestamp);

	public short countMemberAvailableNum(String groupId);

	public short countMemberNum(String groupId);

	// //////////////////////////////////////////////////////////////////////
	// Do Not Disturb

	public void updateDndMode(String groupId, String accountId, short mode, int timestamp);

	public short selectDndMode(String groupId, String accountId);
}
