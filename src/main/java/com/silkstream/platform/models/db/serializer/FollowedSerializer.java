package com.silkstream.platform.models.db.serializer;

import com.silkstream.platform.models.utils.Followed;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;

public class FollowedSerializer extends SerializerBase<Followed> {

	protected FollowedSerializer(JavaType type) {
		super(type);
	}


	public void serialize(Followed value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException{

	}

}

