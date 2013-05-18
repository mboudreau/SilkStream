package com.silkstream.platform.models.marshallers.enums;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.ActionType;

public class ActionTypeMarshaller implements DynamoDBMarshaller<ActionType> {

	public String marshall(ActionType value) {
		return value.toString();
	}

	public ActionType unmarshall(Class<ActionType> clazz, String obj) {
		obj = obj.replace("\\","");
		obj = obj.replace("\"","");
		return ActionType.valueOf(obj);
	}
}
