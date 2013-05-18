package com.silkstream.platform.models.utils;

import com.silkstream.platform.models.dto.DTO;

import java.math.BigDecimal;

public class GeoLocation extends DTO {
	private String address1;
	private String address2;
	private String address3;
	private String formatted_address;
	private String city;
	private String state;
	private String country;
	private String zip;
	private BigDecimal lat;
	private BigDecimal lon;
	private Boolean precise;

	public GeoLocation() {

	}

	public GeoLocation(BigDecimal lat, BigDecimal lon) {
		setLat(lat);
		setLon(lon);
	}

	public GeoLocation(String address1, String address2, String address3, String city, String state, String country, String zip, BigDecimal lat, BigDecimal lon) {
		setAddress1(address1);
		setAddress2(address2);
		setAddress3(address3);
		setCity(city);
		setState(state);
		setCountry(country);
		setZip(zip);
		setLat(lat);
		setLon(lon);
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getFormattedAddress() {
		return formatted_address;
	}

	public void setFormattedAddress(String address) {
		this.formatted_address = address;
	}

	public Boolean getPrecise() {
		return precise;
	}

	public void setPrecise(Boolean precise) {
		this.precise = precise;
	}
}
