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
@Table(name = "project")
public class Project implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "fullName")
	private String fullName;
	
	@Column(name = "center")
	private String center;
	
	@Column(name = "map")
	private String map;
	
	@Column(name = "cut")
	private String cut;
	
	@Column(name = "creator")
	private String creator;
	
	@Column(name = "date")
	private String date;
	
	@Column(name = "machineCost")
	private String machineCost;
	
	@Column(name = "peopleCost")
	private String peopleCost;
	
	@Column(name = "materialCost")
	private String materialCost;
	
	@Column(name = "otherCost")
	private String otherCost;
	
	@Column(name = "allCost")
	private String allCost;
	
	@Column(name = "costDate")
	private String costDate;
	
	@Column(name = "remark")
	private String remark;

	@Column(name = "status")
	private String status;
	
	@Column(name = "expectEndDate")
	private String expectEndDate;
	
	@Column(name = "expectProfit")
	private String expectProfit;
	
	@Column(name = "departmentName")
	private String departmentName;
	
	@Column(name = "allValuation")
	private String allValuation;
	
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

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMachineCost() {
		return machineCost;
	}

	public void setMachineCost(String machineCost) {
		this.machineCost = machineCost;
	}

	public String getPeopleCost() {
		return peopleCost;
	}

	public void setPeopleCost(String peopleCost) {
		this.peopleCost = peopleCost;
	}

	public String getMaterialCost() {
		return materialCost;
	}

	public void setMaterialCost(String materialCost) {
		this.materialCost = materialCost;
	}

	public String getOtherCost() {
		return otherCost;
	}

	public void setOtherCost(String otherCost) {
		this.otherCost = otherCost;
	}

	public String getAllCost() {
		return allCost;
	}

	public void setAllCost(String allCost) {
		this.allCost = allCost;
	}

	public String getCostDate() {
		return costDate;
	}

	public void setCostDate(String costDate) {
		this.costDate = costDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExpectEndDate() {
		return expectEndDate;
	}

	public void setExpectEndDate(String expectEndDate) {
		this.expectEndDate = expectEndDate;
	}

	public String getExpectProfit() {
		return expectProfit;
	}

	public void setExpectProfit(String expectProfit) {
		this.expectProfit = expectProfit;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getAllValuation() {
		return allValuation;
	}

	public void setAllValuation(String allValuation) {
		this.allValuation = allValuation;
	}

	public String getCut() {
		return cut;
	}

	public void setCut(String cut) {
		this.cut = cut;
	}

}
