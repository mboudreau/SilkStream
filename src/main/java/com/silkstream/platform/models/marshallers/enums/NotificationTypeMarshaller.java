package com.silkstream.platform.models.marshallers.enums;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.NotificationType;

public class NotificationTypeMarshaller implements DynamoDBMarshaller<NotificationType> {

	public String marshall(NotificationType value) {
		return value.toString();
	}

	public NotificationType unmarshall(Class<NotificationType> clazz, String obj) {
		obj = obj.replace("\\","");
		obj = obj.replace("\"","");
		return NotificationType.valueOf(obj);
	}
}
