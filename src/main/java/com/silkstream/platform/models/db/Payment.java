package com.silkstream.platform.models.db;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;
import com.silkstream.platform.enums.PaymentType;
import com.silkstream.platform.models.marshallers.MapStringStringMarshaller;
import com.silkstream.platform.models.marshallers.enums.PaymentTypeMarshaller;

import java.util.HashMap;

@DynamoDBTable(tableName = "Payment")
public class Payment extends DynamoDBModel {
	private String id;
	private String tivityId;
	private String userId;
	private PaymentType type;
	private Float total;
	private Long createdDate;
	private HashMap<String, String> data;

	@DynamoDBHashKey(attributeName = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "ti")
	public String getTivityId() {
		return tivityId;
	}

	public void setTivityId(String tivityId) {
		this.tivityId = tivityId;
	}

	@DynamoDBAttribute(attributeName = "ui")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@DynamoDBMarshalling(marshallerClass = PaymentTypeMarshaller.class)
	@DynamoDBAttribute(attributeName = "ty")
	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}

	@DynamoDBAttribute(attributeName = "to")
	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	@DynamoDBAttribute(attributeName = "cd")
	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	@DynamoDBAttribute(attributeName = "da")
	@DynamoDBMarshalling(marshallerClass = MapStringStringMarshaller.class)
	public HashMap<String, String> getData() {
		return data;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}
}
