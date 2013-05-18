package com.silkstream.platform.models.dto.google.geocode;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleGeocodeGeometryBounds {
	private GoogleGeocodeGeometryLocation northeast;
	private GoogleGeocodeGeometryLocation southwest;

	public GoogleGeocodeGeometryLocation getNortheast() {
		return northeast;
	}

	public GoogleGeocodeGeometryLocation getSouthwest() {
		return southwest;
	}

	public void setNortheast(GoogleGeocodeGeometryLocation northeast) {
		this.northeast = northeast;
	}

	public void setSouthwest(GoogleGeocodeGeometryLocation southwest) {
		this.southwest = southwest;
	}
}
