package com.silkstream.platform.service;

import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.silkstream.platform.models.BeanstalkProperties;
import com.silkstream.platform.models.db.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import java.util.UUID;

public class BasicService {
	@Inject
	protected TivityMapperService mapper;
	@Inject
	protected AmazonDynamoDB client;
	@Inject
	protected BeanstalkProperties properties;

	protected User getUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			return (User) principal;
		}
		return null;
	}

	protected Authentication getAuth() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	protected long now() {
		return System.currentTimeMillis() / 1000;
	}

	protected String createId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
