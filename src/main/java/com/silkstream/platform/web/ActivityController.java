package com.silkstream.platform.web;

import com.silkstream.platform.service.ActivityService;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequestMapping(value = "/api/activity", produces = "application/json")
public class ActivityController extends BasicController {
	@Inject
	protected ActivityService activityService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseBody
	public List<String> all() throws Exception{
		return activityService.all();
	}


}
