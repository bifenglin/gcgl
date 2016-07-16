package com.mvc.common;

public enum PermissionEnum {
	CREATER("1", "创建者"), ADMIN("2", "管理员"), USER("3", "用户"), APPLICANT("0",
			"申请者"), TOP("4", "上级部门");

	private String key;
	private String value;

	private PermissionEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String keyToValue(String key) {
		if (key.equals(PermissionEnum.ADMIN.getKey())) {
			return PermissionEnum.ADMIN.getValue();
		} else if (PermissionEnum.CREATER.getKey().equals(key)) {
			return PermissionEnum.CREATER.getValue();
		} else if (PermissionEnum.USER.getKey().equals(key)) {
			return PermissionEnum.USER.getValue();
		} else if (PermissionEnum.APPLICANT.getKey().equals(key)) {
			return PermissionEnum.APPLICANT.getValue();
		}
		return null;
	}

}
