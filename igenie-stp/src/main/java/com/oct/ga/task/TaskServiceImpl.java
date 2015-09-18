package com.oct.ga.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GlobalArgs;
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
import com.oct.ga.service.GaTaskService;
import com.oct.ga.task.dao.GaTaskActivityDao;
import com.oct.ga.task.dao.GaTaskInfoDao;
import com.oct.ga.task.dao.GaTaskLogDao;
import com.oct.ga.task.dao.GaTaskNoteDao;

public class TaskServiceImpl
		implements GaTaskService
{
	// /////////////////////////////////////////////////////
	// task ext
	@Override
	public ProjectMaster queryProjectMaster(String projectId)
	{
		return taskInfoDao.queryProjectMaster(projectId);
	}

	@Override
	public List<ChildTaskMaster> queryLastModifyChildTask(String projectId, int lastTryTime)
	{
		return taskInfoDao.queryLastModifyChildTask(projectId, lastTryTime);
	}

	@Override
	public TodayTaskMaster queryTodayTaskMaster(String taskId)
	{
		return taskInfoDao.queryTodayTaskMaster(taskId);
	}

	@Override
	public void modifyCompletedTime(String taskId, int timestamp)
	{
		taskInfoDao.updateCompletedTime(taskId, timestamp);
	}

	@Override
	public void moveto(String taskId, String projectId, int timestamp)
	{
		taskInfoDao.updateProjectId(taskId, projectId, timestamp);
	}

	// /////////////////////////////////////////////////////
	@Override
	public short queryChannelType(String taskId)
	{
		return taskInfoDao.queryChannelType(taskId);
	}

	@Override
	public List<String> queryCompletedProjectIds(String userId, int lastTryTime)
	{
		return taskInfoDao.queryCompletedProjectIds(userId, lastTryTime);
	}

	@Override
	public List<String> queryUncompletedProjectIds(String userId, int lastTryTime)
	{
		return taskInfoDao.queryUncompletedProjectIds(userId, lastTryTime);
	}

	@Override
	public List<String> queryUncompletedTaskIds(String userId, int lastTryTime, int startTime, int endTime)
	{
		return taskInfoDao.queryUncompletedTaskIds(userId, lastTryTime, startTime, endTime);
	}

	@Override
	public List<String> queryCompletedTaskIds(String userId, int lastTryTime, int startTime, int endTime)
	{
		return taskInfoDao.queryCompletedTaskIds(userId, lastTryTime, startTime, endTime);
	}

	public TaskProBaseInfo queryTaskBaseInfo(String taskId)
	{
		return taskInfoDao.queryTaskBaseInfo(taskId);
	}

	@Override
	public List<String> queryUncompletedTaskIds(String userId, int lastTryTime, int currentTimestamp)
	{
		return taskInfoDao.queryUncompletedTaskIds(userId, lastTryTime, currentTimestamp);
	}

	@Override
	public List<TaskProBaseInfo> queryLastUpdateChildTask(String taskId, int lastTryTime)
	{
		return taskInfoDao.queryLastUpdateChildTask(taskId, lastTryTime);
	}

	public TaskProDetailInfo queryTaskDetailLastUpdate(String taskId, int lastTryTime)
	{
		return taskInfoDao.queryTaskDetailLastUpdate(taskId, lastTryTime);
	}

	// //////////////////////////////////////////////////////////

	@Override
	public int batchUpdateActivityToReceivedState(String taskId, String userId, int timestamp)
	{
		return taskActivityDao.batchUpdateToSyncState(taskId, userId, GlobalArgs.SYNC_STATE_RECEIVED, timestamp);
	}

	@Override
	public void updateActivityToReadState(NotifyTaskLog activity)
	{
		if (activity.getSyncState() == GlobalArgs.SYNC_STATE_NOT_RECEIVED)
			taskActivityDao.updateExtendState(activity.get_id(), activity.getSendToAccountId(),
					GlobalArgs.SYNC_STATE_RECEIVED, DatetimeUtil.currentTimestamp());
	}

	@Override
	public BadgeNumberJsonBean queryBadgeNumber(String projectId, String accountId, int timestamp)
	{
		return taskActivityDao.queryUnreadNumberByProject(projectId, accountId, timestamp);
	}

	@Override
	public NotifyTaskLog queryLastOneByProject(String projectId, String toAccountId, int timestamp)
	{
		return taskActivityDao.queryLastOneByProject(projectId, toAccountId, timestamp);
	}

	// //////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @return projectNumber
	 */
	@Override
	public int queryProjectNumberByAccount(String accountId)
	{
		return taskInfoDao.queryProjectNumberByAccount(accountId);
	}

	@Override
	public List<String> queryUncompleteProjectIdsByAccount(String accountId)
	{
		return taskInfoDao.queryUncompleteProjectIdsByAccount(accountId);
	}

	/**
	 * Query project id list by myAccountId.
	 * 
	 * @param myAccountId
	 * @return projectId(String) List
	 */
	@Override
	public List<String> queryProjectIdsByAccount(String accountId)
	{
		return taskInfoDao.queryProjectIdsByAccount(accountId);
	}

	/**
	 * Query task is list by projectId.
	 * 
	 * @param projectId
	 * @return taskId(String) List
	 */
	@Override
	public List<String> queryTaskIdsByProject(String projectId)
	{
		return taskInfoDao.queryChildrenTaskList(projectId);
	}

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @param timestamp
	 * @return List<TaskProDetailInfo>
	 */
	@Override
	public List<TaskProExtInfo> queryLastUpdateProjectsByAccount(String accountId, int timestamp)
	{
		return taskInfoDao.queryLastUpdateProjectByAccount(accountId, timestamp);
	}

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @param timestamp
	 * @return List<TaskProDetailInfo>
	 */
	@Override
	public List<TaskProExtInfo> queryLastUpdateTasksByAccount(String accountId, int timestamp)
	{
		return taskInfoDao.queryLastUpdateTaskByAccount(accountId, timestamp);
	}

	/**
	 * 
	 * 
	 * @param projectId
	 * @param timestamp
	 * @return List<TaskProDetailInfo>
	 */
	@Override
	public List<TaskProExtInfo> queryLastUpdateTasksByProject(String projectId, int timestamp)
	{
		return taskInfoDao.queryLastUpdateByProject(projectId, timestamp);
	}

	/**
	 * 
	 * 
	 * @param taskId
	 * @param timestamp
	 * @return List<TaskNote>
	 */
	@Override
	public List<TaskNote> queryLastUpdateTaskNotes(String taskId, int timestamp)
	{
		return taskNoteDao.queryLastUpdate(taskId, timestamp);
	}

	/**
	 * 
	 * 
	 * @param taskId
	 * @param timestamp
	 * @param pageNum
	 * @param pageSize
	 * @return Page<NotifyTaskLog>
	 */
	@Override
	public Page<NotifyTaskLog> queryTaskActivityPagination(String taskId, String accountId, int timestamp, int pageNum,
			int pageSize)
	{
		return taskActivityDao.queryPagination(taskId, accountId, timestamp, pageNum, pageSize);
	}

	/**
	 * 
	 * 
	 * @param myAccountId
	 * @param timestamp
	 * @param pageNum
	 * @param pageSize
	 * @return one page of task activity
	 */
	@Override
	public Page<NotifyTaskLog> queryTaskActivityPaginationByAccount(String accountId, int timestamp, int pageNum,
			int pageSize)
	{
		return taskActivityDao.queryPaginationByAccount(accountId, timestamp, pageNum, pageSize);
	}

	// ********* start business service api **********

	@Override
	public int countTaskChildNumber(String taskId)
	{
		return taskInfoDao.countTaskChildNumber(taskId);
	}

	@Override
	public int countTaskNoteNumber(String taskId)
	{
		return taskNoteDao.countTaskNoteNumber(taskId);
	}

	@Override
	public void removeTask(String taskId)
	{
		taskInfoDao.remove(taskId);
	}

	// ********* start service api **********
	@Override
	public void add(TaskNote note, int timestamp)
	{
		taskNoteDao.add(note, timestamp);
	}

	@Override
	public void modify(TaskNote note, int timestamp)
	{
		taskNoteDao.update(note, timestamp);
	}

	@Override
	public void removeNote(String noteId, int timestamp)
	{
		taskNoteDao.updateState(noteId, GlobalArgs.NOTE_STATE_DELETED, timestamp);
	}

	@Override
	public List<TaskNote> queryNoteLastUpdate(String taskId, int timestamp)
	{
		return taskNoteDao.queryLastUpdate(taskId, timestamp);
	}

	@Override
	public boolean isExistNote(String noteId)
	{
		return taskNoteDao.isExist(noteId);
	}

	@Override
	public void add(NotifyTaskLog activity)
	{
		taskActivityDao.add(activity);
	}

	@Override
	public void addExtend(String activityId, String userId, short state, int timestamp)
	{
		taskActivityDao.addExtend(activityId, userId, state, timestamp);
	}

	@Override
	public void add(TaskProExtInfo task, int timestamp)
	{
		taskInfoDao.add(task, timestamp);
	}

	@Override
	public boolean isExist(String taskId)
	{
		return taskInfoDao.isExist(taskId);
	}

	@Override
	public TaskProExtInfo query(String taskId)
	{
		return taskInfoDao.query(taskId);
	}

	@Override
	public void update(TaskProExtInfo task, int timestamp)
	{
		taskInfoDao.update(task, timestamp);
	}

	@Override
	public TaskProExtInfo queryTaskLastUpdate(String taskId, int lastTryTime)
	{
		return taskInfoDao.queryLastUpdate(taskId, lastTryTime);
	}

	// ********* base dao of api *****************

	private GaTaskInfoDao taskInfoDao;
	private GaTaskActivityDao taskActivityDao;
	private GaTaskNoteDao taskNoteDao;
	private GaTaskLogDao taskLogDao;

	public void setTaskInfoDao(GaTaskInfoDao taskInfoDao)
	{
		this.taskInfoDao = taskInfoDao;
	}

	public void setTaskActivityDao(GaTaskActivityDao taskActivityDao)
	{
		this.taskActivityDao = taskActivityDao;
	}

	public void setTaskNoteDao(GaTaskNoteDao taskNoteDao)
	{
		this.taskNoteDao = taskNoteDao;
	}

	public GaTaskLogDao getTaskLogDao()
	{
		return taskLogDao;
	}

	public void setTaskLogDao(GaTaskLogDao taskLogDao)
	{
		this.taskLogDao = taskLogDao;
	}

	private final static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

	// /////////////////////////////////////////////////////
	// Task log

	@Override
	public void addLog(GaTaskLog log, int timestamp)
	{
		taskLogDao.insert(log, timestamp);
	}

	@Override
	public void addLogExtend(String logId, String toAccountId, String channelId, short actionTag, short syncState,
			int timestamp)
	{
		taskLogDao.insert(logId, toAccountId, channelId, actionTag, syncState, timestamp);
	}

	@Override
	public List<String> queryLogIdsPaginationFilter4Moment(String toAccountId, int pageNum, int pageSize)
	{
		Page<String> ids = taskLogDao.queryLogPagination(toAccountId, GlobalArgs.TASK_ACTION_ADD_ATTACH, pageNum,
				pageSize);
		return ids.getPageItems();
	}

	@Override
	public GaTaskLog queryLog(String logId)
	{
		return taskLogDao.query(logId);
	}

	@Override
	public List<String> queryLogIdsPagination(String toAccountId, int pageNum, int pageSize)
	{
		Page<String> ids = taskLogDao.queryLogPagination(toAccountId, pageNum, pageSize);
		return ids.getPageItems();
	}

	// //////////////////////////////////////////////////////////////////////
	// Exercise task

	@Override
	public String modifyExerciseProject2Completed(String accountId, short publishType, int timestamp)
	{
		String projectId = taskInfoDao.queryExerciseProjectId(accountId, publishType);
		if (projectId != null) {
			short state = taskInfoDao.queryProjectState(projectId);
			if (state == GlobalArgs.CLUB_ACTIVITY_STATE_OPENING) {
				taskInfoDao.updateState(projectId, GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED, timestamp);
				return projectId;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}