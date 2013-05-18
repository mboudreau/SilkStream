package com.silkstream.platform.models.dto;

import com.silkstream.platform.models.utils.GeoLocation;

import java.util.ArrayList;
import java.util.List;


public class GoogleResult {
	private List<GeoLocation> geoLocationList = new ArrayList<GeoLocation>();
	private List<PlaceItem> locationItemList = new ArrayList<PlaceItem>();
	private List<String> stringList = new ArrayList<String>();

	public List<GeoLocation> getGeoLocationList() {
		return geoLocationList;
	}

	public void setGeoLocationList(List<GeoLocation> geoLocationList) {
		this.geoLocationList = geoLocationList;
	}

	public List<String> getStringList() {
		return stringList;
	}

	public void setStringList(List<String> stringList) {
		this.stringList = stringList;
	}

	public List<PlaceItem> getLocationItemList() {
		return locationItemList;
	}

	public void setLocationItemList(List<PlaceItem> locationItemList) {
		this.locationItemList = locationItemList;
	}
}
