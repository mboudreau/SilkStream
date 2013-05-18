package com.silkstream.platform.models.marshallers.enums;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.UserLogType;

public class UserLogTypeMarshaller implements DynamoDBMarshaller<UserLogType> {

	public String marshall(UserLogType value) {
		return value.toString();
	}

	public UserLogType unmarshall(Class<UserLogType> clazz, String obj) {
		obj = obj.replace("\\","");
		obj = obj.replace("\"","");
		return UserLogType.valueOf(obj);
	}
}
