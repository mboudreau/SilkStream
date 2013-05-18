package com.silkstream.platform.security.social;

import com.silkstream.platform.enums.GenderType;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.service.MailService;
import com.silkstream.platform.service.UserService;
import javax.inject.Inject;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import java.security.SecureRandom;
import java.util.Random;

public class TivityConnectionSignUp implements ConnectionSignUp {

	@Inject
	private UserService userService;
	@Inject
	private MailService mailService;

	public TivityConnectionSignUp() {
	}

	public String execute(Connection<?> conn) {
		return "";
	}

	public String executeFacebook(Connection<?> conn) {
		ConnectionData connectionData = conn.createData();
		Facebook facebook = new FacebookTemplate(connectionData.getAccessToken());
		FacebookProfile facebookProfile = facebook.userOperations().getUserProfile();
		User user = userService.getWithEmail(facebookProfile.getEmail());
		if (user == null) {
			user = userService.register(
					facebookProfile.getEmail(),
					generateRandomPassword(),
					facebookProfile.getFirstName(),
					facebookProfile.getLastName(),
					GenderType.valueOf(facebookProfile.getGender().toUpperCase()),
					connectionData.getImageUrl(),
					connectionData.getAccessToken(),
			        facebookProfile.getId());
			mailService.sendSignUpFacebook(user);
		}else{
			user.setFirstName(facebookProfile.getFirstName());
			user.setLastName(facebookProfile.getLastName());
			user.setPictureUrl(connectionData.getImageUrl());
			user.setSocialToken(connectionData.getAccessToken());
			user.setFacebookId(facebookProfile.getId());
			user.setGender(GenderType.valueOf(facebookProfile.getGender().toUpperCase()));
		}
		userService.login(user);
		return user.getId();
	}


	//TODO get the email from twitter
	/* public String executeTwitter(Connection<?> conn) {
			ConnectionData connectionData = conn.createData();
			String em = "";
			Twitter twitter = new TwitterTemplate(properties.getTwitterConsumerKey(), properties.getTwitterSecret(), connectionData.getAccessToken(), connectionData.getSecret());

			createUserWithEmail(em);
			return em;
		}*/

	private static String generateRandomPassword() {
		Random random = new SecureRandom();
		int length = 8;
		// Pick from some letters that won't be easily mistaken for each
		// other. So, for example, omit o O and 0, 1 l and L.
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@!#$%^&*()_+.?";

		String pw = "";
		for (int i = 0; i < length; i++) {
			int index = (int) (random.nextDouble() * letters.length());
			pw += letters.substring(index, index + 1);
		}
		return pw;
	}
}
