package com.silkstream.platform.models.dto.google.geocode;

import java.util.List;

public class GoogleReverseGeocodeResult {
	private List<GoogleReverseGeocode> results;
	private String status;

	public List<GoogleReverseGeocode> getResults() {
		return results;
	}

	public String getStatus() {
		return status;
	}

	public void setResults(List<GoogleReverseGeocode> results) {
		this.results = results;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
