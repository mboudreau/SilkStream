package com.silkstream.platform.models.marshallers.enums;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;
import com.silkstream.platform.enums.PaymentType;

public class PaymentTypeMarshaller implements DynamoDBMarshaller<PaymentType> {

	public String marshall(PaymentType value) {
		return value.toString();
	}

	public PaymentType unmarshall(Class<PaymentType> clazz, String obj) {
		obj = obj.replace("\\","");
		obj = obj.replace("\"","");
		return PaymentType.valueOf(obj);
	}
}
