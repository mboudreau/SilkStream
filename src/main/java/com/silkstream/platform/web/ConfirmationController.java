package com.silkstream.platform.web;

import com.silkstream.platform.enums.GenderType;
import com.silkstream.platform.exception.ApplicationException;
import com.silkstream.platform.service.ConfirmationService;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/confirm", produces = "application/json")
public class ConfirmationController extends BasicController {

	@Inject
	ConfirmationService confirmationService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseBody
	public String exists(@PathVariable("id") String id) throws ApplicationException {
		return (confirmationService.get(id) != null) + "";
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, value = "/{id}")
	public void confirmUser(@PathVariable("id") String confirmationId,
	                        @RequestParam("password") String password,
	                        @RequestParam("firstname") String firstName,
	                        @RequestParam("lastname") String lastName,
	                        @RequestParam("gender") String gender) throws ApplicationException {
		confirmationService.confirm(confirmationId, password, firstName, lastName, GenderType.valueOf(gender));
	}
}
