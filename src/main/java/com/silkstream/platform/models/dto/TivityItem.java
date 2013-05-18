package com.silkstream.platform.models.dto;

import com.silkstream.platform.enums.RSVPType;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TivityItem extends DTO {
	private String id;
	private UserItem user;
	private String activity;
	private PlaceItem location;
	private String name;
	private String description;
	private Long rating;
	private Long startTime; //for event only
	private Long endTime;   //for event only
	private Long createdDate;
	private Long updateDate;
	private boolean favorite;
	private boolean promoted;
	private Integer minimum;
	private Integer maximum;
	private boolean active;
	private float cost;
	private List<CommentItem> comments;
	private Long viewCount;
	private Double distance;
	private Double direction;
	private int favoriteCount;
	private Integer commentCount;
	private Set<String> favoritedUserIds;
	private HashMap<String, RSVPType> rsvp;
	private Boolean enabled;

	public TivityItem() {
	}

	public String getId() {
		return id;
	}

	public TivityItem setId(String id) {
		this.id = id;
		return this;
	}

	public UserItem getUser() {
		return user;
	}

	public TivityItem setUser(UserItem user) {
		this.user = user;
		return this;
	}

	public String getActivity() {
		return activity;
	}

	public TivityItem setActivity(String activity) {
		this.activity = activity;
		return this;
	}

	public PlaceItem getLocation() {
		return location;
	}

	public TivityItem setLocation(PlaceItem location) {
		this.location = location;
		return this;
	}

	public String getName() {
		return name;
	}

	public TivityItem setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public TivityItem setDescription(String description) {
		this.description = description;
		return this;
	}

	public Long getRating() {
		return rating;
	}

	public TivityItem setRating(Long rating) {
		this.rating = rating;
		return this;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public TivityItem setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	public Long getUpdateDate() {
		return updateDate;
	}

	public TivityItem setUpdateDate(Long updateDate) {
		this.updateDate = updateDate;
		return this;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}


	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean value) {
		favorite = value;
	}

	public List<CommentItem> getComments() {
		return comments;
	}

	public void setComments(List<CommentItem> comments) {
		this.comments = comments;
	}

	public boolean isPromoted() {
		return promoted;
	}

	public void setPromoted(boolean promoted) {
		this.promoted = promoted;
	}

	public Integer getMinimum() {
		return minimum;
	}

	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}

	public Integer getMaximum() {
		return maximum;
	}

	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance == null ? null : Math.round(distance * 100) / 100.0d;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Double getDirection() {
		return direction;
	}

	public void setDirection(Double direction) {
		this.direction = direction == null ? null : Math.round(direction * 100) / 100.0d;
	}

	public Set<String> getFavoritedUserIds() {
		return favoritedUserIds;
	}

	public void setFavoritedUserIds(Set<String> favoritedUserIds) {
		this.favoritedUserIds = favoritedUserIds;
	}

	public HashMap<String, RSVPType> getRSVP() {
		return rsvp;
	}

	public void setRSVP(HashMap<String, RSVPType> rsvp) {
		this.rsvp = rsvp;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
