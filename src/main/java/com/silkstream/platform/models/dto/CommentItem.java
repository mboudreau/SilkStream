package com.silkstream.platform.models.dto;

public class CommentItem extends DTO {
	private String id;
	private UserItem user;
	private String message;
	private Long creadtedDate;
	private Long rating;
	private String targetId;
	private Long updatedDate;
	private boolean flag;

	public CommentItem() {
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCreadtedDate() {
		return creadtedDate;
	}

	public void setCreadtedDate(Long creadtedDate) {
		this.creadtedDate = creadtedDate;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
