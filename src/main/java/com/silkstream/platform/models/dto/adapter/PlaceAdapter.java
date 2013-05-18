package com.silkstream.platform.models.dto.adapter;

import com.silkstream.platform.models.db.Place;
import com.silkstream.platform.models.dto.PlaceItem;
import org.apache.commons.lang.StringUtils;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("locationAdapter")
public class PlaceAdapter {

	@Inject
	UserAdapter userAdapter;

	public List<PlaceItem> buildCompletePlaceItem(List<Place> value) {
		List<PlaceItem> locations = new ArrayList<PlaceItem>();
		for (Place location : value) {
			locations.add(buildCompletePlaceItem(location));
		}
		return locations;
	}

	public PlaceItem buildCompletePlaceItem(Place place) {
		PlaceItem placeItem = new PlaceItem();

		if (place != null) {
			placeItem.setId(place.getId());
			placeItem.setName(place.getName());
			placeItem.setDescription(place.getDescription());
			placeItem.setGeoLocation(place.getGeoLocation());
			placeItem.setActivities(place.getActivities());
			placeItem.setGoogleReference(place.getGoogleReference());
			placeItem.setAverageRating(place.getAverageRating());
			placeItem.setGoogleId(place.getGoogleId());
			placeItem.setPhone(place.getPhone());
			placeItem.setPromoted(place.isPromoted());
			placeItem.setFavoriteCount(place.getFavoriteCount());
			placeItem.setUrl(place.getUrl());
			if (place.getCreatedDate() != null) {
				placeItem.setCreatedDate(new Date(place.getCreatedDate()));
			}
			if (place.getUpdatedDate() != null) {
				placeItem.setUpdatedDate(new Date(place.getUpdatedDate()));
			}
		}

		return placeItem;
	}

	public List<PlaceItem> buildPartialPlaceItem(List<Place> value) {
		List<PlaceItem> locations = new ArrayList<PlaceItem>();
		for (Place place : value) {
			locations.add(buildPartialPlaceItem(place));
		}
		return locations;
	}

	public PlaceItem buildPartialPlaceItem(Place place) {
		PlaceItem locationItem = new PlaceItem();

		if (place != null) {
			locationItem.setId(place.getId());
			locationItem.setName(place.getName());
			locationItem.setGeoLocation(place.getGeoLocation());
			locationItem.setActivities(place.getActivities());
			locationItem.setPromoted(place.isPromoted());
		}

		return locationItem;
	}

	public Place buildCompletePlace(PlaceItem placeItem) {
		if (placeItem != null) {
			Place place = new Place();
			place.setId(placeItem.getId());
			place.setName(StringUtils.trimToNull(placeItem.getName()));
			if (placeItem.getDescription() != null) {
				place.setDescription(StringUtils.trimToNull(placeItem.getDescription()));
			}
			if (placeItem.getPhone() != null) {
				LinkedList<String> numbers = new LinkedList<String>();
				Pattern p = Pattern.compile("\\d+");
				Matcher m = p.matcher(placeItem.getPhone());
				while (m.find()) {
					numbers.add(m.group());
				}
				StringBuilder stringBuilder = new StringBuilder();
				for (String st : numbers) {
					stringBuilder.append(st);
				}
				place.setPhone(StringUtils.trimToNull(stringBuilder.toString()));
			}
			if (placeItem.getUrl() != null) {
				place.setUrl(StringUtils.trimToNull(placeItem.getUrl()));
			}
			place.setCreatedDate(System.currentTimeMillis());
			place.setUpdatedDate(System.currentTimeMillis());
			place.setGoogleReference(placeItem.getGoogleReference());
			place.setAverageRating(placeItem.getAverageRating());
			place.setGeoLocation(placeItem.getGeoLocation());
			place.setActivities(placeItem.getActivities());
			place.setGoogleId(placeItem.getGoogleId());
			place.setFavoriteCount(placeItem.getFavoriteCount());
			place.setPromoted(placeItem.isPromoted());
			return place;
		}
		return null;
	}

}
