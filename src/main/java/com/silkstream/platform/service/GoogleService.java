package com.silkstream.platform.service;

import com.silkstream.platform.enums.GoogleType;
import com.silkstream.platform.exception.ApplicationException;
import com.silkstream.platform.exception.GoogleServiceException;
import com.silkstream.platform.models.db.Place;
import com.silkstream.platform.models.dto.adapter.PlaceAdapter;
import com.silkstream.platform.models.dto.google.geocode.GoogleReverseGeocode;
import com.silkstream.platform.models.dto.google.geocode.GoogleReverseGeocodeResult;
import com.silkstream.platform.models.dto.google.place.GooglePlace;
import com.silkstream.platform.models.dto.google.place.GooglePlaceAutocompleteItem;
import com.silkstream.platform.models.dto.google.place.GooglePlaceAutocompleteResult;
import com.silkstream.platform.models.dto.google.place.GooglePlaceResult;
import com.silkstream.platform.models.dto.google.placedetail.GoogleDetailAddressComponents;
import com.silkstream.platform.models.dto.google.placedetail.GoogleDetailReverse;
import com.silkstream.platform.models.utils.GeoLocation;
import com.silkstream.platform.utils.StringUtil;
import org.codehaus.jackson.map.ObjectMapper;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service("googleService")
public class GoogleService extends BasicService {

	@Inject
	private PlaceAdapter placeAdapter;

	private String key = "AIzaSyBy2haGqRput2_xrB7t9EkJoOt7Gyg7yBY"; // BACK UP KEY AIzaSyDpYbyFYIDcrfkwrHqibJuGOUEQ4QOboy0 ( THOMAS'S PERSONAL KEY ) , AIzaSyBy2haGqRput2_xrB7t9EkJoOt7Gyg7yBY ( TIVITY - DEFAULT KEY ), AIzaSyD9j6qQsiPZrexRANPYkAVI_NpM1VaM6nM ( TIVITY - SECONDARY KEY )

	public String getKey() {
		return this.key;
	}

	public List<Place> getAutoCompleteFromQuery(String query) throws Exception {
		return getAutoCompleteFromQuery(query, null);
	}

	public List<Place> getAutoCompleteFromQuery(String query, GoogleType type) throws ApplicationException {
		List<Place> predictions = new ArrayList<Place>();
		type = type == null ? GoogleType.CITIES : type; // Default to city autocomplete
		GooglePlaceAutocompleteResult googlePlaceApi = getAutocompleteResults(query, type);
		if (googlePlaceApi != null) {
			for (GooglePlaceAutocompleteItem item : googlePlaceApi.getPredictions()) {
				if (filterItem(item, type)) {
					predictions.add(getPlaceFromGoogleReferenceId(item.getReference()));
				}
			}
			return predictions;
		}
		return null;
	}

	public List<Place> getPlacesFromQuery(String query, String address) throws ApplicationException {
		return getPlacesFromQuery(query, null, address);
	}

	public List<Place> getPlacesFromQuery(String query) throws ApplicationException {
		return getPlacesFromQuery(query, null, null);
	}

	public List<Place> getPlacesFromQuery(String query, GoogleType type, String address) throws ApplicationException {
		type = type == null ? GoogleType.PLACES : type;
		List<Place> predictions = new ArrayList<Place>();
		GooglePlace result = null;
		if (StringUtil.isNotNullOrEmpty(address)) {
			List<GeoLocation> locations = getGeoLocation(address);
			if (locations.size() != 0) {
				result = getNearbyPredictionsFromQuery(query, locations.get(0).getLat(), locations.get(0).getLon());
			}
		} else {
			result = getPredictionsFromQuery(query);
		}

		if (result != null) {
			for (GooglePlaceResult item : result.getResults()) {
				//if (filterItem(item, type)) {
					Place place = getPlaceFromGoogleReferenceId(item.getReference());
					place.setGoogleReference(item.getReference());
					place.setName(item.getName());
					predictions.add(place);
				//}
			}
			return predictions;
		}

		return null;
	}

