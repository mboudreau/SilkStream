package com.silkstream.platform.models.dto.meetup;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetupGroups {
	private List<MeetupGroupsApiData> results;
	private MeetupMeta meta;

	public List<MeetupGroupsApiData> getResults() {
		return results;
	}

	public void setResults(List<MeetupGroupsApiData> results) {
		this.results = results;
	}

	public MeetupMeta getMeta() {
		return meta;
	}

	public void setMeta(MeetupMeta meta) {
		this.meta = meta;
	}
}
