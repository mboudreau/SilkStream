package com.silkstream.platform.models.marshallers;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Map;

public class MapStringStringMarshaller implements DynamoDBMarshaller<Map<String, String>> {

	public String marshall(Map<String, String> object) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(object);
		} catch (Exception e) {

		}
		return str;
	}

	public Map<String, String> unmarshall(Class<Map<String, String>> clazz, String obj) {

		Map<String, String> f = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			f = mapper.readValue(obj, clazz);
		} catch (Exception e) {

		}
		return f;
	}
}
