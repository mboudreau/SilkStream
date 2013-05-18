package com.silkstream.platform.models.marshallers;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.RSVPType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.HashMap;

public class RSVPMarshaller implements DynamoDBMarshaller<HashMap<String, RSVPType>> {

	public String marshall(HashMap<String, RSVPType> getterReturnResult) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(getterReturnResult);
		} catch (Exception e) {

		}
		return str;
	}

	public HashMap<String, RSVPType> unmarshall(Class<HashMap<String, RSVPType>> clazz, String obj) {

		HashMap<String, RSVPType> f = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			f = mapper.reader(new TypeReference<HashMap<String, RSVPType>>() { }).readValue(obj);
		} catch (Exception e) {

		}

		return f;
	}
}
