package com.mvc.common;

public enum CostTypeEnum {
	DAYTAI("daytai", "天/台"),DAYREN("dayren", "天/人"),HOURTAI("hourtai", "小时/台"),HOURREN("hourren", "小时/人"),TIME("time","次");
	
	private String value;
	private String key;
	
	private CostTypeEnum(String key,String value) {
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
