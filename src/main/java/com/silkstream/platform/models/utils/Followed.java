package com.silkstream.platform.models.utils;

import com.silkstream.platform.enums.ModelType;
import com.silkstream.platform.models.db.serializer.ModelTypeSerializer;
import com.silkstream.platform.models.dto.DTO;
import org.codehaus.jackson.map.annotate.JsonSerialize;


public class Followed extends DTO {
	private String id;
	private ModelType modelType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonSerialize(using = ModelTypeSerializer.class)
	public ModelType getModelType() {
		return modelType;
	}

	public void setModelType(ModelType modelType) {
		this.modelType = modelType;
	}
}
