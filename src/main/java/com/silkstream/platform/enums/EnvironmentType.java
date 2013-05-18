package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum EnvironmentType {
	LOCAL("LOCAL"),
	DEV("DEV"),
	STAGING("STAGING"),
	MAINTENANCE("MAINTENANCE"),
	TEST("TEST"),
	PROD("PROD");

	private String type;

	private EnvironmentType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
