package com.silkstream.platform.models.marshallers;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.models.utils.GeoLocation;
import org.codehaus.jackson.map.ObjectMapper;

public class GeoLocationMarshaller implements DynamoDBMarshaller<GeoLocation> {

	public String marshall(GeoLocation getterReturnResult) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(getterReturnResult);
		} catch (Exception e) {

		}
		return str;
	}

	public GeoLocation unmarshall(Class<GeoLocation> clazz, String obj) {

		GeoLocation f = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			f = mapper.readValue(obj, clazz);
		} catch (Exception e) {

		}
		return f;
	}
}
