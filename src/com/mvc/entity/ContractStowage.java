package com.mvc.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contractStowage")
public class ContractStowage implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Column(name = "name	")
	private String name	;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "date")
	private String date;
	
	@Column(name = "	company")
	private String company;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "times")
	private String times;
	
	@Column(name = "allAverage")
	private String allAverage;
	
	@Column(name = "allScore")
	private String allScore;
	
	@Column(name = "allManage")
	private String allManage;
	
	@Column(name = "allFund")
	private String allFund;
	
	@Column(name = "allTechnology")
	private String allTechnology;
	
	@Column(name = "status")
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAllScore() {
		return allScore;
	}

	public void setAllScore(String allScore) {
		this.allScore = allScore;
	}

	public String getAllManage() {
		return allManage;
	}

	public void setAllManage(String allManage) {
		this.allManage = allManage;
	}

	public String getAllFund() {
		return allFund;
	}

	public void setAllFund(String allFund) {
		this.allFund = allFund;
	}

	public String getAllTechnology() {
		return allTechnology;
	}

	public void setAllTechnology(String allTechnology) {
		this.allTechnology = allTechnology;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getAllAverage() {
		return allAverage;
	}

	public void setAllAverage(String allAverage) {
		this.allAverage = allAverage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
