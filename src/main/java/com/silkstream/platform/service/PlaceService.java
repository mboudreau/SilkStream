package com.silkstream.platform.service;

import com.silkstream.platform.enums.SearchIndexType;
import com.silkstream.platform.exception.ApplicationException;
import com.silkstream.platform.models.db.Place;
import com.silkstream.platform.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("locationService")
public class PlaceService extends FollowService {
	@Inject
	private CloudSearchService cloudSearchService;
	@Inject
	private UserService userService;
	@Inject
	private GoogleService googleService;

	public Place get(String id) throws ApplicationException {
		return get(id, null);
	}

	public Place get(String id, List<String> attributesToGet) throws ApplicationException {
		if (id != null) {
			Place place = mapper.load(Place.class, id, attributesToGet);
			if (place != null) {
				return place;
			}
		}
		return null;
	}

	public void delete(String id) {
		if (id != null) {
			Place loc = get(id);
			if (loc != null) {
				mapper.delete(loc);
			}
		}
	}


	public void update(Place place) {
		if (place != null) {
			mapper.save(place);
			index(place);
		}
	}

	public Place add(Place place) {
		if (place != null) {
			place.setCreatedDate(System.currentTimeMillis());
			place.setUpdatedDate(System.currentTimeMillis());
			place.setId(createId());
			mapper.clobber(place);
			if (place.getGeoLocation() != null) {
				index(place);
			}
			return place;
		}
		return null;
	}

	public Place createFromGoogleReferenceId(String id) {
		// Get place from Google
		Place googlePlace = googleService.getPlaceFromGoogleReferenceId(id);
		// Check if it already exists in database
		Place place = getFromGoogleId(googlePlace.getGoogleId());
		if (StringUtil.isNotNullOrEmpty(id) && place == null) {
			return add(googlePlace);
		}
		return place;
	}

	public Place getFromGoogleId(String id) {
		List<Place> places = mapper.scanWith(Place.class, "gi", id);
		if (places.size() != 0) {
			return places.get(0);
		}
		return null;
	}

	private void index(Place place) {
		String activities = place.getActivities() != null ? StringUtils.join(place.getActivities(), ",") : null;
		cloudSearchService.addItem(
				SearchIndexType.LOCATION,
				place.getId(),
				null,
				place.getName(),
				activities,
				place.getGeoLocation().getLat(),
				place.getGeoLocation().getLon(),
				place.getCreatedDate(),
				place.getUpdatedDate(),
				place.isPromoted(),
				place.getFavoriteCount()
		);
	}


}