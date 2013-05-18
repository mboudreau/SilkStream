/*
package com.tivity.platform.models.dto.deprecated;

import com.tivity.platform.models.db.deprecated.Venue;
import com.tivity.platform.models.db.deprecated.VenueSpace;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddVenue {
	private Venue venue;
	private List<VenueSpace> venueSpaces;

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public List<VenueSpace> getVenueSpaces() {
		return venueSpaces;
	}

	public void setVenueSpaces(List<VenueSpace> venueSpaces) {
		this.venueSpaces = venueSpaces;
	}

	public void build() {
		for (VenueSpace venueSpace : venueSpaces) {
			venueSpace.setVenueId(venue.getId());
		}
	}
}
*/
