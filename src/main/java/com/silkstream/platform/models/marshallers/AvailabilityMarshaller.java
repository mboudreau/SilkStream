package com.silkstream.platform.models.marshallers;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.WeekDay;
import com.silkstream.platform.models.utils.AvailabilityDay;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;

public class AvailabilityMarshaller implements DynamoDBMarshaller<HashMap<WeekDay, AvailabilityDay>> {

	public String marshall(HashMap<WeekDay, AvailabilityDay> getterReturnResult) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(getterReturnResult);
		} catch (Exception e) {

		}
		return str;
	}

	public HashMap<WeekDay, AvailabilityDay> unmarshall(Class<HashMap<WeekDay, AvailabilityDay>> clazz, String obj) {

		HashMap<WeekDay, AvailabilityDay> f = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			f = mapper.readValue(obj, clazz);
		} catch (Exception e) {

		}
		return f;
	}
}
