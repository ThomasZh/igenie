package com.oct.ga.activity.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oct.ga.activity.dao.MomentDao;
import com.oct.ga.activity.domain.Moment;
import com.oct.ga.activity.service.MomentService;

@Service
@Transactional
public class MomentServiceImpl implements MomentService {

	@Autowired
	private MomentDao momentDao;

	@Transactional(readOnly = true)
	@Override
	public List<Moment> findByActivityId(String activityId, long createTime, int pageSize) {
		return momentDao.findByActivityId(activityId, createTime, pageSize);
	}

	@Override
	public void create(Moment moment) {
		momentDao.create(moment);
	}

	@Override
	public void delete(String id) {
		momentDao.delete(id);
	}

}
