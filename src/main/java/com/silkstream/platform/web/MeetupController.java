package com.silkstream.platform.web;

import com.silkstream.platform.models.dto.meetup.MeetupOauthResponse;
import com.silkstream.platform.service.MeetupService;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/api/meetup", produces = "application/json")
public class MeetupController extends BasicController {
	@Inject
	protected MeetupService meetupServicee;


	//https://secure.meetup.com/oauth2/authorize?client_id=ucp6enkc56hovqhmpvn0lns440&response_type=code&redirect_uri=http%3A%2F%2Fwww.local.silkstream.us%3A8080%2Fapi%2Fmeetup&scope=messaging

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, params = "code")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseBody
	public void handleMeetupOauthAndSendMessages(@RequestParam("code") String code) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJacksonHttpMessageConverter());
		restTemplate.setMessageConverters(list);
		MeetupOauthResponse response = restTemplate.postForObject("https://secure.meetup.com/oauth2/access?client_id=ucp6enkc56hovqhmpvn0lns440&client_secret=ivcpjo9c2pm7t9k2tljd9fr7kd&grant_type=authorization_code&redirect_uri=http://www.local.silkstream.us:8080/api/meetup&code=" + code + "", null, MeetupOauthResponse.class);
		meetupServicee.sendMessage(response.getAccess_token());
	}
}
