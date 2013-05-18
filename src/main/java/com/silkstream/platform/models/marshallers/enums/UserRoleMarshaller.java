package com.silkstream.platform.models.marshallers.enums;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.UserRole;

public class UserRoleMarshaller implements DynamoDBMarshaller<UserRole> {

	public String marshall(UserRole value) {
		return value.toString();
	}

	public UserRole unmarshall(Class<UserRole> clazz, String obj) {
		obj = obj.replace("\\","");
		obj = obj.replace("\"","");
		return UserRole.valueOf(obj);
	}
}
