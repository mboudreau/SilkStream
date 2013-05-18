package com.silkstream.platform.web;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.silkstream.platform.comparator.ResultInfoComparator;
import com.silkstream.platform.comparator.TivityItemComparator;
import com.silkstream.platform.enums.*;
import com.silkstream.platform.exception.FacebookTokenInvalidException;
import com.silkstream.platform.exception.PlaceDoesntExistException;
import com.silkstream.platform.exception.tivity.TivityNotExistException;
import com.silkstream.platform.models.db.Place;
import com.silkstream.platform.models.db.Tivity;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.db.UserLog;
import com.silkstream.platform.models.dto.*;
import com.silkstream.platform.models.dto.adapter.PlaceAdapter;
import com.silkstream.platform.models.dto.adapter.TivityAdapter;
import com.silkstream.platform.models.dto.google.ResultInfo;
import com.silkstream.platform.models.utils.GeoLocation;
import com.silkstream.platform.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/api/silkstream", produces = "application/json")
public class TivityController extends BasicController {
	@Inject
	protected CloudSearchService cloudSearchService;
	@Inject
	protected GoogleService googleService;
	@Inject
	protected TivityService tivityService;
	@Inject
	protected UserService userService;
	@Inject
	protected UserLogService userlogService;
	@Inject
	protected PlaceService placeService;
	@Inject
	protected UserLogService userLogService;
	@Inject
	protected PlaceController placeController;
	@Inject
	protected CommentService commentService;
	@Inject
	protected ReputationService reputationService;
	@Inject
	protected TivityAdapter tivityAdapter;
	@Inject
	protected PlaceAdapter placeAdapter;
	@Inject
	protected CommentController commentController;
	@Inject
	protected FacebookService facebookService;
	@Inject
	protected PaymentService paymentService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public TivityItem get(@PathVariable("id") String id) {
		Tivity tivity = tivityService.get(id);
		if (tivity == null) {
			throw new TivityNotExistException();
		}
		PlaceItem placeItem = placeAdapter.buildCompletePlaceItem(placeService.get(tivity.getLocationId()));
		if (placeItem != null) {
			TivityItem tivityItem = tivityAdapter.buildCompleteTivityItem(tivity, userService.get(tivity.getUserId()), placeItem);
			tivityItem.setComments(commentService.getFromTargetId(tivityItem.getId()));
			if (tivity.getFavoritedUser() != null && getUser() != null && tivity.getFavoritedUser().contains(getUser().getId())) {
				tivityItem.setFavorite(true);
			} else {
				tivityItem.setFavorite(false);
			}
			return tivityItem;
		}
		throw new PlaceDoesntExistException();
	}

