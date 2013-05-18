package com.silkstream.platform.security;

import com.silkstream.platform.service.UserService;
import javax.inject.Inject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import com.silkstream.platform.models.db.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TivityRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {

	@Inject
	private UserService userService;

	public TivityRememberMeAuthenticationFilter(AuthenticationManager authenticationManager,
	                                      RememberMeServices rememberMeServices) {
		super(authenticationManager,rememberMeServices);
	}

	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
	                                          Authentication authResult) {
		User user = (User) authResult.getPrincipal();
		user = userService.incrementLoginCount(user);
		userService.update(user);

	}
}
