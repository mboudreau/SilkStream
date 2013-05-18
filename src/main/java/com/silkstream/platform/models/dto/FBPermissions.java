package com.silkstream.platform.models.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;
import java.util.Map;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FBPermissions {
	private List<Map<String,Integer>> data;
	private Object paging;

	public List<Map<String, Integer>> getData() {
		return data;
	}

	public void setData(List<Map<String, Integer>> data) {
		this.data = data;
	}
}
