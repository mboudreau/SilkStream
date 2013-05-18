package com.silkstream.platform.models.dto.google.geocode;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleGeocodeGeometry {
	private GoogleGeocodeGeometryBounds bounds;
	private GoogleGeocodeGeometryLocation location;
	private String location_type;
	private GoogleGeocodeGeometryBounds viewport;

	public GoogleGeocodeGeometryBounds getBounds() {
		return bounds;
	}

	public GoogleGeocodeGeometryLocation getLocation() {
		return location;
	}

	public String getLocation_type() {
		return location_type;
	}

	public GoogleGeocodeGeometryBounds getViewport() {
		return viewport;
	}

	public void setBounds(GoogleGeocodeGeometryBounds bounds) {
		this.bounds = bounds;
	}

	public void setLocation(GoogleGeocodeGeometryLocation location) {
		this.location = location;
	}

	public void setLocation_type(String location_type) {
		this.location_type = location_type;
	}

	public void setViewport(GoogleGeocodeGeometryBounds viewport) {
		this.viewport = viewport;
	}
}
