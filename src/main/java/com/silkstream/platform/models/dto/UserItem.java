package com.silkstream.platform.models.dto;

import com.silkstream.platform.enums.GenderType;
import com.silkstream.platform.enums.WeekDay;
import com.silkstream.platform.models.utils.AvailabilityDay;
import com.silkstream.platform.models.utils.Followed;
import com.silkstream.platform.models.utils.GeoLocation;

import java.util.HashMap;
import java.util.List;

public class UserItem extends DTO {
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String pictureUrl;
	private GenderType gender;
	private List<String> activities;
	private Long loginCount;
	private String role;
	private Long reputation;
	private Boolean enable;
	private List<GeoLocation> locations;
	private HashMap<WeekDay, AvailabilityDay> availability;
	private String facebookId;
	private List<Followed> followeds;
	private String bio;

	public UserItem() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public Long getReputation() {
		return reputation;
	}

	public void setReputation(Long reputation) {
		this.reputation = reputation;
	}

	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}

	public List<String> getActivities() {
		return activities;
	}

	public void setActivities(List<String> activities) {
		this.activities = activities;
	}

	public Long getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Long loginCount) {
		this.loginCount = loginCount;
	}

	public List<GeoLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<GeoLocation> locations) {
		this.locations = locations;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public HashMap<WeekDay, AvailabilityDay> getAvailability() {
		return availability;
	}

	public void setAvailability(HashMap<WeekDay, AvailabilityDay> availability) {
		this.availability = availability;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public List<Followed> getFolloweds() {
		return followeds;
	}

	public void setFolloweds(List<Followed> followeds) {
		this.followeds = followeds;
	}
}
