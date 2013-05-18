package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum RSVPType {
	YES("YES"),
	MAYBE("MAYBE"),
	NO("NO");

	private String type;

	@JsonCreator
	public static RSVPType create(String str) {
	    return RSVPType.valueOf(str);
	}

	private RSVPType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
