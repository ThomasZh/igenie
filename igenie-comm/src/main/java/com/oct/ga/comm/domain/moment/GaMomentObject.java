package com.oct.ga.comm.domain.moment;

import java.util.List;

import com.google.gson.Gson;
import com.oct.ga.comm.domain.JsonBeanAdapter;

public class GaMomentObject
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3067743932929217896L;
	private String channelId;
	private String channelName;
	private String momentId;
	private String userId;
	private String userName;
	private String userPhotoUrl;
	private String desc;
	private int timestamp;
	private List<String> photos;
	private short memberRank;
	private List<GaMomentCommentObject> comments;
	private List<GaMomentFavoriteObject> favorites;
	private int favoriteNum;
	private int commentNum;
	private boolean isFavorite;
	
	@Override
	public GaMomentObject decode(String json)
	{
		Gson gson = new Gson();
		GaMomentObject info = gson.fromJson(json, GaMomentObject.class);
		return info;
	}

	@Override
	public String encode()
	{
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}

	@Override
	public String toString()
	{
		return this.encode();
	}

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getMomentId()
	{
		return momentId;
	}

	public void setMomentId(String momentId)
	{
		this.momentId = momentId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public int getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public List<String> getPhotos()
	{
		return photos;
	}

	public void setPhotos(List<String> photos)
	{
		this.photos = photos;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserPhotoUrl()
	{
		return userPhotoUrl;
	}

	public void setUserPhotoUrl(String userPhotoUrl)
	{
		this.userPhotoUrl = userPhotoUrl;
	}

	public String getChannelName()
	{
		return channelName;
	}

	public void setChannelName(String channelName)
	{
		this.channelName = channelName;
	}

	public short getMemberRank()
	{
		return memberRank;
	}

	public void setMemberRank(short memberRank)
	{
		this.memberRank = memberRank;
	}

	public List<GaMomentCommentObject> getComments()
	{
		return comments;
	}

	public void setComments(List<GaMomentCommentObject> comments)
	{
		this.comments = comments;
	}

	public List<GaMomentFavoriteObject> getFavorites()
	{
		return favorites;
	}

	public void setFavorites(List<GaMomentFavoriteObject> favorites)
	{
		this.favorites = favorites;
	}

	public int getFavoriteNum()
	{
		return favoriteNum;
	}

	public void setFavoriteNum(int favoriteNum)
	{
		this.favoriteNum = favoriteNum;
	}

	public int getCommentNum()
	{
		return commentNum;
	}

	public void setCommentNum(int commentNum)
	{
		this.commentNum = commentNum;
	}

	public boolean isFavorite()
	{
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite)
	{
		this.isFavorite = isFavorite;
	}

}
