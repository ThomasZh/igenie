package com.oct.ga.moment;

import java.util.List;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.moment.GaMomentCommentObject;
import com.oct.ga.comm.domain.moment.GaMomentFavoriteObject;
import com.oct.ga.comm.domain.moment.GaMomentLogObject;
import com.oct.ga.comm.domain.moment.GaMomentObject;
import com.oct.ga.comm.domain.moment.GaMomentPhotoObject;
import com.oct.ga.moment.dao.GaMomentCommentDao;
import com.oct.ga.moment.dao.GaMomentDao;
import com.oct.ga.moment.dao.GaMomentFavoriteDao;
import com.oct.ga.moment.dao.GaMomentLogDao;
import com.oct.ga.service.GaMomentService;

public class MomentServiceImpl
		implements GaMomentService
{
	@Override
	public String queryMomentOwner(String momentId)
	{
		return momentDao.queryMomentOwner(momentId);
	}
	
	@Override
	public String queryChannelId(String momentId)
	{
		return momentDao.queryChannelId(momentId);
	}

	@Override
	public void addMoment(String superId, String channelId, String momentId, String userId, String desc, int photoNum,
			int timestamp)
	{
		momentDao.addMoment(superId, channelId, momentId, userId, desc, photoNum, timestamp);
	}

	@Override
	public void reomveMoment(String superId, String channelId, String momentId, String userId, int timestamp)
	{
		momentDao.removeMoment(momentId);
		momentDao.removeMomentPhoto(momentId);
	}

	@Override
	public void addMomentPhoto(String superId, String channelId, String momentId, int seq, String photoId,
			String photoUrl, int timestamp)
	{
		momentDao.addMomentPhoto(superId, channelId, momentId, seq, photoId, photoUrl, timestamp);
	}

	@Override
	public GaMomentObject queryMoment(String momentId)
	{
		return momentDao.queryMoment(momentId);
	}

	@Override
	public List<GaMomentObject> queryMomentPagination(String channelId, short pageNum, short pageSize)
	{
		Page<GaMomentObject> moments = momentDao.queryPagination(channelId, pageNum, pageSize);
		List<GaMomentObject> array = moments.getPageItems();

		return array;
	}

	@Override
	public List<GaMomentPhotoObject> queryMomentPhotoFlowPagination(String channelId, short pageNum, short pageSize)
	{
		Page<GaMomentPhotoObject> moments = momentDao.queryMomentPhotoFlowPagination(channelId, pageNum, pageSize);
		List<GaMomentPhotoObject> array = moments.getPageItems();

		return array;
	}

	@Override
	public List<GaMomentPhotoObject> queryClubMomentPhotoFlowPagination(String clubId, short pageNum, short pageSize)
	{
		Page<GaMomentPhotoObject> moments = momentDao.queryClubMomentPhotoFlowPagination(clubId, pageNum, pageSize);
		List<GaMomentPhotoObject> array = moments.getPageItems();

		return array;
	}

	@Override
	public List<String> queryMomentPhotos(String momentId)
	{
		return momentDao.queryMeomentPhotos(momentId);
	}

	@Override
	public int countPhotoNum(String channelId)
	{
		return momentDao.countPhotoNum(channelId);
	}

	@Override
	public void addMomentFavorite(String momentId, String accountId, int timestamp)
	{
		if (!momentFavoriteDao.isExist(momentId, accountId)) {
			momentFavoriteDao.add(momentId, accountId, timestamp);
		}
	}

	@Override
	public void addMomentComment(String momentId, String accountId, String txt, int timestamp)
	{
		momentCommentDao.add(momentId, accountId, txt, timestamp);
	}

	@Override
	public List<GaMomentFavoriteObject> queryMomentFavoritePagination(String momentId, short pageNum, short pageSize)
	{
		Page<GaMomentFavoriteObject> pages = momentFavoriteDao.queryPagination(momentId, pageNum, pageSize);
		return pages.getPageItems();
	}

	@Override
	public List<GaMomentCommentObject> queryMomentCommentPagination(String momentId, short pageNum, short pageSize)
	{
		Page<GaMomentCommentObject> pages = momentCommentDao.queryPagination(momentId, pageNum, pageSize);
		return pages.getPageItems();
	}

	@Override
	public boolean isFavorte(String momentId, String accountId)
	{
		return momentFavoriteDao.isExist(momentId, accountId);
	}

	@Override
	public int queryFavoriteNum(String momentId)
	{
		return momentDao.queryFavoriteNum(momentId);
	}

	@Override
	public void modifyFavoriteNum(String momentId, int num)
	{
		momentDao.modifyFavoriteNum(momentId, num);
	}

	@Override
	public int queryCommentNum(String momentId)
	{
		return momentDao.queryCommentNum(momentId);
	}

	@Override
	public void modifyCommentNum(String momentId, int num)
	{
		momentDao.modifyCommentNum(momentId, num);
	}

	@Override
	public void addLog(String momentId, String accountId, short action, String txt, String toAccountId, int timestamp)
	{
		momentLogDao.add(momentId, accountId, action, txt, toAccountId, timestamp);
	}

	@Override
	public List<GaMomentLogObject> queryLogPagination(String toAccountId, int pageNum, int pageSize)
	{
		Page<GaMomentLogObject> page = momentLogDao.queryPagination(toAccountId, pageNum, pageSize);
		return page.getPageItems();
	}

	@Override
	public int modifyLogSyncState(String toAccountId, short syncState)
	{
		return momentLogDao.modifySyncState(toAccountId, syncState);
	}

	@Override
	public int countFavoriteNum(String momentId)
	{
		return momentFavoriteDao.countNum(momentId);
	}

	@Override
	public int countCommentNum(String momentId)
	{
		return momentCommentDao.countNum(momentId);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////

	private GaMomentDao momentDao;
	private GaMomentFavoriteDao momentFavoriteDao;
	private GaMomentCommentDao momentCommentDao;
	private GaMomentLogDao momentLogDao;

	public GaMomentCommentDao getMomentCommentDao()
	{
		return momentCommentDao;
	}

	public void setMomentCommentDao(GaMomentCommentDao momentCommentDao)
	{
		this.momentCommentDao = momentCommentDao;
	}

	public GaMomentDao getMomentDao()
	{
		return momentDao;
	}

	public void setMomentDao(GaMomentDao momentDao)
	{
		this.momentDao = momentDao;
	}

	public GaMomentFavoriteDao getMomentFavoriteDao()
	{
		return momentFavoriteDao;
	}

	public void setMomentFavoriteDao(GaMomentFavoriteDao momentFavoriteDao)
	{
		this.momentFavoriteDao = momentFavoriteDao;
	}

	public GaMomentLogDao getMomentLogDao()
	{
		return momentLogDao;
	}

	public void setMomentLogDao(GaMomentLogDao momentLogDao)
	{
		this.momentLogDao = momentLogDao;
	}



}
