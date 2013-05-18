package com.silkstream.platform.models.dto;

import com.silkstream.platform.models.db.Tivity;
import com.silkstream.platform.models.utils.GeoLocation;

import java.util.Date;
import java.util.List;

public class RequestItem extends DTO {
	private String id;
	private UserItem user;
	private String activity;
	private GeoLocation geoLocation;
	private List<Tivity> tivityList;
	private String message;
	private Long rating;
	private Date createdDate;
	private Date updateDate;

	public RequestItem() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserItem getUser() {
		return user;
	}

	public void setUser(UserItem user) {
		this.user = user;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<Tivity> getTivityList() {
		return tivityList;
	}

	public void setTivityList(List<Tivity> tivityList) {
		this.tivityList = tivityList;
	}
}
