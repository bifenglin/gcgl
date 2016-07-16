package com.mvc.common;

public enum ContractStowageEnum {
	ALLSOCRE("allScore","总分"),DATE("date","时间"),ALLMANAGE("allManage","管理实力"),
	ALLFUND("allFund","资金实力"),ALLTECHNOLOGY("allTechnology","技术实力"),
	ALLAVERAGE("allAverage", "平均分"),TIMES("times", "次数");
	
	private String value;
	private String key;
	
	private ContractStowageEnum(String key,String value) {
		this.value = value;
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
