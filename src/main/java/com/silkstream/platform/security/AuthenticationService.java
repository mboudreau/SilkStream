package com.silkstream.platform.security;

import com.silkstream.platform.service.FacebookService;
import com.silkstream.platform.service.TivityMapperService;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.service.UserService;
import javax.inject.Inject;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.NativeWebRequest;

public class AuthenticationService implements UserDetailsService, SignInAdapter {

	@Inject
	protected TivityMapperService mapper;
	@Inject
	protected UserService userService;
	@Inject
	protected FacebookService facebookService;

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException, DataAccessException {
		User user = userService.get(id);
		if (user.getSocialToken() != null) {
			String newToken = facebookService.refreshToken(user.getSocialToken());
			if (newToken != null) {
				user.setSocialToken(newToken);
				mapper.save(user);
			}
			else {
				user = null;
			}
		}
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return user;
	}

	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		return null;
	}
}
