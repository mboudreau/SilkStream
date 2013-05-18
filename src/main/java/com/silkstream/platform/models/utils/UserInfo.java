package com.silkstream.platform.models.utils;

import com.amazonaws.services.dynamodb.datamodeling.*;
import com.silkstream.platform.models.marshallers.GeoDateMarshaller;

public class UserInfo {
	private String level;
	private String activity;
	private GeoDate geoDate;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@DynamoDBMarshalling(marshallerClass = GeoDateMarshaller.class)
	public GeoDate getGeoDate() {
		return geoDate;
	}

	public void setGeoDate(GeoDate geoDate) {
		this.geoDate = geoDate;
	}


}
