package com.silkstream.platform.web;

import com.silkstream.platform.enums.ModelType;
import com.silkstream.platform.enums.SearchIndexType;
import com.silkstream.platform.enums.WeekDay;
import com.silkstream.platform.exception.ApplicationException;
import com.silkstream.platform.exception.FacebookTokenInvalidException;
import com.silkstream.platform.exception.user.UserAlreadyExistsException;
import com.silkstream.platform.exception.user.UserDoesntExistException;
import com.silkstream.platform.models.db.Invitation;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.dto.*;
import com.silkstream.platform.models.dto.adapter.UserAdapter;
import com.silkstream.platform.models.dto.google.ResultInfo;
import com.silkstream.platform.models.utils.AvailabilityDay;
import com.silkstream.platform.models.utils.Followed;
import com.silkstream.platform.models.utils.GeoLocation;
import com.silkstream.platform.service.*;
import com.silkstream.platform.utils.StringUtil;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/api/user", produces = "application/json")
public class UserController extends BasicController {
	@Inject
	UserService userService;
	@Inject
	MailService mailService;
	@Inject
	CloudSearchService cloudSearchService;
	@Inject
	GoogleService googleService;
	@Inject
	UserLogService userLogService;
	@Inject
	FacebookService facebookService;
	@Inject
	protected ShaPasswordEncoder encoder;
	@Inject
	UserAdapter userAdapter;
	@Inject
	InvitationService invitationService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, value = "register")
	@ResponseBody
	public UserItem registerUser(@RequestParam("email") String email, @RequestParam("password") String password) throws UserAlreadyExistsException {
		User user = userService.getWithEmail(email);
		if (user == null) {
			return userAdapter.buildCompleteUserItem(userService.register(email, password));
		} else {
			if (user.isEnabled()) {
				throw new UserAlreadyExistsException("User already exists, with email : " +email );
			} else {
				// Resend confirmation email if user isn't enabled yet
				userService.createConfirmation(user);
			}
		}
		return null;
	}


	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "availability")
	@PreAuthorize("hasRole('USER')")
	public void setAvailabilities(@RequestBody HashMap<WeekDay, AvailabilityDay> availability) {
		User user = getUser();
		if (user != null) {
			user.setAvailability(availability);
			userService.update(user);
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, value = "{id}/follow")
	@PreAuthorize("hasRole('USER')")
	public void followAFriend(@PathVariable String friendId) {
		User user = getUser();
		followAFriend(user,friendId);
	}


	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "activities")
	@PreAuthorize("hasRole('USER')")
	public void setActivities(@RequestBody ActivitiesItem activities) {
		User user = getUser();
		if (user != null) {
			user.setActivities(activities.getActivities());
			userService.update(user);
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "locations")
	@ResponseBody
	@PreAuthorize("hasRole('USER')")
	public void setLocations(@RequestBody PlacesItem places) {
		User user = getUser();
		if (user != null) {
			user.setLocations(places.getLocations());
			userService.update(user);
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, value = "bio")
	@ResponseBody
	@PreAuthorize("hasRole('USER')")
	public void setBio(@RequestBody String bio) {
		User user = getUser();
		if (user != null) {
			user.setBio(bio);
			userService.update(user);
		}

	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public UserItem getCurrentUser() {
		UserItem userItem = userAdapter.buildCompleteUserItem(getUser());
		return userItem;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, params = {"email", "password"})
	@ResponseBody
	public UserItem login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
		User user = userService.login(email.toLowerCase(), password);
		UserItem userItem = null;
		if (user != null) {
			userItem = userAdapter.buildCompleteUserItem(user);
			if (request != null && response != null) {
				userService.rememberUser(request, response);
			}
		}
		return userItem;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, value = "{id}")
	@ResponseBody
	public UserItem getUserById(@PathVariable("id") String id) throws UserDoesntExistException {
		List<String> attributesToGet = userService.getAllAttributes();
		attributesToGet.remove("gl");
		UserItem user = userAdapter.buildCompleteUserItem(userService.get(id, attributesToGet));
		if (user == null) {
			throw new UserDoesntExistException("User with id " + id + " doesn't exist.");
		}
		return user;
	}

	/*@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", value = "update/pwd")
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public void updatePasswordByUser(@RequestParam(required = true) String formerPassword, @RequestParam(required = true) String verification, @RequestParam(required = true) String newPassword) {
		User session = getUser();
		if (session != null) {
			if (!formerPassword.equals(verification)) {
			} else {
				if (!session.getPassword().equals(formerPassword)) {
				} else {
					String password = encoder.encodePassword(newPassword, session.getCreatedDate());
					session.setPassword(password);
					mapper.save(session);
				}
			}
		}
	}*/

	/*@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.PUT, value = "reset/pwd", consumes = "application/json")
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public void resetPassword(@RequestParam String newPassword, String confirmationId, String confirmationPassword) throws ApplicationException {
		if (!newPassword.equals(confirmationPassword)) {
			throw new PasswordDoesnotMatchException();
		}
		User user = userService.getUserFromUserConfirmationId(confirmationId);
		if (user == null) {
			throw new UserDoesntExistException();
		}
		userService.updatePassword(encoder.encodePassword(newPassword, user.getCreatedDate()), user);
	}*/

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, value = "reset/pwd", consumes = "application/json")
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public void resetPasswordConfirmation(@RequestParam String email) {
		User user = userService.getWithEmail(email);
		if (user == null) {
			throw new UserDoesntExistException();
		}
		userService.createConfirmation(user, false);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(@PathVariable String id) {
		userService.disable(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "search", method = RequestMethod.GET, params = {"lat", "lon"})
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public List<UserItem> search(@RequestParam("lat") BigDecimal lat, @RequestParam("lon") BigDecimal lon,
	                             @RequestParam(value = "q", required = false) String query, @RequestParam(value = "a", required = false) String activity,
	                             @RequestParam(value = "n", required = false) String name, @RequestParam(value = "e", required = false) String email,
	                             @RequestParam(value = "p", required = false) Integer page) {
		List<UserItem> userItems = new ArrayList<UserItem>();
		SearchResults<ResultInfo> searchResults = cloudSearchService.search(SearchIndexType.USER, lat, lon, name, activity, email, null, 1000);
		int i = 0;
		if (searchResults != null) {
			if (searchResults.getList() != null) {
				while (searchResults.getList().size() < searchResults.getCount() || i > 10) {
					i++;
					searchResults.getList().addAll(cloudSearchService.search(SearchIndexType.USER, lat, lon, name, activity, email, null, 1000).getList());
				}
			}

			for (ResultInfo resultInfo : searchResults.getList()) {
				UserItem userItem = new UserItem();
				if (resultInfo.getName() != null) {
					String[] array = resultInfo.getName().split(" ");
					if (array.length == 2) {
						userItem.setFirstName(array[0]);
						userItem.setLastName(array[1]);
					}
				}
				if (resultInfo.getActivity() != null) {
					String[] array = resultInfo.getActivity().split(",");
					if (array != null && array.length > 0) {
						userItem.setActivities(Arrays.asList(array));
					}
				}
				userItem.setId(resultInfo.getId());
				userItems.add(userItem);
			}
			return userItems;
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "search", method = RequestMethod.GET, params = {"address"})
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public List<UserItem> search(@RequestParam("address") String address,
	                             @RequestParam(value = "q", required = false) String query, @RequestParam(value = "a", required = false) String activity,
	                             @RequestParam(value = "n", required = false) String name, @RequestParam(value = "e", required = false) String email,
	                             @RequestParam(value = "p", required = false) Integer page) {
		List<GeoLocation> locations = googleService.getGeoLocation(address);
		if (locations.size() > 0) {
			return search(locations.get(0).getLat(), locations.get(0).getLon(), query, activity, name, email, page);
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "friends", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public List<UserItem> getFriends(@RequestParam(value = "name", required = false) String name) {
		return facebookService.getFriendsFromUser(getUser(), name);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "invites", method = RequestMethod.POST, consumes = "application/json")
	@PreAuthorize("hasRole('USER')")
	public void invite(@RequestBody Invites invitations) {
		invite(invitations.getList(), invitations.getMessage());
	}

	public void invite(List<UserItem> invitations, String message) {
		for (UserItem friend : invitations) {
			// check if friend is already in system
			User user = userAdapter.buildCompleteUser(friend);
			User tempUser = null;
			String id = null;
			if(user.getEmail() != null) {
				id = friend.getEmail();
				tempUser = userService.getWithEmail(id);
			}else if(user.getFacebookId() != null) {
				id = friend.getFacebookId();
				tempUser = userService.getWithEmail(id);
			}
			user = tempUser == null?user:tempUser;

			if (user.getId() != null) {
				// If friend already exist in system, just follow friend right away
				followAFriend(friend.getId());
			} else {
				message = StringUtil.isNotNullOrEmpty(message) ? message : "Hey, let's get active together.";
				Invitation invitation = new Invitation();
				invitation.setId(createId());
				invitation.setCreatedDate(now());
				invitation.setFrom(getUser().getId());
				invitation.setTo(id);
				invitationService.save(invitation);
				if (user.getFacebookId() != null) {
					try {
						facebookService.publishOnFriendWall(user, getUser().getFullName() + " wants to get active with " + user.getFullName() + ". Connect on", message, getUser(), "http://www.silkstream.us/invite/" + invitation.getId());
					} catch (OperationNotPermittedException e) {
						throw new FacebookTokenInvalidException();
					}
				} else if (user.getEmail() != null) {
					mailService.sendInvite(user.getEmail(), getUser());
				}
			}
		}
	}

	public void followAFriend(User user,String friendId){
		if (user != null) {
			User friend = userService.get(friendId);
			if (friend != null) {
				if (user.getFolloweds() == null) {
					user.setFolloweds(new ArrayList<Followed>());
				}
				Followed followed = new Followed();
				followed.setId(friendId);
				followed.setModelType(ModelType.USER);
				user.getFolloweds().add(followed);
				userService.update(user);
			} else {
				throw new UserDoesntExistException();
			}
		}
	}


	/*@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "invite")
	@PreAuthorize("hasRole('USER')")
	public void invite(@RequestBody Invites invites) {
		User user = getUser();
		if (invites != null) {
			for (String email : invites.getEmails()) {
				if (user != null) {

				}
			}
		}
	}*/
}
