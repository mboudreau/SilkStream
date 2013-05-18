package com.silkstream.platform.service;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.javadocmd.simplelatlng.window.RectangularWindow;
import com.silkstream.platform.enums.IndexType;
import com.silkstream.platform.enums.SearchIndexType;
import com.silkstream.platform.models.BeanstalkProperties;
import com.silkstream.platform.models.dto.SearchResults;
import com.silkstream.platform.models.dto.google.ResultInfo;
import com.silkstream.platform.models.search.IndexItemRequest;
import com.silkstream.platform.models.search.IndexItemResponse;
import com.silkstream.platform.models.search.deprecated.search.SearchHit;
import com.silkstream.platform.models.search.deprecated.search.SearchResponse;
import javax.inject.Inject;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cloudSearchService")
public class CloudSearchService extends BasicService {
	@Inject
	protected BeanstalkProperties properties;

	public IndexItemResponse addItem(SearchIndexType type, String id, String userId, String name, String activity, BigDecimal lat, BigDecimal lon, Long createdDate, Long updatedDate) {
		return addItem(type, id, userId, name, activity, lat, lon, createdDate, updatedDate, null, null, null);
	}

	public IndexItemResponse addItem(SearchIndexType type, String id, String userId, String name, String activity, BigDecimal lat, BigDecimal lon, Long createdDate, Long updatedDate, Boolean promoted) {
		return addItem(type, id, userId, name, activity, lat, lon, createdDate, updatedDate, promoted, null, null);
	}

	public IndexItemResponse addItem(SearchIndexType type, String id, String userId, String name, String activity, BigDecimal lat, BigDecimal lon, Long createdDate, Long updatedDate, Boolean promoted, Integer points) {
		return addItem(type, id, userId, name, activity, lat, lon, createdDate, updatedDate, promoted, points, null);
	}

