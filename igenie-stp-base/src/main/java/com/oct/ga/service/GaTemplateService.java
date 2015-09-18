package com.oct.ga.service;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.task.TaskDetailInfo;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.domain.template.GaTemplateMaster;
import com.oct.ga.comm.domain.template.TemplateDefineJsonBean;

public interface GaTemplateService
{
	/**
	 * 
	 * 
	 * @param templateType
	 * @param supplierType
	 *            : 321 recommend,322 vendor,323 mine
	 * @param pageNum
	 * @param pageSize
	 * @return Page<GaTemplateMaster>
	 */
	public Page<GaTemplateMaster> queryTemplatePagination(short templateType, short supplierType, String taskId,
			int pageNum, int pageSize);

	public void add(TemplateDefineJsonBean templateDefine);

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

	// ///////////////////////////////////////////////////////////////////

	public TaskProExtInfo makeTemplate2Task(ApplicationContext context, TemplateDefineJsonBean taskTemplate,
			String projectId, String creatorId, int startTime);

	/**
	 * use project's template create an new project.
	 */
	public void makeTemplate2Project(ApplicationContext context, TemplateDefineJsonBean projectTemplate,
			String creatorId, int startTime);

	/**
	 * project's template into an exist project.
	 */
	public void makeTemplateIntoProject(ApplicationContext context, TemplateDefineJsonBean projectTemplate,
			TaskProExtInfo project, String creatorId, int startTime);

	public TemplateDefineJsonBean makeTask2Template(TaskProExtInfo task, String creatorId, String templatePid,
			String templateName, int maxVersion, int deltaTime);

	public TemplateDefineJsonBean makeProject2Template(ApplicationContext context, TaskProExtInfo project,
			String creatorId, String templatePid, String templateName);

}
