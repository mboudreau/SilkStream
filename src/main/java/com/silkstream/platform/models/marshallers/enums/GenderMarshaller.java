package com.silkstream.platform.models.marshallers.enums;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.GenderType;

public class GenderMarshaller implements DynamoDBMarshaller<GenderType> {

	public String marshall(GenderType value) {
		return value.toString();
	}

	public GenderType unmarshall(Class<GenderType> clazz, String obj) {
		obj = obj.replace("\\","");
		obj = obj.replace("\"","");
		return GenderType.valueOf(obj);
	}
}
