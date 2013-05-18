package com.silkstream.platform.models.marshallers.enums;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.ModelType;

public class ModelTypeMarshaller implements DynamoDBMarshaller<ModelType> {

	public String marshall(ModelType value) {
		return value.toString();
	}

	public ModelType unmarshall(Class<ModelType> clazz, String obj) {
		obj = obj.replace("\\","").replace("\"","");
		return ModelType.valueOf(obj);
	}
}
