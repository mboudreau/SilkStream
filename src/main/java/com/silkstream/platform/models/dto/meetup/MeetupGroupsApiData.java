package com.silkstream.platform.models.dto.meetup;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetupGroupsApiData {
	private MeetupCategory category;
	private Long created;
	private String id;
	private String link;
	private Double lat;
	private Double lon;
	private MeetupOrganizer organizer;

	public MeetupCategory getCategory() {
		return category;
	}

	public void setCategory(MeetupCategory category) {
		this.category = category;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public MeetupOrganizer getOrganizer() {
		return organizer;
	}

	public void setOrganizer(MeetupOrganizer organizer) {
		this.organizer = organizer;
	}
}
