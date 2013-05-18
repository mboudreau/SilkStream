package com.silkstream.platform.models.utils;

// Start time and end time are both minutes since midnight, 0 being midnight
public class Hours {
	// Not sure if long is needed.  Max number is 24*60-1
	private Long startTime;
	private Long endTime;

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
}
