package com.silkstream.platform.models.dto.google.placedetail;

import com.silkstream.platform.models.dto.google.geocode.GoogleGeocodeGeometry;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleDetailResult {
	private String formatted_address;
	private String formatted_phone_number;
    private GoogleGeocodeGeometry geometry;
	private String name;
	private String website;
	private String id;
	private String vicinity;
	private String reference;
	private Long rating;
	private List<GoogleComment> reviews;
	private List<GoogleDetailAddressComponents> address_components;

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public String getFormatted_phone_number() {
		return formatted_phone_number;
	}

	public void setFormatted_phone_number(String formatted_phone_number) {
		this.formatted_phone_number = formatted_phone_number;
	}

	public GoogleGeocodeGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(GoogleGeocodeGeometry geometry) {
		this.geometry = geometry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<GoogleDetailAddressComponents> getAddress_components() {
		return address_components;
	}

	public void setAddress_components(List<GoogleDetailAddressComponents> address_components) {
		this.address_components = address_components;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public List<GoogleComment> getReviews() {
		return reviews;
	}

	public void setReviews(List<GoogleComment> reviews) {
		this.reviews = reviews;
	}
}
