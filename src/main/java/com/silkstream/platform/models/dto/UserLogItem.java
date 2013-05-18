package com.silkstream.platform.models.dto;

import com.silkstream.platform.enums.ActionType;
import com.silkstream.platform.enums.UserLogType;

public class UserLogItem extends DTO {
	private String id;
	private Long reputationChange;
	private Long createdDate;
	private UserLogType target;
	private String targetId;
	private ActionType action;
	private String userId;
	private String activity;
	private String description;

	public UserLogItem() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getReputationChange() {
		return reputationChange;
	}

	public void setReputationChange(Long reputationChange) {
		this.reputationChange = reputationChange;
	}

	public UserLogType getTarget() {
		return target;
	}

	public void setTarget(UserLogType target) {
		this.target = target;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public ActionType getAction() {
		return action;
	}

	public void setAction(ActionType action) {
		this.action = action;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
