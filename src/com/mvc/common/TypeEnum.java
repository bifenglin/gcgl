package com.mvc.common;

public enum TypeEnum {
	MACHINE("machine", "机械"),LABOUR("labour", "劳务"), PEOPLE("people", "零工"), MATERIAL("material", "材料"), OTHER("other", "其他"), RENT("rent", "租赁"), AMOUNT("amount", "量");
	
	private String value;
	private String key;
	
	private TypeEnum(String key,String value) {
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
	
	public String keyToValue(String keyString) {
		if (keyString.equals(TypeEnum.LABOUR.getKey())) {
			return TypeEnum.LABOUR.getValue();
		} else if(keyString.equals(TypeEnum.MACHINE.getKey())) {
			return TypeEnum.MACHINE.getValue();
		} else if(keyString.equals(TypeEnum.MATERIAL.getKey())) {
			return TypeEnum.MATERIAL.getValue();
		} else if(keyString.equals(TypeEnum.OTHER.getKey())) {
			return TypeEnum.OTHER.getValue();
		} else if(keyString.equals(TypeEnum.PEOPLE.getKey())) {
			return TypeEnum.PEOPLE.getValue();
		}
		return keyString;
	}
}