	/*@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public TivityItem create(@RequestBody TivityItem item) throws Exception {
		User user = getUser();
		Tivity silkstream = new Tivity();
		silkstream.setId(createId());
		TivityItemValidator validator = new TivityItemValidator();
		List<Error> errors = validator.validate(item);

		if (errors.size() == 0) {
			if (user != null) {
				silkstream.setUserId(user.getId());
			}
			if (!user.hasRole(UserRole.ADMIN)) {
				silkstream.setPromoted(false);
			}
			Place location = null;
			if (item.getLocation().getGoogleId() != null) {
				location = placeService.get(item.getLocation().getId());
				if (location == null) {
					location = placeAdapter.buildCompletePlace(placeController.create(item.getLocation()));
				}
			} else if (StringUtil.isNotNullOrEmpty(item.getLocation().getName())) {
				List<Place> locations = googleService.getPlacesFromQuery(item.getLocation().getName());
				if (locations.size() != 0) {
					location = locations.get(0);
				}
			}

			if (location == null) {
				throw new MissingParametersException("Place has no google id.");
			}

			silkstream.setLocationId(location.getId());
			silkstream.setActivity(StringUtils.trimToNull(item.getActivity()));
			silkstream.setDescription(StringUtils.trimToNull(item.getDescription()));
			silkstream.setStartTime(item.getStartTime());
			silkstream.setEndTime(item.getEndTime());
			silkstream.setRating(0L);
			silkstream.setPromoted(item.isPromoted());

			long now = System.currentTimeMillis();
			silkstream.setCreatedDate(now);
			silkstream.setUpdatedDate(now);
			tivityService.add(silkstream, user, location);
			SearchResults<ResultInfo> tivityResult = cloudSearchService.search(SearchIndexType.ANSWER, user.getId());
			Long points = reputationService.ANSWER_CREATED_POINTS;
			if (user.getLoginCount() == null) {
				user.setLoginCount(0L);
			}
			if (tivityResult != null) {
				if (tivityResult.getList() != null) {
					if (user.getLoginCount() < 5 && tivityResult.getList().size() < 3) {
						switch (tivityResult.getList().size()) {
							case 0:
								points = reputationService.ANSWER_CREATED_POINTS_FIRSTTIME;
								break;
							case 1:
								points = reputationService.ANSWER_CREATED_POINTS_SECONDTIME;
								break;
							case 2:
								points = reputationService.ANSWER_CREATED_POINTS_THIRDTIME;
								break;
						}
					}
				}
			}

			// Log the action, change reputation
			userlogService.create(UserLogType.ANSWER, silkstream.getId(), user.getId(), ActionType.CREATE, points);
			return tivityAdapter.buildCompleteTivityItem(silkstream, user, location).setId(silkstream.getId());
		}
		return null;
	}*/

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public TivityItem create(@RequestParam("activity") String activity,
	                         @RequestParam("placeId") String placeId,
	                         @RequestParam("name") String name,
	                         @RequestParam("description") String description,
	                         @RequestParam("startTime") Long startTime,
	                         @RequestParam("endTime") Long endTime,
	                         @RequestParam(value = "minimum") Integer minimum,
	                         @RequestParam(value = "maximum", required = false) Integer maximum,
	                         @RequestParam(value = "cost", required = false) float cost,
	                         @RequestParam(value = "url", required = false) String url,
	                         @RequestParam(value = "promoted", required = false) boolean promoted) throws Exception {
		User user = getUser();
		Tivity item = new Tivity();
		item.setId(createId());
		item.setUserId(user.getId());
		item.setActivity(activity);
		item.setLocationId(placeId);
		item.setName(name);
		item.setDescription(description);
		item.setStartTime(startTime);
		item.setEndTime(endTime);
		item.setRating(0L);
		item.setMinimum(minimum);
		item.setMaximum(maximum);
		item.setCost(cost);
		item.setPromoted(user.hasRole(UserRole.ADMIN) ? promoted : false);
		Long now = System.currentTimeMillis();
		item.setCreatedDate(now);
		item.setUpdatedDate(now);

		Place place = placeService.get(placeId);
		tivityService.add(item, user, place);
		SearchResults<ResultInfo> tivityResult = cloudSearchService.search(SearchIndexType.ANSWER, user.getId());
		Long points = reputationService.ANSWER_CREATED_POINTS;
		if (user.getLoginCount() == null) {
			user.setLoginCount(0L);
		}
		if (tivityResult != null) {
			if (tivityResult.getList() != null) {
				if (user.getLoginCount() < 5 && tivityResult.getList().size() < 3) {
					switch (tivityResult.getList().size()) {
						case 0:
							points = reputationService.ANSWER_CREATED_POINTS_FIRSTTIME;
							break;
						case 1:
							points = reputationService.ANSWER_CREATED_POINTS_SECONDTIME;
							break;
						case 2:
							points = reputationService.ANSWER_CREATED_POINTS_THIRDTIME;
							break;
					}
				}
			}
		}

		// Log the action, change reputation
		userlogService.create(UserLogType.ANSWER, item.getId(), user.getId(), ActionType.CREATE, points);
		return tivityAdapter.buildCompleteTivityItem(item, user, place).setId(item.getId());
	}


	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "search", method = RequestMethod.GET, params = {"lat", "lon"})
	@ResponseBody
	public SearchResults<TivityItem> geoSearch(@RequestParam("lat") BigDecimal lat, @RequestParam("lon") BigDecimal lon,
	                                           @RequestParam(value = "q", required = false) String query, @RequestParam(value = "a", required = false) String activity,
	                                           @RequestParam(value = "n", required = false) String name, @RequestParam(value = "e", required = false) String email,
	                                           @RequestParam(value = "p", required = false) Integer page) throws Exception {
		if (lat != null && lon != null) {
			int num = 10;
			page = page == null || page < 1 ? 1 : page;

			SearchResults<TivityItem> res = new SearchResults<TivityItem>();
			//res.setGeoLocation(googleService.getGeoLocation(lat, lon));
			SearchResults<ResultInfo> searchResults = cloudSearchService.search(SearchIndexType.ANSWER, lat, lon, null, query, null, 0, 1000);
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

					if (searchResults.getList().size() > (page - 1) * num) {
						if (searchResults.getList().size() >= page * num) {
							searchResults.setList(searchResults.getList().subList((page - 1) * num, page * num));
						} else {
							searchResults.setList(searchResults.getList().subList((page - 1) * num, searchResults.getList().size()));
						}
					}

					List<Tivity> listRequests = new ArrayList<Tivity>();
					for (ResultInfo resultInfo : searchResults.getList()) {
						listRequests.add(tivityService.get(resultInfo.getId()));
					}

					List<TivityItem> regularTivities = new ArrayList<TivityItem>();
					List<TivityItem> promotedTivities = new ArrayList<TivityItem>();

					for (Tivity tivity : listRequests) {
						if (tivity != null) {
							User user = userService.get(tivity.getUserId());
							Place place = placeService.get(tivity.getLocationId());
							if (user != null && place != null) {
								TivityItem item = tivityAdapter.buildCompleteTivityItem(tivity, user, place);
								if (center != null) {
									LatLng point = new LatLng(place.getGeoLocation().getLat().doubleValue(), place.getGeoLocation().getLon().doubleValue());
									item.setDistance(LatLngTool.distance(center, point, LengthUnit.KILOMETER));
									item.setDirection(bearing(center, point));
								}
								item.setCommentCount(commentService.getFromTargetId(item.getId()).size());
								if (tivity.getPromoted()) {
									promotedTivities.add(item);
								} else {
									regularTivities.add(item);
								}
							}
						}
					}

					Collections.sort(promotedTivities, new TivityItemComparator());
					Collections.sort(regularTivities, new TivityItemComparator());

					res.setList(promotedTivities);
					res.getList().addAll(regularTivities);
					res.setCount(searchResults.getCount());
					res.setItemPerPage(num);
				}
			}
			SearchOptions options = new SearchOptions();
			options.setActivity(activity);
			options.setEmail(email);
			options.setName(name);
			options.setPage(page);
			res.setOptions(options);

			return res;
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "search", method = RequestMethod.GET, params = {"address"})
	@ResponseBody
	public SearchResults<TivityItem> addressSearch(@RequestParam("address") String address,
	                                               @RequestParam(value = "q", required = false) String query, @RequestParam(value = "a", required = false) String activity,
	                                               @RequestParam(value = "n", required = false) String name, @RequestParam(value = "e", required = false) String email,
	                                               @RequestParam(value = "p", required = false) Integer page) throws Exception {
		List<GeoLocation> locations = googleService.getGeoLocation(address);
		if (locations.size() > 0) {
			SearchResults<TivityItem> results = geoSearch(locations.get(0).getLat(), locations.get(0).getLon(), query, activity, name, email, page);
			results.getOptions().setGeoLocation(locations.get(0));
			return results;
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}/rsvp/{action}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public RSVPType rsvp(@PathVariable("id") String id, @PathVariable("action") String action, HttpServletResponse response) {
		RSVPType type = RSVPType.valueOf(action.toUpperCase());
		Tivity tivity = tivityService.get(id);
		User user = getUser();
		if (type != null && tivity != null && user != null) {
			if (type == RSVPType.YES && tivity.getCost() != null && tivity.getCost() > 0) {
				// If a cost is involved, redirect to payment
				try {
					response.addHeader("REDIRECT", paymentService.requestPaypalPayment(tivity, user));
				} catch (Exception e) {

				}
			} else {
				return confirmRSVP(id, action, user.getId());
			}
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}/confirm/{action}/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public RSVPType confirmRSVP(@PathVariable("id") String id, @PathVariable("action") String action, @PathVariable("userId") String userId) {
		RSVPType type = RSVPType.valueOf(action.toUpperCase());
		Tivity tivity = tivityService.get(id);
		User user = userService.get(userId);
		if (type != null && tivity != null && user != null) {
			HashMap<String, RSVPType> hash = tivity.getRSVP() == null ? new HashMap<String, RSVPType>() : tivity.getRSVP();
			hash.put(user.getId(), type);
			tivity.setRSVP(hash);
			tivityService.save(tivity);
			List<UserLog> userLogs = userLogService.getAllLogsOnTarget(id);
			for (UserLog userLog : userLogs) {
				if (userLog.getUserid() != null && userLog.getAction() != null) {
					if (userLog.getUserid().equals(user.getId()) && userLog.getAction().toString().contains("RSVP")) {
						userLogService.delete(userLog.getId());
					}
				}
			}
			userLogService.create(UserLogType.ANSWER, id, user.getId(), ActionType.valueOf("RSVP_" + type.toString()), 0L);
			return type;
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}/rsvp/invite", method = RequestMethod.POST, consumes = "application/json")
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public void invite(@PathVariable("id") String id, @RequestBody List<String> friends) {
		for (String fbid : friends) {
			User user = userService.getWithFbId(fbid);
			if (user.getFacebookId() != null) {
				Tivity tivity = tivityService.get(id);
				try {
					facebookService.publishOnFriendWall(user, getUser().getFullName() + " wants you to join him.  Get started on", "Hey, I just RSVP'ed to join '" + tivity.getName() + "' on Tivity and so should you.", getUser(), "http://www.silkstream.us/silkstream/" + id);
				} catch (OperationNotPermittedException e) {
					throw new FacebookTokenInvalidException();
				}
			}
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, value = "{id}/comment")
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public CommentItem comment(@PathVariable("id") String id, @RequestBody String message) {
		return commentService.create(id, message, getUser().getId(), UserLogType.ANSWER);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(@PathVariable String id) {
		if (id != null) {
			Tivity tivity = tivityService.get(id);
			if (tivity != null) {
				mapper.delete(tivity);
				commentController.deleteFromTargetId(id);
				cloudSearchService.deleteItem(id);
				List<UserLog> userLogs = userLogService.getAllLogsOnTarget(id);
				for (UserLog userLog : userLogs) {
					userLogService.delete(userLog.getId());
					cloudSearchService.deleteItem(userLog.getId());
				}
			}
		}
	}
}