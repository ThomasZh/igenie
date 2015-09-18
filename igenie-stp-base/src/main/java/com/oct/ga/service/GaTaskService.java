package com.oct.ga.service;

import java.util.List;

import com.oct.ga.comm.domain.BadgeNumberJsonBean;
import com.oct.ga.comm.domain.Page;
import com.oct.ga.comm.domain.msg.NotifyTaskLog;
import com.oct.ga.comm.domain.task.TaskNote;
import com.oct.ga.comm.domain.taskext.ChildTaskMaster;
import com.oct.ga.comm.domain.taskext.GaTaskLog;
import com.oct.ga.comm.domain.taskext.ProjectMaster;
import com.oct.ga.comm.domain.taskext.TodayTaskMaster;
import com.oct.ga.comm.domain.taskpro.TaskProBaseInfo;
import com.oct.ga.comm.domain.taskpro.TaskProDetailInfo;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;

public interface GaTaskService
{
	public List<String> queryCompletedProjectIds(String userId, int lastTryTime);

	public List<String> queryUncompletedProjectIds(String userId, int lastTryTime);

	public TaskProBaseInfo queryTaskBaseInfo(String taskId);

	public List<String> queryUncompletedTaskIds(String userId, int lastTryTime, int timestamp);

	public List<String> queryUncompletedTaskIds(String userId, int lastTryTime, int startTime, int endTime);

	public List<String> queryCompletedTaskIds(String userId, int lastTryTime, int startTime, int endTime);

	public List<TaskProBaseInfo> queryLastUpdateChildTask(String taskId, int lastTryTime);

	public TaskProDetailInfo queryTaskDetailLastUpdate(String taskId, int lastTryTime);

	public short queryChannelType(String taskId);

	public void moveto(String taskId, String projectId, int timestamp);

	// //////////////////////////////////////////////////////////////////////
	// task ext
	/**
	 * id,name,color,state,startTime; not include version info.
	 */
	public ProjectMaster queryProjectMaster(String projectId);

	/**
	 * id,name,color,state,startTime,memberNum; not include your member rank
	 * info.
	 */
	public List<ChildTaskMaster> queryLastModifyChildTask(String projectId, int lastTryTime);

	/**
	 * id,name,color,state,startTime,pid; not include project name
	 */
	public TodayTaskMaster queryTodayTaskMaster(String taskId);

	public void modifyCompletedTime(String taskId, int timestamp);

	// //////////////////////////////////////////////////////////////////////

	public int batchUpdateActivityToReceivedState(String taskId, String userId, int timestamp);

	public void updateActivityToReadState(NotifyTaskLog activity);

	public BadgeNumberJsonBean queryBadgeNumber(String projectId, String accountId, int timestamp);

	public NotifyTaskLog queryLastOneByProject(String projectId, String toAccountId, int timestamp);

	// //////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @return projectNumber
	 */
	public int queryProjectNumberByAccount(String accountId);

	public List<String> queryUncompleteProjectIdsByAccount(String accountId);

	/**
	 * Query project id list by myAccountId.
	 * 
	 * @param myAccountId
	 * @return projectId(String) List
	 */
	public List<String> queryProjectIdsByAccount(String accountId);

	/**
	 * Query task is list by projectId.
	 * 
	 * @param projectId
	 * @return taskId(String) List
	 */
	public List<String> queryTaskIdsByProject(String projectId);

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @param timestamp
	 * @return List<TaskProDetailInfo>
	 */
	public List<TaskProExtInfo> queryLastUpdateProjectsByAccount(String accountId, int timestamp);

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @param timestamp
	 * @return List<TaskProDetailInfo>
	 */
	public List<TaskProExtInfo> queryLastUpdateTasksByAccount(String accountId, int timestamp);

	/**
	 * 
	 * 
	 * @param projectId
	 * @param timestamp
	 * @return List<TaskProDetailInfo>
	 */
	public List<TaskProExtInfo> queryLastUpdateTasksByProject(String projectId, int timestamp);

	/**
	 * 
	 * 
	 * @param taskId
	 * @param timestamp
	 * @return List<TaskNote>
	 */
	public List<TaskNote> queryLastUpdateTaskNotes(String taskId, int timestamp);

	/**
	 * 
	 * 
	 * @param taskId
	 * @param timestamp
	 * @param pageNum
	 * @param pageSize
	 * @return Page<NotifyTaskLog>
	 */
	public Page<NotifyTaskLog> queryTaskActivityPagination(String taskId, String accountId, int timestamp, int pageNum,
			int pageSize);

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @param timestamp
	 * @param pageNum
	 * @param pageSize
	 * @return one page of task activity
	 */
	public Page<NotifyTaskLog> queryTaskActivityPaginationByAccount(String accountId, int timestamp, int pageNum,
			int pageSize);

	// ********* Task Summary **********
	public int countTaskChildNumber(String taskId);

	public int countTaskNoteNumber(String taskId);

	public void removeTask(String taskId);

	// ********* Task Note **********
	public void add(TaskNote note, int timestamp);

	public void modify(TaskNote note, int timestamp);

	public void removeNote(String noteId, int timestamp);

	public List<TaskNote> queryNoteLastUpdate(String taskId, int timestamp);

	public boolean isExistNote(String noteId);

	// ********* Task Log **********

	public void add(NotifyTaskLog activity);

	public void addExtend(String activityId, String userId, short state, int timestamp);

	public void add(TaskProExtInfo task, int timestamp);

	public boolean isExist(String taskId);

	public TaskProExtInfo query(String taskId);

	public void update(TaskProExtInfo task, int timestamp);

	public TaskProExtInfo queryTaskLastUpdate(String taskId, int lastTryTime);

	// //////////////////////////////////////////////////////////////////////
	// task log

	public void addLog(GaTaskLog log, int timestamp);

	public GaTaskLog queryLog(String logId);

	public void addLogExtend(String logId, String toAccountId, String channelId, short actionTag, short syncState,
			int timestamp);

	public List<String> queryLogIdsPaginationFilter4Moment(String toAccountId, int pageNum, int pageSize);

	public List<String> queryLogIdsPagination(String toAccountId, int pageNum, int pageSize);

	// //////////////////////////////////////////////////////////////////////
	// Exercise task

	public String modifyExerciseProject2Completed(String accountId, short publishType, int timestamp);

}