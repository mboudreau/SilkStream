package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum FacebookPermission {
	INSTALLED("INSTALLED"),
	EMAIL("EMAIL"),
	PUBLISH_ACTIONS("PUBLISH_ACTIONS");

	private String type;

	private FacebookPermission(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
