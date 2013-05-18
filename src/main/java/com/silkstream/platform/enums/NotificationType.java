package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum NotificationType {
	ADDED_TIVITY_TO_LOCATION("ADDED_TIVITY_TO_LOCATION"),
	LOCATION_FOLLOWED("LOCATION_FOLLOWED");

	private String type;

	private NotificationType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
