package com.silkstream.platform.models.marshallers.list;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;

public class ListStringMarshaller implements DynamoDBMarshaller<List<String>> {

	public String marshall(List<String> object) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(object);
		} catch (Exception e) {

		}
		return str;
	}

	public List<String> unmarshall(Class<List<String>> clazz, String obj) {

		List<String> f = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			f = mapper.readValue(obj, clazz);
		} catch (Exception e) {

		}
		return f;
	}
}
