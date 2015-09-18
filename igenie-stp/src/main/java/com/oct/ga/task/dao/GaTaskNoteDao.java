package com.oct.ga.task.dao;

import java.util.List;

import com.oct.ga.comm.domain.task.TaskNote;

public interface GaTaskNoteDao
{

	public void add(TaskNote data, int timestamp);

	public List<TaskNote> queryLastUpdate(String taskId, int lastTryTime);

	public void updateState(String noteId, short state, int timestamp);

	public void update(TaskNote data, int timestamp);

	public int countTaskNoteNumber(String taskId);

	public boolean isExist(String noteId);
}
