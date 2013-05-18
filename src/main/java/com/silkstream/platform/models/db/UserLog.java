package com.silkstream.platform.models.db;

import com.amazonaws.services.dynamodb.datamodeling.*;
import com.silkstream.platform.enums.ActionType;
import com.silkstream.platform.enums.UserLogType;
import com.silkstream.platform.models.marshallers.enums.ActionTypeMarshaller;
import com.silkstream.platform.models.marshallers.enums.UserLogTypeMarshaller;

@DynamoDBTable(tableName = "UserLog")
public class UserLog extends DynamoDBModel {
	private String id;
	private String userid;
	private Long reputationChange;
	private Long createdDate;
	private UserLogType target;
	private String targetId;
	private ActionType action;

	public UserLog() {

	}

	@DynamoDBHashKey(attributeName = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "cr")
	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	@DynamoDBAttribute(attributeName = "ui")
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@DynamoDBAttribute(attributeName = "rc")
	public Long getReputationChange() {
		return reputationChange;
	}

	public void setReputationChange(Long reputationChange) {
		this.reputationChange = reputationChange;
	}

	@DynamoDBMarshalling(marshallerClass = UserLogTypeMarshaller.class)
	@DynamoDBAttribute(attributeName = "ta")
	public UserLogType getTarget() {
		return target;
	}

	public void setTarget(UserLogType target) {
		this.target = target;
	}

	@DynamoDBAttribute(attributeName = "ti")
	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	@DynamoDBMarshalling(marshallerClass = ActionTypeMarshaller.class)
	@DynamoDBAttribute(attributeName = "at")
	public ActionType getAction() {
		return action;
	}

	public void setAction(ActionType action) {
		this.action = action;
	}
}
