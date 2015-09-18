package com.oct.ga.comm.domain.club;

import java.util.List;

import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.JsonBeanAdapter;
import com.oct.ga.comm.domain.apply.GaApplicantTemplateCell;

public class ActivityCreateInfo
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4361835647032918058L;

	private String name;
	private String desc;
	private String titleBkImage;
	private int startTime;
	private int endTime;
	private String locDesc;
	private String locX;
	private String locY;
	/**
	 * private:1211, public:1212
	 */
	private short publishType = GlobalArgs.CLUB_ACTIVITY_PUBLISH_TYPE_PRIVATE;
	/**
	 * yes:1, no:0
	 */
	private short approveType = GlobalArgs.FALSE;
	/**
	 * yes:1, no:0
	 */
	private short applyFormType = GlobalArgs.FALSE;
	private List<GaApplicantTemplateCell> participationCells;
	private List<GaApplicantTemplateCell> contactCells;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getTitleBkImage()
	{
		return titleBkImage;
	}

	public void setTitleBkImage(String titleBkImage)
	{
		this.titleBkImage = titleBkImage;
	}

	public int getStartTime()
	{
		return startTime;
	}

	public void setStartTime(int startTime)
	{
		this.startTime = startTime;
	}

	public int getEndTime()
	{
		return endTime;
	}

	public void setEndTime(int endTime)
	{
		this.endTime = endTime;
	}

	public String getLocDesc()
	{
		return locDesc;
	}

	public void setLocDesc(String locDesc)
	{
		this.locDesc = locDesc;
	}

	public String getLocX()
	{
		return locX;
	}

	public void setLocX(String locX)
	{
		this.locX = locX;
	}

	public String getLocY()
	{
		return locY;
	}

	public void setLocY(String locY)
	{
		this.locY = locY;
	}

	public short getPublishType()
	{
		return publishType;
	}

	public void setPublishType(short type)
	{
		this.publishType = type;
	}

	public short getApproveType()
	{
		return approveType;
	}

	public void setApproveType(short type)
	{
		this.approveType = type;
	}

	public short getApplyFormType()
	{
		return applyFormType;
	}

	public void setApplyFormType(short type)
	{
		this.applyFormType = type;
	}

	public List<GaApplicantTemplateCell> getParticipationCells()
	{
		return participationCells;
	}

	public void setParticipationCells(List<GaApplicantTemplateCell> participationCells)
	{
		this.participationCells = participationCells;
	}

	public List<GaApplicantTemplateCell> getContactCells()
	{
		return contactCells;
	}

	public void setContactCells(List<GaApplicantTemplateCell> contactCells)
	{
		this.contactCells = contactCells;
	}

}
