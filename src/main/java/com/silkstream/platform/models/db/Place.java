package com.silkstream.platform.models.db;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;
import com.silkstream.platform.models.marshallers.GeoLocationMarshaller;
import com.silkstream.platform.models.marshallers.list.ListStringMarshaller;
import com.silkstream.platform.models.utils.GeoLocation;

import java.util.Set;

@DynamoDBTable(tableName = "Place")
public class Place extends DynamoDBModel {
	private String id;
	private String name;
	private String description;
	private String phone;
	private String url;
	private Long createdDate;
	private Long updatedDate;
	private String googleReference;
	private String googleId;
	private Long averageRating;
	private GeoLocation geoLocation;
	private Set<String> activities;
	private boolean promoted;
	private Integer favoriteCount;
	public Place() {
	}


	@DynamoDBHashKey(attributeName = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "de")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@DynamoDBAttribute(attributeName = "na")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DynamoDBAttribute(attributeName = "cd")
	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	@DynamoDBAttribute(attributeName = "ra")
	public Long getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Long averageRating) {
		this.averageRating = averageRating;
	}

	@DynamoDBMarshalling(marshallerClass = GeoLocationMarshaller.class)
	@DynamoDBAttribute(attributeName = "gl")
	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	@DynamoDBAttribute(attributeName = "ud")
	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	@DynamoDBMarshalling(marshallerClass = ListStringMarshaller.class)
	@DynamoDBAttribute(attributeName = "ac")
	public Set<String> getActivities() {
		return activities;
	}

	public void setActivities(Set<String> activities) {
		this.activities = activities;
	}

	@DynamoDBAttribute(attributeName = "ph")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@DynamoDBAttribute(attributeName = "ur")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@DynamoDBAttribute(attributeName = "gr")
	public String getGoogleReference() {
		return googleReference;
	}

	public void setGoogleReference(String googleReference) {
		this.googleReference = googleReference;
	}

	@DynamoDBAttribute(attributeName = "gi")
	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	@DynamoDBAttribute(attributeName = "ip")
	public boolean isPromoted() {
		return promoted;
	}

	public void setPromoted(boolean promoted) {
		this.promoted = promoted;
	}

	@DynamoDBAttribute(attributeName = "fc")
	public Integer getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(Integer favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
}
