package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.moment.GaMomentCommentObject;
import com.oct.ga.comm.domain.moment.GaMomentFavoriteObject;
import com.oct.ga.comm.domain.moment.GaMomentLogObject;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.domain.moment.GaMomentPhotoObject;

public interface GaMomentService
{
	// //////////////////////////////////////////////////////////////////////
	// Moment

	public void addMoment(String superId, String channelId, String momentId, String userId, String desc, int photoNum,
			int timestamp);

	public void reomveMoment(String superId, String channelId, String momentId, String userId, int timestamp);

	public void addMomentPhoto(String superId, String channelId, String momentId, int seq, String photoId,
			String photoUrl, int timestamp);

	public GaMomentObject queryMoment(String momentId);

	public List<GaMomentObject> queryMomentPagination(String channelId, short pageNum, short pageSize);

	public List<GaMomentPhotoObject> queryMomentPhotoFlowPagination(String channelId, short pageNum, short pageSize);

	public List<GaMomentPhotoObject> queryClubMomentPhotoFlowPagination(String clubId, short pageNum, short pageSize);

	public List<String> queryMomentPhotos(String momentId);

	public int countPhotoNum(String channelId);

	public String queryMomentOwner(String momentId);

	public String queryChannelId(String momentId);

	// //////////////////////////////////////////////////////////////////////
	// Moment Favorite & Comment

	public void addMomentFavorite(String momentId, String accountId, int timestamp);

	public List<GaMomentFavoriteObject> queryMomentFavoritePagination(String momentId, short pageNum, short pageSize);

	public boolean isFavorte(String momentId, String accountId);

	public int queryFavoriteNum(String momentId);

	public int countFavoriteNum(String momentId);

	public void modifyFavoriteNum(String momentId, int num);

	public void addMomentComment(String momentId, String accountId, String txt, int timestamp);

	public List<GaMomentCommentObject> queryMomentCommentPagination(String momentId, short pageNum, short pageSize);

	public int queryCommentNum(String momentId);

	public int countCommentNum(String momentId);

	public void modifyCommentNum(String momentId, int num);

	// //////////////////////////////////////////////////////////////////////
	// Moment Favorite & Comment log

	public void addLog(String momentId, String accountId, short action, String txt, String toAccountId, int timestamp);

	public List<GaMomentLogObject> queryLogPagination(String toAccountId, int pageNum, int pageSize);

	public int modifyLogSyncState(String toAccountId, short syncState);
}
