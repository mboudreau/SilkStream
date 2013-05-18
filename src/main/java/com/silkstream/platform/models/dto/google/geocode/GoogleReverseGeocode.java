package com.silkstream.platform.models.dto.google.geocode;

import com.silkstream.platform.models.dto.google.placedetail.GoogleDetailAddressComponents;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleReverseGeocode {
	private List<String> types;
	private List<GoogleDetailAddressComponents> address_components;
	private String formatted_address;
	private String vicinity;
	private GoogleGeocodeGeometry geometry;

	public List<String> getTypes() {
		return types;
	}

	public List<GoogleDetailAddressComponents> getAddress_components() {
		return address_components;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public GoogleGeocodeGeometry getGeometry() {
		return geometry;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public void setAddress_components(List<GoogleDetailAddressComponents> address_components) {
		this.address_components = address_components;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public void setGeometry(GoogleGeocodeGeometry geometry) {
		this.geometry = geometry;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
}
