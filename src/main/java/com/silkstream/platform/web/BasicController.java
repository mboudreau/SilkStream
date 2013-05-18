package com.silkstream.platform.web;

import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.javadocmd.simplelatlng.LatLng;
import com.silkstream.platform.enums.EnvironmentType;
import com.silkstream.platform.exception.ApplicationException;
import com.silkstream.platform.models.BeanstalkProperties;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.service.MailService;
import com.silkstream.platform.service.TivityMapperService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.UUID;

public class BasicController {
	//http://blog.cuttleworks.com/2011/12/spring-restful-controllers-and-error-handling/


	@Inject
	protected TivityMapperService mapper;
	@Inject
	protected AmazonDynamoDB client;
	@Inject
	protected BeanstalkProperties properties;
	@Inject
	protected MailService mailService;


	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ModelAndView handleApplicationException(Exception ex) {
		ModelAndView model = new ModelAndView();
		model.addObject("Error", "Wrong api called");
		model.setViewName("error");
		if (properties.getEnvironment().equals(EnvironmentType.PROD)) {
			if (ex instanceof ApplicationException) {
				mailService.sendException(ex);
			}
		}
		return model;
	}

	protected long now() {
		return System.currentTimeMillis() / 1000;
	}

	protected String createId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	protected User getUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			return (User) principal;
		}
		return null;
	}

	protected double bearing(LatLng point1, LatLng point2) {
		double dLat = Math.toRadians(point2.getLatitude() - point1.getLatitude());
		double dLon = Math.toRadians(point2.getLongitude() - point1.getLongitude());
		double lat1 = Math.toRadians(point1.getLatitude());
		double lat2 = Math.toRadians(point2.getLatitude());

		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) -
				Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
		double brng = Math.toDegrees(Math.atan2(y, x));
		return brng;
	}
}
