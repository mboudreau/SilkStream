package com.silkstream.platform.models.marshallers;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.models.db.Place;
import org.codehaus.jackson.map.ObjectMapper;

public class PlaceMarshaller implements DynamoDBMarshaller<Place> {

	public String marshall(Place getterReturnResult) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(getterReturnResult);
		} catch (Exception e) {

		}
		return str;
	}

	public Place unmarshall(Class<Place> clazz, String obj) {

		Place f = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			f = mapper.readValue(obj, clazz);
		} catch (Exception e) {

		}
		return f;
	}
}
