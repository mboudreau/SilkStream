package com.silkstream.platform.models.dto.freegeoip;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IPLocation {
	private String ip;
	private String city;
	private String latitude;
	private String longitude;
	private String region_code;
	private String region_name;
	private String metrocode;
	private String zipcode;
	private String country_code;
	private String country_name;

	public String getIp() {
		return ip;
	}

	public String getRegion_name() {
		return region_name;
	}

	public String getMetrocode() {
		return metrocode;
	}

	public String getZipcode() {
		return zipcode;
	}

	public String getCountry_name() {
		return country_name;
	}

	public String getCity() {
		return city;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getRegion_code() {
		return region_code;
	}

	public String getCountry_code() {
		return country_code;
	}
}
