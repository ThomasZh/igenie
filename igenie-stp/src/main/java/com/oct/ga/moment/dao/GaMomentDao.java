package com.oct.ga.moment.dao;

import java.util.List;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.domain.moment.GaMomentPhotoObject;

public interface GaMomentDao
{
	public void addMoment(String superId, String channelId, String momentId, String userId, String desc, int photoNum,
			int timestamp);

	public void addMomentPhoto(String superId, String channelId, String momentId, int seq, String photoId,
			String photoUrl, int timestamp);

	public GaMomentObject queryMoment(String momentId);

	public Page<GaMomentObject> queryPagination(String channelId, int pageNum, int pageSize);

	public Page<GaMomentPhotoObject> queryMomentPhotoFlowPagination(String channelId, int pageNum, int pageSize);

	public Page<GaMomentPhotoObject> queryClubMomentPhotoFlowPagination(String clubId, int pageNum, int pageSize);

	public List<String> queryMeomentPhotos(String momentId);

	public int countPhotoNum(String channelId);

	public void removeMoment(String momentId);

	public void removeMomentPhoto(String momentId);

	public int queryFavoriteNum(String momentId);

	public void modifyFavoriteNum(String momentId, int num);

	public int queryCommentNum(String momentId);

	public void modifyCommentNum(String momentId, int num);

	public String queryMomentOwner(String momentId);
	
	public String queryChannelId(String momentId);
}
