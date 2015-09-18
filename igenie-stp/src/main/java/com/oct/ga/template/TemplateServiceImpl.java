package com.oct.ga.template;

import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;
import com.oct.ga.comm.domain.template.ChecknameJsonBean;
import com.oct.ga.comm.domain.template.ClwcJsonBean;
import com.oct.ga.comm.domain.template.GaTemplateMaster;
import com.oct.ga.comm.domain.template.TemplateDefineJsonBean;
import com.oct.ga.comm.parser.JsonParser;
import com.oct.ga.service.GaGroupService;
import com.oct.ga.service.GaTaskService;
import com.oct.ga.service.GaTemplateService;
import com.oct.ga.task.TaskServiceImpl;
import com.oct.ga.template.dao.GaTemplateDao;

public class TemplateServiceImpl
		implements GaTemplateService
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
	@Override
	public Page<GaTemplateMaster> queryTemplatePagination(short templateType, short supplierType, String taskId,
			int pageNum, int pageSize)
	{
		switch (supplierType) {
		case GlobalArgs.TEMPLATE_SUPPLIER_TYPE_RECOMMEND:
			return templateDao.queryRecommendPagination(templateType, pageNum, pageSize);
		case GlobalArgs.TEMPLATE_SUPPLIER_TYPE_VENDOR:
			return templateDao.queryVendorPagination(templateType, taskId, pageNum, pageSize);
		case GlobalArgs.TEMPLATE_SUPPLIER_TYPE_MINE:
			return templateDao.queryMinePagination(templateType, taskId, pageNum, pageSize);
		}

		return null;
	}

	@Override
	public void add(TemplateDefineJsonBean templateDefine)
	{
		templateDao.add(templateDefine);
	}

	@Override
	public TemplateDefineJsonBean queryMaxVersion(String templateId)
	{
		return templateDao.queryMaxVersion(templateId);
	}

	@Override
	public TemplateDefineJsonBean query(String templateId, int version)
	{
		return templateDao.query(templateId, version);
	}

	@Override
	public List<TemplateDefineJsonBean> queryChildren(String templatePid)
	{
		return templateDao.queryChildren(templatePid);
	}

	/**
	 * 
	 * 
	 * @param taskId
	 * @return version number
	 */
	@Override
	public int queryMaxVersionByTask(String taskId)
	{
		return templateDao.queryMaxVersionByTask(taskId);
	}

	/**
	 * 
	 * 
	 * @param taskId
	 * @return
	 */
	@Override
	public String queryTemplateId(String taskId)
	{
		return templateDao.queryTemplateId(taskId);
	}

	// ///////////////////////////////////////////////////////////////////

	@Override
	public TaskProExtInfo makeTemplate2Task(ApplicationContext context, TemplateDefineJsonBean taskTemplate,
			String projectId, String creatorId, int startTime)
	{
		GaTaskService taskService = (GaTaskService) context.getBean("gaTaskService");
		GaGroupService groupService = (GaGroupService) context.getBean("gaGroupService");

		int currentTimestamp = DatetimeUtil.currentTimestamp();

		TaskProExtInfo task = new TaskProExtInfo();
		String taskId = UUID.randomUUID().toString();
		task.setId(taskId);
		task.setPid(projectId);
		task.setTemplateId(taskTemplate.getTemplateId());
		task.setTemplateVersion(taskTemplate.getVersion());
		task.setName(taskTemplate.getTemplateName());
		task.setDesc(taskTemplate.getTemplateDesc());
		task.setCreateAccountId(creatorId);
		task.setStartTime(startTime + taskTemplate.getStartTime());
		task.setEndTime(startTime + taskTemplate.getEndTime());

		JSONArray jsonArray = JSONArray.fromObject(taskTemplate.getExtAttr());
		String extAttrStr = jsonArray.toString();

		task.setExtAttribute(extAttrStr);
		task.setPermission(taskTemplate.getPermissionMode());
		task.setMemberNum((short) 1);

		taskService.add(task, currentTimestamp);
		groupService.createGroup(taskId, taskTemplate.getTemplateName(), GlobalArgs.CHANNEL_TYPE_TASK,
				currentTimestamp, creatorId);
		groupService.joinAsLeader(taskId, creatorId, currentTimestamp);

		return task;
	}

	/**
	 * use project's template create an new project.
	 */
	@Override
	public void makeTemplate2Project(ApplicationContext context, TemplateDefineJsonBean projectTemplate,
			String creatorId, int startTime)
	{
		TaskProExtInfo project = makeTemplate2Task(context, projectTemplate, "", creatorId, startTime);

		makeTemplateIntoProject(context, projectTemplate, project, creatorId, startTime);
	}

	/**
	 * project's template into an exist project.
	 */
	@Override
	public void makeTemplateIntoProject(ApplicationContext context, TemplateDefineJsonBean projectTemplate,
			TaskProExtInfo project, String creatorId, int startTime)
	{
		List<TemplateDefineJsonBean> childrenTemplate = projectTemplate.getChildren();
		for (TemplateDefineJsonBean childTemplate : childrenTemplate) {
			makeTemplate2Task(context, childTemplate, project.getId(), creatorId, startTime);
		}
	}

	@Override
	public TemplateDefineJsonBean makeTask2Template(TaskProExtInfo task, String creatorId, String templatePid,
			String templateName, int maxVersion, int deltaTime)
	{
		String templateId = UUID.randomUUID().toString();
		TemplateDefineJsonBean template = new TemplateDefineJsonBean();
		template.setTemplateId(templateId);
		template.setTemplatePid(templatePid);
		if (task.getPid() == null || task.getPid().length() == 0) {
			template.setTemplateType(GlobalArgs.TEMPLATE_TYPE_PROJECT);
		} else {
			template.setTemplateType(GlobalArgs.TEMPLATE_TYPE_TASK);
		}
		template.setTemplateName(templateName);
		template.setTemplateDesc(task.getDesc());
		template.setTaskId(task.getId());
		template.setAccountId(creatorId);
		template.setVersion(maxVersion);
		template.setCreateTime(DatetimeUtil.currentTimestamp());

		switch (template.getExtAttrType()) {
		case GlobalArgs.TEMPLATE_EXTATTR_TYPE_CHECK_LIST:
			List<ChecknameJsonBean> checklist = JsonParser.json2Checklist(task.getExtAttribute());
			for (ChecknameJsonBean check : checklist) {
				template.addChildExtAttr(check);
			}
			template.setExtAttr(checklist);
			break;
		case GlobalArgs.TEMPLATE_EXTATTR_TYPE_CLWC_LIST:
			List<ClwcJsonBean> clwclist = JsonParser.json2Clwclist(task.getExtAttribute());
			for (ClwcJsonBean clwc : clwclist) {
				template.addChildExtAttr(clwc);
			}
			break;
		case GlobalArgs.TEMPLATE_EXTATTR_TYPE_NORMAL_TASK:
		default:
			break;
		}

		template.setStartTime(deltaTime);
		template.setEndTime(task.getEndTime() - task.getStartTime() + deltaTime);
		template.setPermissionMode(task.getPermission());
		template.setSupplierType(GlobalArgs.TEMPLATE_SUPPLIER_TYPE_MINE);

		String taskTemplateId = task.getTemplateId();
		if (taskTemplateId.equals("00000000-0000-0000-0000-000000000000")) {
			if (task.getExtAttribute() == null) {
				template.setExtAttrType(GlobalArgs.TEMPLATE_EXTATTR_TYPE_NORMAL_TASK);
			} else {
				template.setExtAttrType(GlobalArgs.TEMPLATE_EXTATTR_TYPE_CHECK_LIST);
			}

			template.setSplitForEachMember(false);
			template.setFeedbackInvite(true);
			template.setFeedbackUpdate(true);
		} else {
			TemplateDefineJsonBean templateInfo = queryMaxVersion(taskTemplateId);
			template.setExtAttrType(templateInfo.getExtAttrType());
			template.setSplitForEachMember(templateInfo.isSplitForEachMember());
			template.setFeedbackInvite(templateInfo.isFeedbackInvite());
			template.setFeedbackUpdate(templateInfo.isFeedbackUpdate());
		}

		add(template);

		return template;
	}

	@Override
	public TemplateDefineJsonBean makeProject2Template(ApplicationContext context, TaskProExtInfo project,
			String creatorId, String templatePid, String templateName)
	{
		TaskServiceImpl taskService = (TaskServiceImpl) context.getBean("gaTaskService");

		int maxVersion = queryMaxVersionByTask(project.getId());
		logger.debug("maxVersion: " + maxVersion);
		int newVersion = maxVersion + 1;
		int deltaTime = 0;

		TemplateDefineJsonBean projectTemplate = makeTask2Template(project, creatorId, templatePid, templateName,
				newVersion, deltaTime);

		List<String> childTaskIds = taskService.queryTaskIdsByProject(project.getId());
		for (String childTaskId : childTaskIds) {
			TaskProExtInfo childTask = taskService.query(childTaskId);

			makeTask2Template(childTask, creatorId, projectTemplate.getTemplateId(), childTask.getName(), newVersion,
					childTask.getStartTime() - project.getStartTime());
		}

		return projectTemplate;
	}

	// ///////////////////////////////////////////////////////////////////

	private GaTemplateDao templateDao;

	public GaTemplateDao getTemplateDao()
	{
		return templateDao;
	}

	public void setTemplateDao(GaTemplateDao templateDao)
	{
		this.templateDao = templateDao;
	}

	private final static Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);

}
