package com.silkstream.platform.models.dto;

import com.silkstream.platform.models.utils.GeoLocation;

import java.util.List;

public class PlacesItem extends DTO{
	private List<GeoLocation> locations;

	public PlacesItem() {
	}

	public PlacesItem(List<GeoLocation> locations) {
		setLocations(locations);
	}
	
	public List<GeoLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<GeoLocation> locations) {
		this.locations = locations;
	}
}
