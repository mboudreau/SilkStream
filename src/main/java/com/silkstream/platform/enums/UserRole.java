package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum UserRole {
	ANON("ANON"),
	USER("USER"),
	ADMIN("ADMIN"),
	SUPERADMIN("SUPERADMIN");

	private String type;

	private UserRole(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
