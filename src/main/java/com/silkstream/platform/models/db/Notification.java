package com.silkstream.platform.models.db;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;
import com.silkstream.platform.enums.NotificationType;
import com.silkstream.platform.models.marshallers.enums.NotificationTypeMarshaller;

@DynamoDBTable(tableName = "Notification")
public class Notification extends DynamoDBModel {
	private String id;
	private String to;
	private String from;
	private NotificationType notificationType;
	private Long createdDate;
	private Long sendDate;
	private boolean sentFlag;

	public Notification() {

	}

	public Notification(String from, String to) {
		this.from = from;
		this.to = to;
	}


	@DynamoDBHashKey(attributeName = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "to")
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@DynamoDBAttribute(attributeName = "fr")
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@DynamoDBMarshalling(marshallerClass = NotificationTypeMarshaller.class)
	@DynamoDBAttribute(attributeName = "nt")
	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	@DynamoDBAttribute(attributeName = "cd")
	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	@DynamoDBAttribute(attributeName = "sd")
	public Long getSendDate() {
		return sendDate;
	}

	public void setSendDate(Long sendDate) {
		this.sendDate = sendDate;
	}

	@DynamoDBAttribute(attributeName = "sf")
	public boolean isSentFlag() {
		return sentFlag;
	}

	public void setSentFlag(boolean sentFlag) {
		this.sentFlag = sentFlag;
	}
}