package com.silkstream.platform.models.db;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;
import com.silkstream.platform.enums.RSVPType;
import com.silkstream.platform.models.marshallers.RSVPMarshaller;

import java.util.HashMap;
import java.util.Set;

@DynamoDBTable(tableName = "Tivity")
public class Tivity extends DynamoDBModel {
	private String id;
	private String userId;
	private String name;
	private String locationId;
	private String description;
	private Long rating;
	private String activity;
	private HashMap<String, String> data;
	private Long startTime;
	private Long endTime;
	private Long createdDate;
	private Long updatedDate;
	private boolean promoted;
	private Integer minimum;
	private Integer maximum;
	private Float cost;
	private Set<String> favoritedUser;
	private HashMap<String, RSVPType> rsvp;
	private Boolean enabled;

	@DynamoDBHashKey(attributeName = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "ui")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@DynamoDBAttribute(attributeName = "na")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DynamoDBAttribute(attributeName = "cr")
	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}


	@DynamoDBAttribute(attributeName = "d")
	public String getDescription() {
		return description;
	}

	public void setDescription(String value) {
		this.description = value;
	}

	@DynamoDBAttribute(attributeName = "ra")
	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	@DynamoDBAttribute(attributeName = "ac")
	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@DynamoDBAttribute(attributeName = "lo")
	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String location) {
		this.locationId = location;
	}

	@DynamoDBAttribute(attributeName = "da")
	public HashMap<String, String> getData() {
		return data;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}

	@DynamoDBAttribute(attributeName = "ud")
	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	@DynamoDBAttribute(attributeName = "st")
	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	@DynamoDBAttribute(attributeName = "et")
	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@DynamoDBAttribute(attributeName = "pr")
	public boolean getPromoted() {
		return promoted;
	}

	public void setPromoted(boolean promoted) {
		this.promoted = promoted;
	}

	@DynamoDBAttribute(attributeName = "mi")
	public Integer getMinimum() {
		return minimum;
	}

	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}

	@DynamoDBAttribute(attributeName = "ma")
	public Integer getMaximum() {
		return maximum;
	}

	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}

	@DynamoDBAttribute(attributeName = "co")
	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	@DynamoDBMarshalling(marshallerClass = RSVPMarshaller.class)
	@DynamoDBAttribute(attributeName = "rs")
	public HashMap<String, RSVPType> getRSVP() {
		return rsvp;
	}

	public void setRSVP(HashMap<String, RSVPType> value) {
		this.rsvp = value;
	}

	@DynamoDBAttribute(attributeName = "fu")
	public Set<String> getFavoritedUser() {
		return favoritedUser;
	}

	public void setFavoritedUser(Set<String> favoritedUser) {
		this.favoritedUser = favoritedUser;
	}

	@DynamoDBAttribute(attributeName = "en")
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
