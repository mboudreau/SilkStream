package com.silkstream.platform.models.marshallers.list;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.ModelType;
import com.silkstream.platform.models.utils.Followed;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ListFollowedMarshaller implements DynamoDBMarshaller<List<Followed>> {

	public String marshall(List<Followed> getterReturnResult) {
		String str = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			str = mapper.writeValueAsString(getterReturnResult);
		} catch (Exception e) {

		}
		return str;
	}

	public List<Followed> unmarshall(Class<List<Followed>> clazz, String obj) {

		List<Object> f = null;
		List<Followed> res = new ArrayList<Followed>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			f = mapper.readValue(obj, List.class);
			for(Object item : f){
				LinkedHashMap<String,Object> currentToken = (LinkedHashMap<String,Object>) item;
				Followed followed = new Followed();
				followed.setId((String) currentToken.get("id"));
				currentToken =(LinkedHashMap<String,Object>) currentToken.get("modelType");
				followed.setModelType(ModelType.valueOf((String) currentToken.get("type")));
				res.add(followed);
			}
		} catch (Exception e) {

		}
		return res;
	}
}
