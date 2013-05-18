package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum GoogleType {
	CITIES("CITIES", "(cities)"),
	PLACES("PLACES", "establishment"),
	REGIONS("REGIONS", "(regions)"),
	GEOCODE("GEOCODE", "geocode");

	private String type;
	private String value;

	private GoogleType(String type, String value) {
		this.type = type;
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return type;
	}

	public String toValue() {
		return value;
	}
}
