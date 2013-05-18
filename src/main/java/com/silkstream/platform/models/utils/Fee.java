package com.silkstream.platform.models.utils;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;

public class Fee {
	private Float value;
	private String currency;

	public Fee() {

	}

	public Fee(Float value, String currency) {
		this.value = value;
		this.currency = "USD";
	}

	@DynamoDBAttribute(attributeName = "v")
	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	@DynamoDBAttribute(attributeName = "c")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
