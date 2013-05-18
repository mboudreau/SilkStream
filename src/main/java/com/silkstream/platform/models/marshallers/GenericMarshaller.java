package com.silkstream.platform.models.marshallers;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import org.codehaus.jackson.map.ObjectMapper;

public class GenericMarshaller<T> implements DynamoDBMarshaller<T> {

	public String marshall(T getterReturnResult) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(getterReturnResult);
		} catch (Exception e) {

		}
		return str;
	}

	public T unmarshall(Class<T> clazz, String obj) {

		T l = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			l = mapper.readValue(obj, clazz);
		} catch (Exception e) {

		}
		return l;
	}
}
