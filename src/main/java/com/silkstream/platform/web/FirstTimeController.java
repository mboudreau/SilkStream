package com.silkstream.platform.web;

import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.dto.FirstTimeItem;
import com.silkstream.platform.service.FacebookService;
import com.silkstream.platform.service.UserService;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/first-time", produces = "application/json")
public class FirstTimeController extends BasicController {
	@Inject
	UserController userController;
	@Inject
	UserService userService;
	@Inject
	FacebookService facebookService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public FirstTimeItem getFirstTime() {
		User user = getUser();
		FirstTimeItem item = new FirstTimeItem();
		item.setActivities(user.getActivities());
		item.setLocations(user.getLocations());
		item.setAvailability(user.getAvailability());
		item.setFriends(facebookService.getFriendsFromUser(getUser()));
		return item;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	@PreAuthorize("hasRole('USER')")
	public void setFirstTime(@RequestBody FirstTimeItem item) {
		User user = getUser();
		user.setActivities(item.getActivities());
		user.setLocations(item.getLocations());
		user.setAvailability(item.getAvailability());
		userService.update(user);
		userController.invite(item.getFriends(), item.getMessage());
	}
}
