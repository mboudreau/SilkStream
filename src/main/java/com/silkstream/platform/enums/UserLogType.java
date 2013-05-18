package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum UserLogType {
	ANSWER("ANSWER"),
	REQUEST("REQUEST"),
	COMMENT("COMMENT"),
	LOCATION("LOCATION");

	private String type;

	private UserLogType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
