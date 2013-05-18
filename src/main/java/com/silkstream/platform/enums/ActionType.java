package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum ActionType {
	CREATE("CREATE"),
	UPDATE("UPDATE"),
	COMMENT("COMMENT"),
	FAVORITE("FAVORITE"),
	UNFAVORITE("UNFAVORITE"),
	FOLLOW("FOLLOW"),
	UNFOLLOW("UNFOLLOW"),
	RSVP_YES("RSVP_YES"),
	RSVP_MAYBE("RSVP_MAYBE"),
	RSVP_NO("RSVP_NO");

	private String type;

	private ActionType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
