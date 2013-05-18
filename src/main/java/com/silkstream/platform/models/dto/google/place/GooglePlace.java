package com.silkstream.platform.models.dto.google.place;

import com.silkstream.platform.models.dto.DTO;

import java.util.List;

public class GooglePlace extends DTO {
	private List<GooglePlaceResult> results;

	public List<GooglePlaceResult> getResults() {
		return results;
	}

	public void setResults(List<GooglePlaceResult> results) {
		this.results = results;
	}
}

