package com.silkstream.webapp.web;

import com.silkstream.platform.models.BeanstalkProperties;
import com.silkstream.platform.web.BasicController;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class WebController extends BasicController{
	@Inject
	private BeanstalkProperties properties;


	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@RequestMapping(value = "api/**", method = RequestMethod.GET)
	public ModelAndView wrongApi(ModelAndView model) {
		model.addObject("Error", "Wrong api called");
		model.setViewName("error");
		return model;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accessDenied(ModelAndView model) {
		//model.addObject("ApplicationException", "Wrong api called");
		model.setViewName("error");
		return model;
	}

	@RequestMapping(value = "/**", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model) {
		model.addObject("maintenance", properties.isMaintenance());
		model.addObject("environment", properties.getEnvironment());
		model.setViewName(properties.isMaintenance() ? "maintenance" : "index");
		return model;
	}
}
