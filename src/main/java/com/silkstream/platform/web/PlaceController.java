package com.silkstream.platform.web;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.silkstream.platform.comparator.PlaceItemComparator;
import com.silkstream.platform.comparator.ResultInfoComparator;
import com.silkstream.platform.enums.*;
import com.silkstream.platform.exception.ApplicationException;
import com.silkstream.platform.exception.FacebookTokenInvalidException;
import com.silkstream.platform.models.db.*;
import com.silkstream.platform.models.dto.PlaceItem;
import com.silkstream.platform.models.dto.SearchOptions;
import com.silkstream.platform.models.dto.SearchResults;
import com.silkstream.platform.models.dto.TivityItem;
import com.silkstream.platform.models.dto.adapter.PlaceAdapter;
import com.silkstream.platform.models.dto.adapter.TivityAdapter;
import com.silkstream.platform.models.dto.adapter.UserAdapter;
import com.silkstream.platform.models.dto.google.ResultInfo;
import com.silkstream.platform.models.dto.google.placedetail.GoogleDetailReverse;
import com.silkstream.platform.models.utils.Followed;
import com.silkstream.platform.models.utils.GeoLocation;
import com.silkstream.platform.service.*;
import com.silkstream.platform.utils.StringUtil;
import com.silkstream.platform.validator.PlaceItemValidator;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@Controller
@RequestMapping(value = "/api/place", produces = "application/json")
public class PlaceController extends BasicController {
	@Inject
	protected CloudSearchService cloudSearchService;
	@Inject
	protected UserService userService;
	@Inject
	protected PlaceService placeService;
	@Inject
	protected GoogleService googleService;
	@Inject
	private PlaceAdapter placeAdapter;
	@Inject
	UserAdapter userAdapter;
	@Inject
	protected UserLogService userLogService;
	@Inject
	protected TivityService tivityService;
	@Inject
	protected TivityController tivityController;
	@Inject
	protected TivityAdapter tivityAdapter;
	@Inject
	protected UserLogService userlogService;
	@Inject
	protected FacebookService facebookService;
	@Inject
	protected NotificationService notificationService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public PlaceItem getItem(@PathVariable("id") String id) {
		Place place = placeService.get(id);
		if (place == null) {
			return null;
		}
		PlaceItem result = placeAdapter.buildCompletePlaceItem(place);
		GoogleDetailReverse googleDetailResult = googleService.getGoogleInfosFromReference(place.getGoogleReference());

		List<Tivity> tivities = tivityService.getTivitiesFromPlace(result.getId());
		List<User> users = userService.getUsersFromTivities(tivities);
		List<TivityItem> tivityItems = new ArrayList<TivityItem>();

		for (Tivity tivity : tivities) {
			tivityItems.add(tivityAdapter.buildCompleteTivityItem(tivity, getUserFromId(users, tivity.getUserId()), place));
		}

		result.setTivities(tivityItems);

		// Set favorite flag
		if (getUser() != null) {
			result.setFavorite(userService.isFollowing(getUser(), ModelType.LOCATION, result.getId()));
		}

		if (googleDetailResult != null && googleDetailResult.getResult() != null) {
			result.setGoogleRating(googleDetailResult.getResult().getRating());
			result.setGoogleComments(googleDetailResult.getResult().getReviews());
		}
		return result;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "search", method = RequestMethod.GET, params = {"lat", "lon"})
	@ResponseBody
	public SearchResults<PlaceItem> searchGeo(@RequestParam("lat") BigDecimal lat, @RequestParam("lon") BigDecimal lon,
	                                          @RequestParam(value = "activity", required = false) String activity,
	                                          @RequestParam(value = "name", required = false) String name,
	                                          @RequestParam(value = "page", required = false) Integer page) throws Exception {
		if (lat != null && lon != null) {
			int num = 10;
			page = page == null || page < 1 ? 0 : page - 1;

			List<Place> results = new ArrayList<Place>();

			SearchResults<ResultInfo> placesFromCloudSearch = cloudSearchService.search(SearchIndexType.LOCATION, lat, lon, name, activity, null, page * num, num);
			//filtering by distance before any db calls


			if (placesFromCloudSearch != null) {

				LatLng center = null;
				if (lat != null && lon != null) {
					center = new LatLng(lat.doubleValue(), lon.doubleValue());
				}

				List<ResultInfo> promotedRes = new ArrayList<ResultInfo>();
				List<ResultInfo> notPromotedRes = new ArrayList<ResultInfo>();

				for (ResultInfo resultInfo : placesFromCloudSearch.getList()) {
					if (resultInfo != null) {
						if (center != null) {
							if (resultInfo.getLat() != null && resultInfo.getLon() != null) {
								LatLng point = new LatLng(resultInfo.getLat().doubleValue(), resultInfo.getLon().doubleValue());
								resultInfo.setDistance(LatLngTool.distance(center, point, LengthUnit.KILOMETER));
							}
						}
						if (resultInfo.getPromoted()) {
							promotedRes.add(resultInfo);
						} else {
							notPromotedRes.add(resultInfo);
						}
					}
				}
				Collections.sort(promotedRes, new ResultInfoComparator());
				Collections.sort(notPromotedRes, new ResultInfoComparator());

				placesFromCloudSearch.setList(promotedRes);
				placesFromCloudSearch.getList().addAll(notPromotedRes);


				for (ResultInfo info : placesFromCloudSearch.getList()) {
					Place place = new Place();
					GeoLocation geoLocation = new GeoLocation();
					if (info != null && info.getEnabled()) {
						geoLocation.setLon(info.getLon());
						geoLocation.setLat(info.getLat());
						place.setName(info.getName());
						if (info.getActivity() != null) {
							Set<String> temp = new HashSet<String>();
							for (String st : Arrays.asList(info.getActivity().split(","))) {
								temp.add(st);
							}
							place.setActivities(temp);
						}
						place.setId(info.getId());
						place.setPromoted(info.getPromoted());
						place.setGeoLocation(geoLocation);
						results.add(place);
					}
				}
			}

			/*Set<String> googleRef = new HashSet<String>();
			for (Place place : results) {
				if (place != null) {
					if (googleRef.contains(place.getGoogleReference()) && place.getGoogleReference() != null) {
						results.remove(place);
					}
					googleRef.add(place.getGoogleReference());
				}
			}*/

			// If not enough results, show Goo

			SearchResults<PlaceItem> res = new SearchResults<PlaceItem>();
			res.setList(new ArrayList<PlaceItem>());
			for (Place place : results) {
				res.getList().add(placeAdapter.buildCompletePlaceItem(place));
			}
			SearchOptions options = new SearchOptions();
			options.setGeoLocation(new GeoLocation(lat, lon));
			options.setName(name);
			options.setPage(page);
			options.setActivity(activity);
			res.setOptions(options);
			return res;
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "search", method = RequestMethod.GET, params = {"address"})
	@ResponseBody
	public SearchResults<PlaceItem> searchByAddress(@RequestParam("address") String address,
	                                                @RequestParam(value = "activity", required = false) String activity,
	                                                @RequestParam(value = "name", required = false) String name,
	                                                @RequestParam(value = "page", required = false) Integer page) throws Exception {
		if (StringUtil.isNotNullOrEmpty(address)) {
			List<GeoLocation> locations = googleService.getGeoLocation(address);
			if (locations.size() > 0) {
				SearchResults<PlaceItem> results = searchGeo(locations.get(0).getLat(), locations.get(0).getLon(), activity, name, page);
				results.getOptions().setGeoLocation(locations.get(0));
				return results;
			}
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "search/google", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasRole('USER')")
	public SearchResults<PlaceItem> searchByQuery(@RequestParam(value = "query") String query, @RequestParam(value = "address", required = false) String address) {
		return new SearchResults<PlaceItem>(placeAdapter.buildCompletePlaceItem(googleService.getPlacesFromQuery(query, address)));
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "search/tivities", method = RequestMethod.GET, params = {"lat", "lon"})
	@ResponseBody
	public SearchResults<PlaceItem> searchTivitiesByGeo(@RequestParam("lat") BigDecimal lat, @RequestParam("lon") BigDecimal lon,
	                                                    @RequestParam(value = "activity", required = false) String activity,
	                                                    @RequestParam(value = "name", required = false) String name,
	                                                    @RequestParam(value = "userId", required = false) String userId,
	                                                    @RequestParam(value = "page", required = false) Integer page) {
		if (lat != null && lon != null) {
			int num = 10;
			page = page == null || page < 1 ? 0 : page - 1;

			SearchResults<PlaceItem> res = new SearchResults<PlaceItem>();
			SearchResults<ResultInfo> searchResults = cloudSearchService.search(SearchIndexType.LOCATION, lat, lon, name, activity, userId, 0, 1000);
			if (searchResults != null) {
				if (searchResults.getList() != null) {
					List<ResultInfo> promotedRes = new ArrayList<ResultInfo>();
					List<ResultInfo> notPromotedRes = new ArrayList<ResultInfo>();

					//filtering by distance before any db calls
					LatLng center = null;
					if (lat != null && lon != null) {
						center = new LatLng(lat.doubleValue(), lon.doubleValue());
					}

					for (ResultInfo resultInfo : searchResults.getList()) {
						if (resultInfo != null) {
							if (center != null) {
								if (resultInfo.getLat() != null && resultInfo.getLon() != null) {
									LatLng point = new LatLng(resultInfo.getLat().doubleValue(), resultInfo.getLon().doubleValue());
									resultInfo.setDistance(LatLngTool.distance(center, point, LengthUnit.KILOMETER));
								}
							}
							if (resultInfo.getPromoted()) {
								promotedRes.add(resultInfo);
							} else {
								notPromotedRes.add(resultInfo);
							}
						}
					}
					Collections.sort(promotedRes, new ResultInfoComparator());
					Collections.sort(notPromotedRes, new ResultInfoComparator());

					searchResults.setList(promotedRes);
					searchResults.getList().addAll(notPromotedRes);

					if (page > 0) {
						if (searchResults.getList().size() > (page - 1) * num) {
							if (searchResults.getList().size() >= page * num) {
								searchResults.setList(searchResults.getList().subList((page - 1) * num, page * num));
							} else {
								searchResults.setList(searchResults.getList().subList((page - 1) * num, searchResults.getList().size()));
							}
						}
					} else {
						if (searchResults.getList().size() >= num) {
							searchResults.setList(searchResults.getList().subList(0, num));
						}
					}

					List<String> placeIds = new ArrayList<String>();
					List<Place> listRequests = new ArrayList<Place>();
					for (ResultInfo resultInfo : searchResults.getList()) {
						Place loc = placeService.get(resultInfo.getId());
						if (loc != null) {
							listRequests.add(loc);
							placeIds.add(loc.getId());
						}
					}

					List<PlaceItem> placeItems = new ArrayList<PlaceItem>();
					List<PlaceItem> placeItemsWithTivities = new ArrayList<PlaceItem>();
					List<Tivity> tivities = null;
					if (placeIds.size() > 0) {
						tivities = tivityService.getTivitiesFromPlaces(placeIds);
					}
					for (Place place : listRequests) {
						if (place != null) {
							PlaceItem item = placeAdapter.buildCompletePlaceItem(place);
							GoogleDetailReverse googleDetailResult = googleService.getGoogleInfosFromReference(place.getGoogleReference());


							if (googleDetailResult != null && googleDetailResult.getResult() != null) {
								item.setGoogleRating(googleDetailResult.getResult().getRating());
								item.setGoogleComments(googleDetailResult.getResult().getReviews());
							}

							if (center != null) {
								LatLng point = new LatLng(place.getGeoLocation().getLat().doubleValue(), place.getGeoLocation().getLon().doubleValue());
								item.setDistance(LatLngTool.distance(center, point, LengthUnit.KILOMETER));
								item.setDirection(bearing(center, point));
							}
							List<TivityItem> tivityItems = new ArrayList<TivityItem>();
							for (Tivity tivity : tivities) {
								if (tivity.getLocationId().equals(place.getId())) {
									tivityItems.add(tivityAdapter.buildCompleteTivityItem(tivity, userService.get(tivity.getUserId()), place));
								}
							}
							if (getUser() != null) {
								item.setFavorite(userService.isFollowing(getUser(), ModelType.LOCATION, item.getId()));
							}
							item.setTivities(tivityItems);
							if (tivityItems.size() > 0) {
								placeItemsWithTivities.add(item);
							} else {
								placeItems.add(item);
							}
						}

					}

					Collections.sort(placeItems, new PlaceItemComparator());
					Collections.sort(placeItemsWithTivities, new PlaceItemComparator());
					res.setList(placeItemsWithTivities);
					res.getList().addAll(placeItems);
				}
			}
			res.setCount(searchResults.getCount());
			res.setItemPerPage(num);

			SearchOptions options = new SearchOptions();
			options.setGeoLocation(new GeoLocation(lat, lon));
			options.setActivity(activity);
			options.setName(name);
			options.setPage(page);
			res.setOptions(options);

			return res;
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "search/tivities", method = RequestMethod.GET, params = {"address"})
	@ResponseBody
	public SearchResults<PlaceItem> searchTivitiesByAddress(@RequestParam("address") String address,
	                                                        @RequestParam(value = "activity", required = false) String activity,
	                                                        @RequestParam(value = "name", required = false) String name,
	                                                        @RequestParam(value = "userId", required = false) String userId,
	                                                        @RequestParam(value = "page", required = false) Integer page) {
		if (StringUtil.isNotNullOrEmpty(address)) {
			List<GeoLocation> locations = googleService.getGeoLocation(address);
			if (locations.size() > 0) {
				SearchResults<PlaceItem> results = searchTivitiesByGeo(locations.get(0).getLat(), locations.get(0).getLon(), activity, name, userId, page);
				results.getOptions().setGeoLocation(locations.get(0));
				return results;
			}
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public PlaceItem create(@RequestBody PlaceItem item) throws ApplicationException {
		PlaceItemValidator itemValidator = new PlaceItemValidator();
		List<Error> errors = itemValidator.validate(item);
		if (errors.size() == 0) {
			Place place = placeService.getFromGoogleId(item.getGoogleId());
			if (place == null) {
				if (item.getGeoLocation() != null) {
					return placeAdapter.buildCompletePlaceItem(placeService.add(placeAdapter.buildCompletePlace(item)));
				}
			}
			return placeAdapter.buildCompletePlaceItem(place);
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "add/{type}/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	// TODO: change 'type' to ENUM
	public PlaceItem add(@PathVariable("type") String type, @PathVariable("id") String id) throws ApplicationException {
		if (type.equals("google")) {
			Place place = placeService.createFromGoogleReferenceId(id);
			if (place != null) {
				return placeAdapter.buildCompletePlaceItem(place);
			}
		}
		return null;
	}


	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(@PathVariable String id) {
		placeService.delete(id);
		cloudSearchService.deleteItem(id);
		List<Tivity> tivities = tivityService.getTivitiesFromPlace(id);
		for (Tivity tivity : tivities) {
			tivityController.delete(tivity.getId());
		}
		List<UserLog> userLogs = userLogService.getAllLogsOnTarget(id);
		for (UserLog userLog : userLogs) {
			userLogService.delete(userLog.getId());
			cloudSearchService.deleteItem(userLog.getId());
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/follow")
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public Boolean follow(@PathVariable("id") String id) {
		Place place = placeService.get(id);
		if (place != null) {
			User user = getUser();
			List<Followed> list = user.getFolloweds() == null ? new ArrayList<Followed>() : user.getFolloweds();
			if (!placeService.isFollowing(user, ModelType.LOCATION, id)) {
				userLogService.create(UserLogType.LOCATION, id, user.getId(), ActionType.FOLLOW, 0L);
				Followed followed = new Followed();
				followed.setId(id);
				followed.setModelType(ModelType.LOCATION);
				list.add(followed);
				user.setFolloweds(list);
				userService.update(user);
				if (place.getFavoriteCount() == null) {
					place.setFavoriteCount(0);
				}
				place.setFavoriteCount(place.getFavoriteCount() + 1);
				placeService.update(place);
				Notification notification = new Notification();
				notification.setFrom(place.getId());
				notification.setTo(user.getId());
				notification.setNotificationType(NotificationType.LOCATION_FOLLOWED);
				notificationService.add(notification);
				return true;
			} else {
				userlogService.create(UserLogType.LOCATION, id, user.getId(), ActionType.UNFOLLOW, 0L);
				user.setFolloweds(userService.removeSpecificId(user.getFolloweds(), id));
				if (user.getFolloweds().size() == 0) {
					user.setFolloweds(null);
				}
				userService.update(user);
				if (place.getFavoriteCount() == null) {
					place.setFavoriteCount(1);
				}
				if (place.getFavoriteCount() <= 0) {
					place.setFavoriteCount(1);
				}
				place.setFavoriteCount(place.getFavoriteCount() - 1);
				placeService.update(place);
				return false;
			}
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}/follow/invite", method = RequestMethod.POST, consumes = "application/json")
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public void invite(@PathVariable("id") String id, @RequestBody List<String> friends) {
		for (String fbid : friends) {
			User user = userService.getWithFbId(fbid);
			if (user.getFacebookId() != null) {
				Place place = placeService.get(id);
				try {
					facebookService.publishOnFriendWall(user, getUser().getFullName() + " wants you to follow a Place.  Get started on", "Hey, I just started following '" + place.getName() + "' on Tivity and so should you.", getUser(), "http://www.silkstream.us/place/" + id);
				} catch (OperationNotPermittedException e) {
					throw new FacebookTokenInvalidException();
				}
			}
		}
	}

	private User getUserFromId(List<User> users, String userId) {
		for (User user : users) {
			if (user.getId().equals(userId)) {
				return user;
			}
		}
		return null;
	}

}
