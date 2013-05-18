package com.silkstream.platform.models.dto.google.place;

import com.silkstream.platform.models.dto.DTO;
import com.silkstream.platform.models.dto.google.geocode.GoogleGeocodeGeometry;

import java.util.List;

public class GooglePlaceResult extends DTO {
	private String id;
	private String name;
	private String icon;
	private String formatted_address;
	private GoogleGeocodeGeometry geometry;
	private String reference;
	private List<String> types;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public GoogleGeocodeGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(GoogleGeocodeGeometry geometry) {
		this.geometry = geometry;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}
}

