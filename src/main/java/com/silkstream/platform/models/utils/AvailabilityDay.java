package com.silkstream.platform.models.utils;


import com.silkstream.platform.enums.WeekDay;
import com.silkstream.platform.models.dto.DTO;

import java.util.List;

public class AvailabilityDay extends DTO {
	private WeekDay day;
	private List<Hours> hours;

	public WeekDay getDay() {
		return day;
	}

	public void setDay(WeekDay day) {
		this.day = day;
	}

	public List<Hours> getHours() {
		return hours;
	}

	public void setHours(List<Hours> hours) {
		this.hours = hours;
	}
}
