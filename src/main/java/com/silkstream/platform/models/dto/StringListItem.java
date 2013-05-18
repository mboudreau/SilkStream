package com.silkstream.platform.models.dto;

import java.util.List;

public class StringListItem extends DTO{
	private List<String> values;

	public StringListItem() {
	}

	public StringListItem(List<String> values) {
		setValues(values);
	}
	
	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
}
