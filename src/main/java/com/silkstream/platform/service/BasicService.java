package com.silkstream.platform.service;

import com.silkstream.platform.models.BeanstalkProperties;

import javax.inject.Inject;
import java.util.UUID;

public class BasicService {
	@Inject
	protected BeanstalkProperties properties;

	protected long now() {
		return System.currentTimeMillis() / 1000;
	}

	protected String createId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
