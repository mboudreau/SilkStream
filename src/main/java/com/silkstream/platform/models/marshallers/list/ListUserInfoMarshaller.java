package com.silkstream.platform.models.marshallers.list;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.models.utils.UserInfo;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;

public class ListUserInfoMarshaller implements DynamoDBMarshaller<List<UserInfo>> {

	public String marshall(List<UserInfo> getterReturnResult) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(getterReturnResult);
		} catch (Exception e) {

		}
		return str;
	}

	public List<UserInfo> unmarshall(Class<List<UserInfo>> clazz, String obj) {

		List<UserInfo> f = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			f = mapper.readValue(obj, clazz);
		} catch (Exception e) {

		}
		return f;
	}
}