	public IndexItemResponse addItem(SearchIndexType type, String id, String userId, String name, String activity, BigDecimal lat, BigDecimal lon, Long createdDate, Long updatedDate, Boolean promoted, Integer points, Boolean enabled) {
		if (type == null || id == null) {
			return null;
		}
		enabled = (enabled != null) ? enabled : true;
		promoted = (promoted != null) ? promoted : false;
		IndexItemRequest indexItemRequest = new IndexItemRequest();
		indexItemRequest.setId(id);
		indexItemRequest.setType(IndexType.ADD);
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("type", type);
		fields.put("env", properties.getEnvironment().toString());
		if (userId != null) {
			fields.put("userid", userId);
		}
		if (activity != null) {
			fields.put("activity", activity);
		}
		if (name != null) {
			fields.put("name", name);
		}
		if (lat != null && lon != null) {
			fields.put("lat", (long) ((90 + lat.doubleValue()) * 100000));
			fields.put("lon", (long) ((180 + lon.doubleValue()) * 100000));
		}
		if (promoted != null) {
			if (promoted) {
				fields.put("promoted", 1);
			} else {
				fields.put("promoted", 0);
			}
		}
		if (enabled != null) {
			if (enabled) {
				fields.put("enabled", 1);
			} else {
				fields.put("enabled", 0);
			}
		}
		if (points != null) {
			fields.put("points", points);
		}

		if (createdDate != null) {
			createdDate = createdDate / 1000;
		}
		if (updatedDate != null) {
			updatedDate = updatedDate / 1000;
		}
		if (createdDate != null) {
			fields.put("created", createdDate);
		}
		if (updatedDate != null) {
			fields.put("updated", updatedDate);
		}
		indexItemRequest.setFields(fields);
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJacksonHttpMessageConverter());
		restTemplate.setMessageConverters(list);
		List<IndexItemRequest> list1 = new ArrayList<IndexItemRequest>();
		list1.add(indexItemRequest);
		IndexItemResponse response = restTemplate.postForObject(properties.getDocumentUrl(), list1, IndexItemResponse.class);
		return response;
	}

	public IndexItemResponse deleteItem(String id) {
		IndexItemRequest indexItemRequest = new IndexItemRequest();
		indexItemRequest.setType(IndexType.DELETE);
		indexItemRequest.setId(id);
		indexItemRequest.setFields(new HashMap<String, Object>());
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJacksonHttpMessageConverter());
		restTemplate.setMessageConverters(list);
		List<IndexItemRequest> list1 = new ArrayList<IndexItemRequest>();
		list1.add(indexItemRequest);
		IndexItemResponse response = restTemplate.postForObject(properties.getDocumentUrl(), list1, IndexItemResponse.class);
		return response;
	}

	public SearchResults<ResultInfo> search(SearchIndexType type) {
		return search(type, null, null, null, null, null, null, null);
	}

	public SearchResults<ResultInfo> search(SearchIndexType type, String userId) {
		return search(type, null, null, null, null, userId, null, null);
	}

	public SearchResults<ResultInfo> search(SearchIndexType type, String userId, Integer size) {
		return search(type, null, null, null, null, userId, null, size);
	}

	public SearchResults<ResultInfo> search(SearchIndexType type, String name, String activity) {
		return search(type, null, null, name, activity, null, null, null);
	}

	public SearchResults<ResultInfo> search(SearchIndexType type, String name, String activity, String userId) {
		return search(type, null, null, name, activity, userId, null, null);
	}

	public SearchResults<ResultInfo> search(SearchIndexType type, BigDecimal lat, BigDecimal lon) {
		return search(type, lat, lon, null, null, null, null, null);
	}

	public SearchResults<ResultInfo> search(SearchIndexType type, Integer start, Integer size) {
		return search(type, null, null, null, null, null, start, size);
	}

	public SearchResults<ResultInfo> search(SearchIndexType type, String name, String activity, Integer start, Integer size) {
		return search(type, null, null, name, activity, null, start, size);
	}

	public SearchResults<ResultInfo> search(SearchIndexType type, BigDecimal lat, BigDecimal lon, Integer start, Integer size) {
		return search(type, lat, lon, null, null, null, start, size);
	}

	public SearchResults<ResultInfo> search(SearchIndexType type, BigDecimal lat, BigDecimal lon, String name, String activity, String userId, Integer start, Integer size) {
		return getAllFieldsFromUrl(buildUrl(type, lat, lon, name, activity, userId, start, size));
	}

	public String buildUrl(SearchIndexType type, BigDecimal lat, BigDecimal lon, String name, String activity, String userId, Integer start, Integer size) {
		BigDecimal radius = new BigDecimal(100);
		activity = activity == null ? "*" : activity + "*";
		name = name == null ? "*" : name + "*";
		userId = userId == null ? "*" : userId;
		start = start == null ? 0 : start;
		size = size == null ? 10 : size;
		type = type == null ? SearchIndexType.ALL : type;
		String latString = "";
		String lonString = "";
		// Calculate bounding box for radius
		if (lat != null && lon != null) {
			RectangularWindow rectangularWindow = new RectangularWindow(new LatLng(lat.doubleValue(), lon.doubleValue()), radius.doubleValue(), radius.doubleValue(), LengthUnit.KILOMETER);
			String latMin = new BigDecimal((long) ((90 + rectangularWindow.getMinLatitude()) * 100000)).toPlainString();
			String latMax = new BigDecimal((long) ((90 + rectangularWindow.getMaxLatitude()) * 100000)).toPlainString();
			String lonMin = new BigDecimal((long) ((180 + rectangularWindow.getLeftLongitude()) * 100000)).toPlainString();
			String lonMax = new BigDecimal((long) ((180 + rectangularWindow.getRightLongitude()) * 100000)).toPlainString();
			latString = " lat:" + latMin + ".." + latMax;
			lonString = " lon:" + lonMin + ".." + lonMax;
		}

		return properties.getSearchUrl() + "bq=(and type:'" + type.toString() + "' name:'" + name + "' userid:'" + userId + "' activity:'" + activity.replaceAll("&", " ") + "'" + latString + lonString + " env:'" + properties.getEnvironment().toString() + "')&rank=-promoted,-updated&size=" + size + "&start=" + start + "&return-fields=userid,lat,lon,name,activity,type,created,updated,promoted,points";
	}


	public SearchResults<ResultInfo> getAllFieldsFromUrl(String url) {
		SearchResults<ResultInfo> searchResults = new SearchResults<ResultInfo>();
		List<ResultInfo> results = new ArrayList<ResultInfo>();
		try {
			RestTemplate restTemplate = new RestTemplate();
			List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
			list.add(new MappingJacksonHttpMessageConverter());
			restTemplate.setMessageConverters(list);
			SearchResponse response = restTemplate.getForEntity(url, SearchResponse.class).getBody();
			if (response != null) {
				if (response.getHits() != null) {
					for (SearchHit hit : response.getHits().getHit()) {
						ResultInfo item = new ResultInfo();
						item.setId(hit.getId());
						if (hit.getData() != null) {
							if (hit.getData().get("userid") != null && hit.getData().get("userid").length != 0) {
								item.setUid(hit.getData().get("userid")[0]);
							}
							if (hit.getData().get("lat") != null && hit.getData().get("lat").length != 0) {
								Double n = new Double(hit.getData().get("lat")[0]);
								item.setLat(new BigDecimal(n / 100000 - 90));
							}
							if (hit.getData().get("lon") != null && hit.getData().get("lon").length != 0) {
								Double n = new Double(hit.getData().get("lon")[0]);
								item.setLon(new BigDecimal(n / 100000 - 180));
							}
							if (hit.getData().get("name") != null && hit.getData().get("name").length != 0) {
								item.setName(hit.getData().get("name")[0]);
							}
							if (hit.getData().get("activity") != null && hit.getData().get("activity").length != 0) {
								item.setActivity(hit.getData().get("activity")[0]);
							}
							if (hit.getData().get("updated") != null && hit.getData().get("updated").length != 0) {
								item.setUpdated(new Long(hit.getData().get("updated")[0]));
							}
							if (hit.getData().get("created") != null && hit.getData().get("created").length != 0) {
								item.setCreated(new Long(hit.getData().get("created")[0]));
							}
							if (hit.getData().get("promoted") != null && hit.getData().get("promoted").length != 0) {
								item.setPromoted((hit.getData().get("promoted")[0]).equals("1") ? true : false);
							}
							if (hit.getData().get("enabled") != null && hit.getData().get("enabled").length != 0) {
								item.setEnabled((hit.getData().get("enabled")[0]).equals("1") ? true : false);
							}
							if (hit.getData().get("points") != null && hit.getData().get("points").length != 0) {
								item.setPoints(new Long(hit.getData().get("points")[0]));
							}
							if (hit.getData().get("type") != null && hit.getData().get("type").length != 0) {
								item.setType(SearchIndexType.valueOf(hit.getData().get("type")[0]));
							}
						}
						results.add(item);
						/*if (hit.getData().get("type")[0].equals("request")) {
																		 listRequests.create(hit.getId());
																	 } else if (hit.getData().get("type")[0].equals("answer")) {
																		 listAnswers.create(hit.getId());
																	 }*/
					}
				}
			}
			searchResults.setList(results);
			searchResults.setCount(response.getHits().getFound());

		} catch (Exception e) {
			searchResults.setList(results);
		}

		return searchResults;
	}

}