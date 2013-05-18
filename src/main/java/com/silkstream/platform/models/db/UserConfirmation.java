package com.silkstream.platform.models.db;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "UserConfirmation")
public class UserConfirmation extends DynamoDBModel {
	private String confimationId;
	private String userId;
	private Long createdDate;

	@DynamoDBHashKey(attributeName = "id")
	public String getConfimationId() {
		return confimationId;
	}

	public void setConfimationId(String confimationId) {
		this.confimationId = confimationId;
	}

	@DynamoDBAttribute(attributeName = "uid")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@DynamoDBAttribute(attributeName = "cd")
	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
}
