package com.silkstream.platform.models.dto.google.geocode;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleGeocodeGeometryLocation {
	private BigDecimal lat;
	private BigDecimal lng;

	public BigDecimal getLat() {
		return lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}
}
