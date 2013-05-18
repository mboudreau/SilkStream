package com.silkstream.platform.web;

import com.silkstream.platform.exception.MissingParametersException;
import com.silkstream.platform.exception.ValidatorException;
import com.silkstream.platform.models.db.Invitation;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.service.InvitationService;
import com.silkstream.platform.service.UserService;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(value = "/api/invitation", produces = "application/json")
public class InvitationController extends BasicController {
	@Inject
	InvitationService invitationService;
	@Inject
	UserService userService;
	@Inject
	UserController userController;


	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public List<Invitation> getInvitation(@RequestParam(value = "id", required = false) String invitationId, @RequestParam(value = "fr", required = false) String idFrom) {
		if(invitationId==null && idFrom ==null){
			throw new MissingParametersException();
		}
		if(invitationId!=null && idFrom!=null){
			throw new ValidatorException("both parameters cannot be present");
		}
		if(invitationId!=null){
			return Collections.nCopies(1,invitationService.get(invitationId));
		}else {
			return invitationService.loadUsingFrom(idFrom);
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public void setFollow(@RequestBody Invitation invitation, @RequestBody String userId) {
		User userFrom = userService.get(invitation.getFrom());
		userController.followAFriend(userFrom,userId);
	}
}
