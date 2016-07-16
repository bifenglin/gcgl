package com.mvc.service;

import java.util.List;

import com.mvc.entity.User;
import com.mvc.entity.Utp;

public interface UtpService {
	public List<Object> getUtpByUser(User user); 
	public void save(Utp utp);
	public Utp getUtpByUserNameAndProjectName(String userName, String projectName);
	public void updateUtp(Utp utp);
	public void delete(Utp utp);
	public List<Object> getUtpByProjectName(String projectName);
}
