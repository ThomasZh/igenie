package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.FriendInvite;

public interface GaFriendInviteService
{
	public void addEkey(String ekey, String fromUserId, String toRegisterId, short channelType, String channelId,
			int timestamp);

	public FriendInvite queryInvite(String ekey);

	public List<FriendInvite> queryInviteList(String toInvitedRegisterId);

	public void modifyState(String ekey, int timestamp);
}
