package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum ModelType {
	USER("USER"),
	TIVITY("TIVITY"),
	LOCATION("LOCATION");

	private String type;

	private ModelType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
