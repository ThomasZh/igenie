package com.oct.ga.group;

import java.util.List;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.group.GroupMemberDetailInfo;
import com.oct.ga.comm.domain.group.GroupMemberMasterInfo;
import com.oct.ga.group.dao.GaGroupDao;
import com.oct.ga.group.dao.GaGroupMemberDao;
import com.oct.ga.service.GaGroupService;
import com.redoct.ga.sup.account.SupAccountService;

public class GroupServiceImpl
		implements GaGroupService
{
	@Override
	public void createGroup(String groupId, String groupName, short channelType, int timestamp, String creatorId)
	{
		groupDao.add(groupId, groupName, channelType, timestamp, creatorId);
	}

	@Override
	public void createGroup(String groupId, String groupName, short channelType, int timestamp, String creatorId,
			short depth)
	{
		groupDao.add(groupId, groupName, channelType, timestamp, creatorId, depth);
	}

	@Override
	public void modifyGroupName(String groupId, String groupName, int timestamp)
	{
		groupDao.update(groupId, groupName, timestamp);
	}

	public short queryChannelType(String groupId)
	{
		return groupDao.queryChannelType(groupId);
	}

	public short queryDepth(String groupId)
	{
		return groupDao.queryDepth(groupId);
	}

	@Override
	public short queryState(String groupId)
	{
		return groupDao.queryState(groupId);
	}

	@Override
	public String queryGroupName(String groupId)
	{
		return groupDao.queryGroupName(groupId);
	}

	@Override
	public boolean isActive(String groupId)
	{
		return groupDao.isActive(groupId);
	}

	@Override
	public void updateSate(String groupId, short state, int timestamp)
	{
		groupDao.updateSate(groupId, state, timestamp);
	}

	// //////////////////////////////////////////////////////////////////////

	@Override
	public void joinAsMember(String groupId, String userId, int timestamp)
	{
		if (groupMemberDao.isMember(groupId, userId)) {
			return;
		} else {
			if (groupMemberDao.isExist(groupId, userId)) {
				groupMemberDao.updateState(groupId, userId, GlobalArgs.INVITE_STATE_ACCPET, timestamp);
			} else {
				groupMemberDao.add(groupId, userId, GlobalArgs.MEMBER_RANK_NORMAL, GlobalArgs.INVITE_STATE_ACCPET,
						timestamp);
			}
		}

		// Logic: recount member numbers
		this.recountMemberNum(groupId, timestamp);
		this.recountMemberAvailableNum(groupId, timestamp);
	}

	@Override
	public void joinAsLeader(String groupId, String userId, int timestamp)
	{
		if (groupMemberDao.isMember(groupId, userId)) {
			return;
		} else {
			if (groupMemberDao.isExist(groupId, userId)) {
				groupMemberDao.updateState(groupId, userId, GlobalArgs.INVITE_STATE_ACCPET, timestamp);
			} else {
				groupMemberDao.add(groupId, userId, GlobalArgs.MEMBER_RANK_LEADER, GlobalArgs.INVITE_STATE_ACCPET,
						timestamp);
			}
		}

		// Logic: recount member numbers
		this.recountMemberNum(groupId, timestamp);
		this.recountMemberAvailableNum(groupId, timestamp);
	}

	@Override
	public void applyWaitJoin(String groupId, String userId, int timestamp, String leaderId)
	{
		if (groupMemberDao.isMember(groupId, userId)) {
			return;
		} else {
			if (groupMemberDao.isExist(groupId, userId)) {
				groupMemberDao.updateState(groupId, userId, GlobalArgs.INVITE_STATE_APPLY, timestamp);
			} else {
				groupMemberDao.add(groupId, userId, GlobalArgs.MEMBER_RANK_NORMAL, GlobalArgs.INVITE_STATE_APPLY,
						timestamp);
			}
		}

		// Logic: recount member numbers
		this.recountMemberNum(groupId, timestamp);
		this.recountMemberAvailableNum(groupId, timestamp);
	}

	@Override
	public void acceptToJoin(String groupId, String userId, int timestamp)
	{
		if (groupMemberDao.isMember(groupId, userId)) {
			return;
		} else {
			if (groupMemberDao.isExist(groupId, userId)) {
				groupMemberDao.updateState(groupId, userId, GlobalArgs.INVITE_STATE_ACCPET, timestamp);
			} else {
				groupMemberDao.add(groupId, userId, GlobalArgs.MEMBER_RANK_NORMAL, GlobalArgs.INVITE_STATE_ACCPET,
						timestamp);
			}
		}

		// Logic: recount member numbers
		this.recountMemberNum(groupId, timestamp);
		this.recountMemberAvailableNum(groupId, timestamp);
	}

	@Override
	public void rejectToJoin(String groupId, String userId, int timestamp)
	{
		if (groupMemberDao.isExist(groupId, userId)) {
			groupMemberDao.updateState(groupId, userId, GlobalArgs.INVITE_STATE_REJECT, timestamp);
		} else {
			return;
		}

		// Logic: recount member numbers
		this.recountMemberNum(groupId, timestamp);
		this.recountMemberAvailableNum(groupId, timestamp);
	}

	@Override
	public void quit(String groupId, String userId, int timestamp)
	{
		if (groupMemberDao.isExist(groupId, userId)) {
			// groupMemberDao.remove(groupId, userId);
			groupMemberDao.updateState(groupId, userId, GlobalArgs.INVITE_STATE_QUIT, timestamp);
		} else {
			return;
		}

		// Logic: recount member numbers
		this.recountMemberNum(groupId, timestamp);
		this.recountMemberAvailableNum(groupId, timestamp);
	}

	@Override
	public void kickout(String groupId, String userId, int timestamp, String leaderId)
	{
		if (groupMemberDao.isExist(groupId, userId)) {
			// groupMemberDao.remove(groupId, userId);
			groupMemberDao.updateState(groupId, userId, GlobalArgs.INVITE_STATE_KICKOFF, timestamp);
		} else {
			return;
		}

		// Logic: recount member numbers
		this.recountMemberNum(groupId, timestamp);
		this.recountMemberAvailableNum(groupId, timestamp);
	}

	@Override
	public boolean isMember(String groupId, String userId)
	{
		return groupMemberDao.isMember(groupId, userId);
	}

	@Override
	public boolean isLeader(String groupId, String userId)
	{
		return groupMemberDao.isLeader(groupId, userId);
	}

	@Override
	public GroupMemberDetailInfo queryMember(String groupId, String userId)
			throws SupSocketException
	{
		AccountBasic account = accountService.queryAccount(userId);
		GroupMemberDetailInfo member = groupMemberDao.queryMember(groupId, userId);
		member.setNickname(account.getNickname());
		return member;
	}

	@Override
	public List<GroupMemberDetailInfo> queryMembers(String groupId)
			throws SupSocketException
	{
		List<GroupMemberDetailInfo> members = groupMemberDao.queryMembers(groupId);
		for (GroupMemberDetailInfo member : members) {
			AccountBasic account = accountService.queryAccount(member.getAccountId());
			member.setNickname(account.getNickname());
		}
		return members;
	}

	@Override
	public List<GroupMemberDetailInfo> queryLastChangedMembers(String groupId, int lastTryTime)
			throws SupSocketException
	{
		List<GroupMemberDetailInfo> members = groupMemberDao.queryLastChangedMembers(groupId, lastTryTime);
		for (GroupMemberDetailInfo member : members) {
			AccountBasic account = accountService.queryAccount(member.getAccountId());
			member.setNickname(account.getNickname());
		}
		return members;
	}

	/**
	 * @return id,name,rank,state
	 * @throws SupSocketException
	 */
	public List<GroupMemberMasterInfo> queryLastChangedMembersMasterInfo(String groupId, int lastTryTime)
			throws SupSocketException
	{
		List<GroupMemberMasterInfo> members = groupMemberDao.queryLastChangedMembersMasterInfo(groupId, lastTryTime);
		for (GroupMemberMasterInfo member : members) {
			AccountBasic account = accountService.queryAccount(member.getAccountId());
			member.setNickname(account.getNickname());
		}
		return members;
	}

	@Override
	public List<String> queryMemberIds(String groupId)
	{
		return groupMemberDao.queryMemberIds(groupId);
	}

	@Override
	public List<String> queryActiveMemberIds(String groupId)
	{
		return groupMemberDao.queryActiveMemberIds(groupId);
	}

	@Override
	public String queryLeaderId(String groupId)
	{
		return groupMemberDao.queryLeaderId(groupId);
	}

	@Override
	public GroupMemberDetailInfo queryLeader(String groupId)
			throws SupSocketException
	{
		GroupMemberDetailInfo leader = groupMemberDao.queryLeader(groupId);
		AccountBasic account = accountService.queryAccount(leader.getAccountId());
		leader.setNickname(account.getNickname());
		leader.setAvatarUrl(account.getAvatarUrl());
		return leader;
	}

	@Override
	public void promoteToLeader(String groupId, String userId, int timestamp)
	{
		groupMemberDao.updateRank(groupId, userId, GlobalArgs.MEMBER_RANK_LEADER, timestamp);
	}

	@Override
	public short queryMemberRank(String groupId, String userId)
	{
		return groupMemberDao.queryMemberRank(groupId, userId);
	}

	@Override
	public void modifyMemberState(String groupId, String userId, short state, int timestamp)
	{
		groupMemberDao.updateState(groupId, userId, state, timestamp);
	}

	@Override
	public short queryMemberState(String groupId, String userId)
	{
		return groupMemberDao.queryMemberState(groupId, userId);
	}

	// //////////////////////////////////////////////////////////////////////
	// Group member summary information

	@Override
	public short queryMemberAvailableNum(String groupId)
	{
		return groupDao.queryMemberAvailableNum(groupId);
	}

	@Override
	public void modifyMemberAvailableNum(String groupId, short num, int timestamp)
	{
		groupDao.updateMemberAvailableNum(groupId, num, timestamp);
	}

	@Override
	public int queryMemberNum(String groupId)
	{
		return groupDao.queryMemberNum(groupId);
	}

	@Override
	public void modifyMemberNum(String groupId, int num, int timestamp)
	{
		groupDao.updateMemberNum(groupId, num, timestamp);
	}

	@Override
	public void recountMemberAvailableNum(String groupId, int timestamp)
	{
		short num = groupMemberDao.countMemberAvailableNum(groupId);
		groupDao.updateMemberAvailableNum(groupId, num, timestamp);
	}

	@Override
	public void recountMemberNum(String groupId, int timestamp)
	{
		int num = groupMemberDao.countMemberNum(groupId);
		groupDao.updateMemberNum(groupId, num, timestamp);
	}

	// //////////////////////////////////////////////////////////////////////
	// Children summary information

	@Override
	public int queryChildNum(String groupId)
	{
		return groupDao.queryChildNum(groupId);
	}

	@Override
	public void modifyChildNum(String groupId, int num, int timestamp)
	{
		groupDao.updateChildNum(groupId, num, timestamp);
	}

	// //////////////////////////////////////////////////////////////////////
	// Attachment summary information

	@Override
	public int queryAttachmentNum(String groupId)
	{
		return groupDao.queryAttachmentNum(groupId);
	}

	@Override
	public void modifyAttachmentNum(String groupId, int num, int timestamp)
	{
		groupDao.updateAttachmentNum(groupId, num, timestamp);
	}

	// //////////////////////////////////////////////////////////////////////
	// Note summary information

	@Override
	public int queryNoteNum(String groupId)
	{
		return groupDao.queryNoteNum(groupId);
	}

	@Override
	public void modifyNoteNum(String groupId, int num, int timestamp)
	{
		groupDao.updateNoteNum(groupId, num, timestamp);
	}

	// //////////////////////////////////////////////////////////////////////
	// Do Not Disturb

	@Override
	public void setDndMode(String groupId, String accountId, short mode, int timestamp)
	{
		groupMemberDao.updateDndMode(groupId, accountId, mode, timestamp);
	}

	@Override
	public short queryDndMode(String groupId, String accountId)
	{
		return groupMemberDao.selectDndMode(groupId, accountId);
	}

	// //////////////////////////////////////////////////////////////////////

	private SupAccountService accountService;
	private GaGroupMemberDao groupMemberDao;
	private GaGroupDao groupDao;

	public void setGroupDao(GaGroupDao dao)
	{
		this.groupDao = dao;
	}

	public void setGroupMemberDao(GaGroupMemberDao dao)
	{
		this.groupMemberDao = dao;
	}

	public GaGroupDao getGroupDao()
	{
		return groupDao;
	}

	public GaGroupMemberDao getGroupMemberDao()
	{
		return groupMemberDao;
	}

	public SupAccountService getAccountService()
	{
		return accountService;
	}

	public void setAccountService(SupAccountService accountService)
	{
		this.accountService = accountService;
	}

}
