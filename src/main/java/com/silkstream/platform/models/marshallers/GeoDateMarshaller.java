package com.silkstream.platform.models.marshallers;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.models.utils.GeoDate;
import org.codehaus.jackson.map.ObjectMapper;

public class GeoDateMarshaller implements DynamoDBMarshaller<GeoDate> {

	public String marshall(GeoDate getterReturnResult) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(getterReturnResult);
		} catch (Exception e) {

		}
		return str;
	}

	public GeoDate unmarshall(Class<GeoDate> clazz, String obj) {

		GeoDate f = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			f = mapper.readValue(obj, clazz);
		} catch (Exception e) {

		}
		return f;
	}
}
