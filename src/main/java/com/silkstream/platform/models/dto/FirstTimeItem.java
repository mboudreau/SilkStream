package com.silkstream.platform.models.dto;

import com.silkstream.platform.enums.WeekDay;
import com.silkstream.platform.models.utils.AvailabilityDay;
import com.silkstream.platform.models.utils.GeoLocation;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.HashMap;
import java.util.List;

public class FirstTimeItem extends DTO {
	private String id;
	private List<String> activities;
	private List<GeoLocation> locations;
	private HashMap<WeekDay, AvailabilityDay> availability;
	private List<UserItem> friends;
	private String message;

	public FirstTimeItem() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getActivities() {
		return activities;
	}

	public void setActivities(List<String> activities) {
		this.activities = activities;
	}

	public List<GeoLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<GeoLocation> locations) {
		this.locations = locations;
	}

	public HashMap<WeekDay, AvailabilityDay> getAvailability() {
		return availability;
	}

	@JsonDeserialize(as = HashMap.class, keyAs = WeekDay.class, contentAs = AvailabilityDay.class)
	public void setAvailability(HashMap<WeekDay, AvailabilityDay> availability) {
		this.availability = availability;
	}

	public List<UserItem> getFriends() {
		return friends;
	}

	public void setFriends(List<UserItem> friends) {
		this.friends = friends;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
