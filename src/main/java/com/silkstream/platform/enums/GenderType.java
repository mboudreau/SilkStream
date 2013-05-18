package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum GenderType {
	MALE("MALE"),
	FEMALE("FEMALE");

	private String type;

	private GenderType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
