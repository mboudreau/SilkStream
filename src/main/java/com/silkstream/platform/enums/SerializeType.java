package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum SerializeType {
	NORMAL("activity"),
	ARRAY("venue"),
	PLACE("venuespace");

	private String type;

	private SerializeType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
