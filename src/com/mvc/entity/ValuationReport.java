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
@Table(name = "valuationReport")
public class ValuationReport implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "unit")
	private String unit;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "valuationId")
	private String valuationId;
	
	@Column(name = "price")
	private String price;
	
	@Column(name = "priceDate")
	private String priceDate;
	
	@Column(name = "priceUser")
	private String priceUser;
	
	@Column(name = "priceUserTruename")
	private String priceUserTruename;
	
	@Column(name = "total")
	private String total;
	
	@Column(name = "totalDate")
	private String totalDate;
	
	@Column(name = "totalUser")
	private String totalUser;
	
	@Column(name = "totalUserTruename")
	private String totalUserTruename;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "cost")
	private String cost;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValuationId() {
		return valuationId;
	}

	public void setValuationId(String valuationId) {
		this.valuationId = valuationId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getPriceUser() {
		return priceUser;
	}

	public void setPriceUser(String priceUser) {
		this.priceUser = priceUser;
	}

	public String getPriceUserTruename() {
		return priceUserTruename;
	}

	public void setPriceUserTruename(String priceUserTruename) {
		this.priceUserTruename = priceUserTruename;
	}

	public String getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(String totalUser) {
		this.totalUser = totalUser;
	}

	public String getTotalUserTruename() {
		return totalUserTruename;
	}

	public void setTotalUserTruename(String totalUserTruename) {
		this.totalUserTruename = totalUserTruename;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPriceDate() {
		return priceDate;
	}

	public void setPriceDate(String priceDate) {
		this.priceDate = priceDate;
	}

	public String getTotalDate() {
		return totalDate;
	}

	public void setTotalDate(String totalDate) {
		this.totalDate = totalDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
