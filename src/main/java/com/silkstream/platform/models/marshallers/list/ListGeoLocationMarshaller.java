package com.silkstream.platform.models.marshallers.list;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.models.utils.GeoLocation;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;

public class ListGeoLocationMarshaller implements DynamoDBMarshaller<List<GeoLocation>> {

	public String marshall(List<GeoLocation> getterReturnResult) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(getterReturnResult);
		} catch (Exception e) {

		}
		return str;
	}

	public List<GeoLocation> unmarshall(Class<List<GeoLocation>> clazz, String obj) {

		List<GeoLocation> f = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			f = mapper.readValue(obj, clazz);
		} catch (Exception e) {

		}
		return f;
	}
}
