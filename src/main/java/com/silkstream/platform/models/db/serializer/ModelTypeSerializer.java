package com.silkstream.platform.models.db.serializer;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import com.silkstream.platform.enums.ModelType;

import java.io.IOException;

public class ModelTypeSerializer extends JsonSerializer<ModelType> {

	@Override
	public void serialize(ModelType value, JsonGenerator generator,
	                      SerializerProvider provider) throws IOException,
			JsonProcessingException {

		generator.writeStartObject();
		generator.writeFieldName("type");
		generator.writeString(value.toString());
		generator.writeEndObject();
	}
}
