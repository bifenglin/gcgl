package com.mvc.entity;

import java.util.List;
import java.util.Map;

public class UserLoginResult {
	private String code;
	private String msg;
	private List<Utp> utps;
	private List<Project> projects;
	private User user;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<Utp> getUtps() {
		return utps;
	}
	public void setUtps(List<Utp> utps) {
		this.utps = utps;
	}
	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
