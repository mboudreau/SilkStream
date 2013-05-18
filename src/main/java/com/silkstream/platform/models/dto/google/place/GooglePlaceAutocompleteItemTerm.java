package com.silkstream.platform.models.dto.google.place;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlaceAutocompleteItemTerm {
	private Integer offset;
	private String value;

	public Integer getOffset() {
		return offset;
	}

	public String getValue() {
		return value;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
