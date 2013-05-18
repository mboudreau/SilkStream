package com.silkstream.platform.models.search.deprecated;

import com.silkstream.platform.enums.EnvironmentType;
import com.silkstream.platform.enums.IndexType;
import com.silkstream.platform.enums.ModelType;

import java.math.BigDecimal;
import java.util.Set;

public interface Indexable {
	public BigDecimal getLat();

	public BigDecimal getLon();

	public String getName();

	public ModelType getModelType();

	public EnvironmentType getEnv();

	public IndexType getType();

	public Set<String> getActivityTypes();

	public com.silkstream.platform.models.search.deprecated.index.IndexItemRequest getRequest();
}
