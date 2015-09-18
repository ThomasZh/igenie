package com.oct.ga.template.dao;

import java.util.List;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.template.GaTemplateMaster;
import com.oct.ga.comm.domain.template.TemplateDefineJsonBean;

public interface GaTemplateDao
{
	/**
	 * 
	 * 
	 * @param templateType
	 * @return
	 */
	public Page<GaTemplateMaster> queryRecommendPagination(short templateType, int pageNum, int pageSize);

	public Page<GaTemplateMaster> queryVendorPagination(short templateType, String taskId, int pageNum, int pageSize);

	public Page<GaTemplateMaster> queryMinePagination(short templateType, String accountId, int pageNum, int pageSize);

	public void add(TemplateDefineJsonBean template);

	public TemplateDefineJsonBean queryMaxVersion(String templateId);

	public TemplateDefineJsonBean query(String templateId, int version);

	public List<TemplateDefineJsonBean> queryChildren(String templatePid);

	/**
	 * 
	 * 
	 * @param taskId
	 * @return version number
	 */
	public int queryMaxVersionByTask(String taskId);

	/**
	 * 
	 * 
	 * @param taskId
	 * @return
	 */
	public String queryTemplateId(String taskId);

}
