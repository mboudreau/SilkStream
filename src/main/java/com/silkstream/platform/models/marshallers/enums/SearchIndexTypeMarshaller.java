package com.silkstream.platform.models.marshallers.enums;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.SearchIndexType;

public class SearchIndexTypeMarshaller implements DynamoDBMarshaller<SearchIndexType> {

	public String marshall(SearchIndexType value) {
		return value.toString();
	}

	public SearchIndexType unmarshall(Class<SearchIndexType> clazz, String obj) {
		obj = obj.replace("\\","");
		obj = obj.replace("\"","");
		return SearchIndexType.valueOf(obj);
	}
}
