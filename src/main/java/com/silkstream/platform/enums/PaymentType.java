package com.silkstream.platform.enums;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum PaymentType {
	PAYPAL("PAYPAL"),
	DWOLLA("DWOLLA");

	private String type;

	@JsonCreator
	public static PaymentType create(String str) {
		return PaymentType.valueOf(str);
	}

	private PaymentType(String type) {
		this.type = type;
	}

	@JsonValue
	public String toString() {
		return type;
	}
}
