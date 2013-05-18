package com.silkstream.platform.models.dto.google.place;

import com.silkstream.platform.models.dto.DTO;

import java.util.List;

public class GooglePlaceAutocompleteResult extends DTO {
	private List<GooglePlaceAutocompleteItem> predictions;
	private String status;

	public List<GooglePlaceAutocompleteItem> getPredictions() {
		return predictions;
	}

	public void setPredictions(List<GooglePlaceAutocompleteItem> predictions) {
		this.predictions = predictions;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
