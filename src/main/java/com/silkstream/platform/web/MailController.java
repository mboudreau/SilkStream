package com.silkstream.platform.web;

import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.dto.Mail;
import com.silkstream.platform.service.MailService;
import com.silkstream.platform.validator.MailValidator;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/mail", produces = "application/json")
public class MailController {
	@Inject
	private MailService mailService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{type}", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void sendEmail(@RequestBody Mail mail, @PathVariable("type") String type) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			User user = (User) principal;
			mail.setEmail(user.getEmail());
			mail.setName(user.getFirstName() + " " + user.getLastName());
		}
		mail.setSubject("[" + type.toUpperCase() + "] " + mail.getSubject());
		sendSupportEmail(mail);
	}

	protected void sendSupportEmail(Mail mail) {
		MailValidator validator = new MailValidator();
		List<Error> errors = validator.validate(mail);
		if (errors.size() == 0) {
			mailService.sendSupportEmail(mail.getEmail(), mail.getName(), mail.getSubject(), mail.getMessage());
		} else {
			throw new RuntimeException(errors.toString());
		}
	}
}
