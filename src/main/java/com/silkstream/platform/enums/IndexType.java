package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum IndexType {
	ADD("add"),
	DELETE("delete"),
	UPDATE("update");

	private String type;

	private IndexType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}

	/*@JsonValue
		public String value() { return type; }*/

}
