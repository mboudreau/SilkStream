package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum WeekDay {
	MONDAY("MONDAY"),
	TUESDAY("TUESDAY"),
	WEDNESDAY("WEDNESDAY"),
	THURSDAY("THURSDAY"),
	FRIDAY("FRIDAY"),
	SATURDAY("SATURDAY"),
	SUNDAY("SUNDAY");

	private String type;

	private WeekDay(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
