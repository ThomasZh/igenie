package com.oct.ga.task.dao;

import java.util.List;

import com.oct.ga.comm.domain.taskext.ChildTaskMaster;
import com.oct.ga.comm.domain.taskext.ProjectMaster;
import com.oct.ga.comm.domain.taskext.TodayTaskMaster;
import com.oct.ga.comm.domain.taskpro.TaskProBaseInfo;
import com.oct.ga.comm.domain.taskpro.TaskProDetailInfo;
import com.oct.ga.comm.domain.taskpro.TaskProExtInfo;

public interface GaTaskInfoDao
{
	public void updateCompletedTime(String taskId, int timestamp);

	public ProjectMaster queryProjectMaster(String projectId);

	public List<ChildTaskMaster> queryLastModifyChildTask(String projectId, int lastTryTime);

	public TodayTaskMaster queryTodayTaskMaster(String taskId);

	public short queryChannelType(String taskId);

	public List<String> queryCompletedProjectIds(String userId, int lastTryTime);

	public List<String> queryUncompletedProjectIds(String userId, int lastTryTime);

	public List<String> queryUncompletedTaskIds(String userId, int lastTryTime, int startTime, int endTime);

	public List<String> queryCompletedTaskIds(String userId, int lastTryTime, int startTime, int endTime);

	public TaskProBaseInfo queryTaskBaseInfo(String taskId);

	public List<String> queryUncompletedTaskIds(String userId, int lastTryTime, int currentTimestamp);

	public List<TaskProBaseInfo> queryLastUpdateChildTask(String taskId, int lastTryTime);

	public TaskProDetailInfo queryTaskDetailLastUpdate(String taskId, int lastTryTime);

	public void updateProjectId(String taskId, String projectId, int timestamp);

	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * 
	 * @param taskId
	 * @return List<taskId>
	 */
	public List<String> queryChildrenTaskList(String taskId);

	/**
	 * 
	 * 
	 * @param taskId
	 * @return task info
	 */
	public TaskProExtInfo query(String taskId);

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @param timestamp
	 * @return Task
	 */
	public List<TaskProExtInfo> queryLastUpdateProjectByAccount(String accountId, int timestamp);

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @param timestamp
	 * @return Task
	 */
	public List<TaskProExtInfo> queryLastUpdateTaskByAccount(String accountId, int timestamp);

	/**
	 * 
	 * 
	 * 
	 * @param projectId
	 * @param timestamp
	 * @return tasks
	 */
	public List<TaskProExtInfo> queryLastUpdateByProject(String projectId, int timestamp);

	/**
	 * 
	 * 
	 * @param taskId
	 * @param timestamp
	 * @return Task
	 */
	public TaskProExtInfo queryLastUpdate(String taskId, int timestamp);

	/**
	 * 
	 * 
	 * @param data
	 */
	public void add(TaskProExtInfo data, int timestamp);

	/**
	 * 
	 * 
	 * @param data
	 */
	public void update(TaskProExtInfo data, int timestamp);

	/**
	 * check the task(id) exist in GA task.
	 * 
	 * @param taskId
	 * @return true/false
	 */
	public boolean isExist(String taskId);

	/**
	 * 
	 * 
	 * @param taskId
	 */
	public void remove(String taskId);

	public int countTaskChildNumber(String taskId);

	public int queryProjectNumberByAccount(String accountId);

	public List<String> queryUncompleteProjectIdsByAccount(String accountId);

	public List<String> queryProjectIdsByAccount(String accountId);

	public String queryExerciseProjectId(String accountId, short publishType);

	public short queryProjectState(String projectId);

	public void updateState(String projectId, short state, int timestamp);

}
