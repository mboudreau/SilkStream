/*
package com.tivity.platform.models.dto.deprecated;


import com.tivity.platform.models.db.deprecated.Activity;
import com.tivity.platform.models.db.deprecated.Venue;
import com.tivity.platform.models.db.deprecated.VenueSpace;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
	private int start;
	private int found;
	private List<Venue> venues = new ArrayList<Venue>();
	private List<VenueSpace> venueSpaces = new ArrayList<VenueSpace>();
	private List<Activity> activities = new ArrayList<Activity>();
	private List<String> activityTypes = new ArrayList<String>();

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getFound() {
		return found;
	}

	public void setFound(int found) {
		this.found = found;
	}

	public List<Venue> getVenues() {
		return venues;
	}

	public void setVenues(List<Venue> venues) {
		this.venues = venues;
	}

	public List<VenueSpace> getVenueSpaces() {
		return venueSpaces;
	}

	public void setVenueSpaces(List<VenueSpace> venueSpaces) {
		this.venueSpaces = venueSpaces;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public List<String> getActivityTypes() {
		return activityTypes;
	}

	public void setActivityTypes(List<String> activityTypes) {
		this.activityTypes = activityTypes;
	}
}
*/
