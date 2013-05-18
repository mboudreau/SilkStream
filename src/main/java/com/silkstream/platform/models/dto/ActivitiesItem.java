package com.silkstream.platform.models.dto;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;

import java.util.List;

public class ActivitiesItem extends DTO{
	private List<String> activities;

	public ActivitiesItem() {
	}

	public ActivitiesItem(List<String> activities) {
		setActivities(activities);
	}
	
	public List<String> getActivities() {
		return activities;
	}

	public void setActivities(List<String> activities) {
		this.activities = activities;
	}
}
