package com.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.EntityDao;
import com.mvc.entity.UserLog;


@Service("UserLogServiceImpl")
public class UserLogServiceImpl implements UserLogService{
	@Autowired
	private EntityDao entityDao;
	
	public void saveUserLog(UserLog userLog){
		entityDao.save(userLog);
	}
	
}
