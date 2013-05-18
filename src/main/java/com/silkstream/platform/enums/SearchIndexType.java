package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum SearchIndexType {
	ANSWER("ANSWER"),
	REQUEST("REQUEST"),
	COMMENT("COMMENT"),
	NOTIFICATION("NOTIFICATION"),
	USER("USER"),
	ALL("*"),
	LOG("LOG"),
	LOCATION("LOCATION");

	private String type;

	private SearchIndexType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