	public List<GeoLocation> getGeoLocation(String address) {
		try {
			if (address != null) {
				URL targetUrl = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(address, "UTF8") + "&sensor=false");
				ObjectMapper mapper = new ObjectMapper();
				GoogleReverseGeocodeResult data = mapper.readValue(targetUrl, GoogleReverseGeocodeResult.class);
				List<GeoLocation> geoLocations = new ArrayList<GeoLocation>();
				if (data != null) {
					for (GoogleReverseGeocode item : data.getResults()) {
						geoLocations.add(parseGoogleReverseGeocode(item));
					}
				}
				return geoLocations;
			}
		} catch (Exception e) {
			throw new GoogleServiceException();
		}
		return null;
	}

	public GeoLocation getGeoLocation(BigDecimal lat, BigDecimal lon) {
		try {
			if (lat != null && lon != null) {
				URL targetUrl = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + URLEncoder.encode(lat.toString() + "," + lon.toString(), "UTF8") + "&sensor=false");
				ObjectMapper mapper = new ObjectMapper();
				GoogleReverseGeocodeResult data = mapper.readValue(targetUrl, GoogleReverseGeocodeResult.class);
				if (data != null) {
					for (GoogleReverseGeocode item : data.getResults()) {
						GeoLocation geo = parseGoogleReverseGeocode(item);
						geo.setPrecise(true);
						return geo;
					}
				}
			}
		} catch (Exception e) {
			throw new GoogleServiceException();
		}
		return null;
	}


	public Place getPlaceFromGoogleReferenceId(String id) throws ApplicationException {
		try {
			URL targetUrl = new URL("https://maps.googleapis.com/maps/api/place/details/json?reference=" + id + "&key=" + key + "&sensor=false");
			ObjectMapper mapper = new ObjectMapper();
			GoogleDetailReverse googleDetailReverse = mapper.readValue(targetUrl, GoogleDetailReverse.class);

			Place place = new Place();
			GeoLocation geoLocation = new GeoLocation();

			if (googleDetailReverse != null) {
				if (googleDetailReverse.getResult() != null) {
					place.setName(googleDetailReverse.getResult().getName());
					place.setUrl(googleDetailReverse.getResult().getWebsite());
					place.setPhone(googleDetailReverse.getResult().getFormatted_phone_number());
					place.setGoogleReference(googleDetailReverse.getResult().getReference());
					place.setGoogleId(googleDetailReverse.getResult().getId());

					if (googleDetailReverse.getResult().getGeometry() != null) {
						if (googleDetailReverse.getResult().getGeometry().getLocation() != null) {
							geoLocation.setLat(googleDetailReverse.getResult().getGeometry().getLocation().getLat());
							geoLocation.setLon(googleDetailReverse.getResult().getGeometry().getLocation().getLng());
						}
					}
					geoLocation.setFormattedAddress(googleDetailReverse.getResult().getFormatted_address());

					String locality = null;
					String state = null;
					String country = null;
					String zip = null;
					for (GoogleDetailAddressComponents g : googleDetailReverse.getResult().getAddress_components()) {
						if (g.getTypes().contains("locality") && g.getTypes().contains("political")) {
							locality = g.getLong_name();
						} else if (g.getTypes().contains("administrative_area_level_1") && g.getTypes().contains("political")) {
							state = g.getLong_name();
						} else if (g.getTypes().contains("country") && g.getTypes().contains("political")) {
							country = g.getLong_name();
						} else if (g.getTypes().contains("postal_code")) {
							zip = g.getLong_name();
						}
					}

					geoLocation.setCity(locality);
					geoLocation.setAddress1(googleDetailReverse.getResult().getVicinity());
					geoLocation.setState(state);
					geoLocation.setCountry(country);
					geoLocation.setZip(zip);
					place.setGeoLocation(geoLocation);
					return place;
				}
			}
			return null;


		} catch (Exception e) {
			throw new GoogleServiceException();
		}
	}


	public GoogleDetailReverse getGoogleInfosFromReference(String id) throws ApplicationException {
		try {
			URL targetUrl = new URL("https://maps.googleapis.com/maps/api/place/details/json?reference=" + id + "&key=" + key + "&sensor=false");
			ObjectMapper mapper = new ObjectMapper();
			GoogleDetailReverse googleDetailReverse = mapper.readValue(targetUrl, GoogleDetailReverse.class);
			return googleDetailReverse;

		} catch (Exception e) {
			throw new GoogleServiceException();
		}
	}

	public URL buildSearchUrl(String query, BigDecimal lat, BigDecimal lon) throws ApplicationException {
		try {
			if (query != null) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?name=" + URLEncoder.encode(query, "UTF8"));
				stringBuilder.append("&rankby=distance");
				/*stringBuilder.append("https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + URLEncoder.encode(query, "UTF8"));
				stringBuilder.append("&radius=50000");*/
				stringBuilder.append("&location=" + lat + "," + lon);
				stringBuilder.append("&key=" + this.key);
				stringBuilder.append("&sensor=false");
				return new URL(stringBuilder.toString());
			}
			return null;
		} catch (Exception e) {
			throw new GoogleServiceException();
		}
	}

	public URL buildSearchUrl(String query) throws ApplicationException {
		try {
			if (StringUtil.isNotNullOrEmpty(query)) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + URLEncoder.encode(query, "UTF8"));
				stringBuilder.append("&key=" + this.key);
				stringBuilder.append("&sensor=false");
				return new URL(stringBuilder.toString());
			}
			return null;
		} catch (Exception e) {
			throw new GoogleServiceException();
		}
	}

	public URL buildAutocompleteUrl(String query, GoogleType type, BigDecimal lat, BigDecimal lon) throws ApplicationException {
		try {
			if (query != null) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=");
				stringBuilder.append(URLEncoder.encode(query, "UTF8"));
				stringBuilder.append("&types=" + type.toValue());
				if (lat != null && lon != null) {
					stringBuilder.append("&location=" + lat + "," + lon);
					stringBuilder.append("&radius=" + 100000);
				}
				stringBuilder.append("&key=" + this.key);
				stringBuilder.append("&sensor=false");
				return new URL(stringBuilder.toString());
			}
			return null;
		} catch (Exception e) {
			throw new GoogleServiceException();
		}
	}

	public boolean filterItem(GooglePlaceAutocompleteItem item, GoogleType type) {
		Boolean b = false;
		if (item != null) {
			if (type.equals(GoogleType.CITIES)) {
				if (item.getTypes().contains("locality")) {
					b = true;
				} else {
					b = false;
				}
			} else if (type.equals(GoogleType.PLACES)) {
				if (/*!item.getTypes().contains("establishment") ||*/ item.getTypes().contains("locality") || item.getTypes().contains("sublocality") || item.getTypes().contains("country") || item.getTypes().contains("administrative_area1") || item.getTypes().contains("administrative_area2") || item.getTypes().contains("administrative_area3") || item.getTypes().contains("administrative_area1")) {
					b = false;
				} else {
					b = true;
				}
			}
			return b;
		}
		return false;
	}

	public GooglePlace getNearbyPredictionsFromQuery(String query, BigDecimal lat, BigDecimal lon) throws ApplicationException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(buildSearchUrl(query, lat, lon), GooglePlace.class);
		} catch (Exception e) {
			throw new GoogleServiceException();
		}
	}

	public GooglePlace getPredictionsFromQuery(String query) throws ApplicationException {
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.readValue(buildSearchUrl(query), GooglePlace.class);
			} catch (Exception e) {
				throw new GoogleServiceException();
			}
		}

	public GooglePlaceAutocompleteResult getAutocompleteResults(String query, GoogleType type) {
		return getAutocompleteResults(query, type, null, null);
	}

	public GooglePlaceAutocompleteResult getAutocompleteResults(String query, GoogleType type, BigDecimal lat, BigDecimal lon) throws ApplicationException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(buildAutocompleteUrl(query, type, lat, lon), GooglePlaceAutocompleteResult.class);
		} catch (Exception e) {
			throw new GoogleServiceException();
		}
	}

	public GeoLocation parseGoogleReverseGeocode(GoogleReverseGeocode item) {
		if (item != null) {
			GeoLocation geoLocation = new GeoLocation();
			String street_number = "";
			String route = "";
			geoLocation.setAddress1(item.getVicinity());
			if (item.getGeometry() != null) {
				if (item.getGeometry().getLocation() != null) {
					geoLocation.setLat(item.getGeometry().getLocation().getLat());
					geoLocation.setLon(item.getGeometry().getLocation().getLng());
				}
			}

			for (GoogleDetailAddressComponents components : item.getAddress_components()) {
				if (components != null) {
					if (components.getTypes() != null) {
						if (components.getTypes().contains("street_number")) {
							street_number = components.getLong_name();
						} else if (components.getTypes().contains("route")) {
							route = components.getLong_name();
						} else if (components.getTypes().contains("postal_code")) {
							geoLocation.setZip(components.getLong_name());
						} else if (components.getTypes().contains("country") && components.getTypes().contains("political")) {
							geoLocation.setCountry(components.getLong_name());
						} else if (components.getTypes().contains("administrative_area_level_1") && components.getTypes().contains("political")) {
							geoLocation.setState(components.getShort_name());
						} else if (components.getTypes().contains("locality") && components.getTypes().contains("political")) {
							geoLocation.setCity(components.getLong_name());
						}
					}
				}
			}
			if (item.getVicinity() == null) {
				geoLocation.setAddress1(street_number + " " + route);
			}
			geoLocation.setFormattedAddress(item.getFormatted_address());
			return geoLocation;
		}
		return null;
	}
}

