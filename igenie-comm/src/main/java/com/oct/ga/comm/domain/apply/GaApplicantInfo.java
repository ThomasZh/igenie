package com.oct.ga.comm.domain.apply;

import java.util.List;

import com.oct.ga.comm.domain.JsonBeanAdapter;

/**
 * 单个申请人信息集合
 * @author thomas
 *
 */
public class GaApplicantInfo
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6743156296359974710L;

	private int seq;
	private List<GaApplicantCell> applicant;

	public int getSeq()
	{
		return seq;
	}

	public void setSeq(int seq)
	{
		this.seq = seq;
	}

	public List<GaApplicantCell> getApplicant()
	{
		return applicant;
	}

	public void setApplicant(List<GaApplicantCell> applicant)
	{
		this.applicant = applicant;
	}

}
