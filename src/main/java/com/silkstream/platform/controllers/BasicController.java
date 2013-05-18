package com.silkstream.platform.controllers;

import com.silkstream.platform.models.BeanstalkProperties;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.UUID;

public class BasicController {
	//http://blog.cuttleworks.com/2011/12/spring-restful-controllers-and-error-handling/

	@Inject
	protected BeanstalkProperties properties;

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ModelAndView handleApplicationException(Exception ex) {
		ModelAndView model = new ModelAndView();
		model.addObject("Error", "Wrong api called");
		model.setViewName("error");
		return model;
	}

	protected long now() {
		return System.currentTimeMillis() / 1000;
	}

	protected String createId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
