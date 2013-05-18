package com.silkstream.platform.service;

import com.silkstream.platform.enums.GenderType;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.db.UserConfirmation;
import com.silkstream.platform.exception.WrongPasswordException;
import javax.inject.Inject;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("confirmationService")
public class ConfirmationService extends BasicService {

	@Inject
	private ShaPasswordEncoder encoder;
	@Inject
	private UserService userService;
	@Inject
	private MailService mailService;

	public UserConfirmation get(String id) {
		return mapper.load(UserConfirmation.class, id);
	}

	public void deleteConfirmationsByUser(User user) {
		List<UserConfirmation> list = mapper.scanWith(UserConfirmation.class, "uid", user.getId());
		if (list != null) {
			for (UserConfirmation confirmation : list) {
				mapper.delete(confirmation);
			}
		}
	}

	public void confirm(String confirmationId, String password, String firstName, String lastName, GenderType gender) {
		UserConfirmation confirmation = get(confirmationId);
		if (confirmation != null) {
			User user = userService.get(confirmation.getUserId());
			if (user != null) {
				if (encoder.isPasswordValid(user.getPassword(), password, user.getCreatedDate())) {
					user.setLastName(lastName);
					user.setFirstName(firstName);
					user.setGender(gender);
					user.setEnabled(true);
					deleteConfirmationsByUser(user);
					userService.login(user); // Add user to authentication
					mailService.sendWelcome(user);
				} else {
					throw new WrongPasswordException("Password does not match our records per the confirmation id.");
				}
			}
		}
	}
}
