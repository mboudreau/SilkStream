/*
package com.tivity.platform.models.dto.deprecated;


import com.tivity.platform.annotation.URLSerialize;
import com.tivity.platform.enums.SerializeType;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.Set;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRequest {
	private BigDecimal lat;
	private BigDecimal lon;
	private String name;
	private Set<String> types;
	private Set<String> activities;
	private Integer start;
	private Integer count;
	private Integer radius;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getTypes() {
		return types;
	}

	public void setTypes(Set<String> types) {
		this.types = types;
	}

	public Set<String> getActivities() {
		return activities;
	}

	public void setActivities(Set<String> types) {
		this.activities = types;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}
}
*/
