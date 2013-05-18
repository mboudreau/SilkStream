package com.silkstream.platform.models.dto;

import com.silkstream.platform.models.dto.google.placedetail.GoogleComment;
import com.silkstream.platform.models.utils.GeoLocation;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class PlaceItem extends DTO {
	private String id;
	private String name;
	private String description;
	private String phone;
	private String url;
	private String googleReference;
	private String googleId;
	private Date createdDate;
	private Date updatedDate;
	private Long averageRating;
	private GeoLocation geoLocation;
	private Set<String> activities;
	private Long googleRating;
	private List<GoogleComment> googleComments;
	private List<TivityItem> tivities;
	private Double distance;
	private Double direction;
	private boolean promoted;
	private boolean favorite;
	private Integer favoriteCount;

	public PlaceItem() {
	}

	public String getId() {
		return id;
	}

	public PlaceItem setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public PlaceItem setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public PlaceItem setDescription(String description) {
		this.description = description;
		return this;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public PlaceItem setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public PlaceItem setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
		return this;
	}

	public Long getAverageRating() {
		return averageRating;
	}

	public PlaceItem setAverageRating(Long averageRating) {
		this.averageRating = averageRating;
		return this;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public PlaceItem setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
		return this;
	}

	public Set<String> getActivities() {
		return activities;
	}

	public PlaceItem setActivities(Set<String> activities) {
		this.activities = activities;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGoogleReference() {
		return googleReference;
	}

	public void setGoogleReference(String googleReference) {
		this.googleReference = googleReference;
	}

	public Long getGoogleRating() {
		return googleRating;
	}

	public void setGoogleRating(Long googleRating) {
		this.googleRating = googleRating;
	}

	public List<GoogleComment> getGoogleComments() {
		return googleComments;
	}

	public void setGoogleComments(List<GoogleComment> googleComments) {
		this.googleComments = googleComments;
	}

	public List<TivityItem> getTivities() {
		return tivities;
	}

	public void setTivities(List<TivityItem> tivities) {
		this.tivities = tivities;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getDirection() {
		return direction;
	}

	public void setDirection(Double direction) {
		this.direction = direction;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public boolean isPromoted() {
		return promoted;
	}

	public void setPromoted(boolean promoted) {
		this.promoted = promoted;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public Integer getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(Integer favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
}
