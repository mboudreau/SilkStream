package com.silkstream.platform.models.dto;

import com.silkstream.platform.models.utils.GeoLocation;

public class RequestCreateItem extends DTO {
	private String activity;
	private String message;
	private GeoLocation geolocation;

	public RequestCreateItem() {
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public GeoLocation getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(GeoLocation geolocation) {
		this.geolocation = geolocation;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
