package com.silkstream.platform.service;


import com.silkstream.platform.models.dto.meetup.MeetupGroups;
import com.silkstream.platform.models.dto.meetup.MeetupGroupsApiData;
import org.codehaus.jackson.map.ObjectMapper;
import javax.inject.Inject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("meetupService")
public class MeetupService {

	@Inject
	private ActivityService activityService;


	private final String key = "3e48386446586f557e71183d5d7f1e5a";
	private final int radius = 25;
	private final String lat = "40.714623";
	private final String lon = "-74.006605";
	private final String meetupData = "backups/meetupdata.json";
	private final String subject = "promote your meet-up group to the right audience\n";
	private final String message = "Hey Meetup Organizer,\n" +  //TODO - add the name instead of Meetup Organizer
			"\n" +
			"Allow me to introduce you to Tivity (http://www.silkstream.us), a crowd-sourced knowledge base of local athletic activities. We understand that even with all of the great technology out there, it's still too hard to find local pick-up game locations, groups and events so we decided to create a web application that will help expand the reach and connection needed to do so. Use Tivity as a way to promote your current MeetUp groups, or find others who'd be interested in joining you. It's completely free and dedicated to the athletic activities that you're most interested in.\n" +
			"\n" +
			"Give it a try and let us know what you think.\n" +
			"\n" +
			"See you out there!\n" +
			"\n" +
			"- Team Tivity";


	private String accessToken;
	private Set<String> organizersIds = new HashSet<String>();
	private MeetupGroups meetupGroups = new MeetupGroups();


	public void sendMessage(String token) throws Exception {
		setAccessToken(token);

		ObjectMapper parser = new ObjectMapper();
		MeetupGroups data = parser.readValue(new File(meetupData), MeetupGroups.class);

		String id = "871897"; // mister Boudj
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new FormHttpMessageConverter());
		restTemplate.setMessageConverters(list);

		Resource resource = new ClassPathResource("activities.json");
		List<MeetupGroupsApiData> updatedList = new ArrayList<MeetupGroupsApiData>(data.getResults());
		try {
			int total = 0;
			while (total < 12) {
				for (MeetupGroupsApiData groups : data.getResults()) {
					MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
					if (groups.getOrganizer() != null) {
						parts.add("member_id", groups.getOrganizer().getMember_id());
						parts.add("subject", this.subject);
						parts.add("message", this.message);
						parts.add("logo", resource);

						restTemplate.postForLocation(buildMessagesUrl(id), parts);
					}

					updatedList.remove(groups);
					total++;
				}
			}
		} catch (Exception e) {
			//In case we go over the limit of messages
		}

		data.setResults(updatedList);
		parser.writeValue(new File(meetupData), data);


	}

	public void getData() throws Exception {
		meetupGroups.setResults(new ArrayList<MeetupGroupsApiData>());
		ObjectMapper parser = new ObjectMapper();
		List<String> activities = new ArrayList<String>();
		activities = activityService.all();
		for (String topic : activities) {
			try {
				MeetupGroups result = parser.readValue(getRawJsonFromUrl(buildGroupsUrl(topic)), MeetupGroups.class);
				handleData(result);
				meetupGroups.setMeta(result.getMeta());
				while (!result.getMeta().getNext().equals("")) {
					result = parser.readValue(getRawJsonFromUrl(buildGroupsUrl(result.getMeta().getNext())), MeetupGroups.class);
					handleData(result);
				}
			} catch (Exception e) {
				//When a topic isn't valid this is what happens !
			}
		}
		parser.writeValue(new File(meetupData), this.meetupGroups);
	}

	private void handleData(MeetupGroups result) {
		for (MeetupGroupsApiData data : result.getResults()) {
			if (organizersIds.add(data.getOrganizer().getMember_id() + "")) {
				meetupGroups.getResults().add(data);
			}
		}
	}

	private String getRawJsonFromUrl(String url) throws Exception {
		String result = "";
		URL oracle = new URL(url);
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			result += inputLine;
		in.close();
		return result;
	}

	private String buildGroupsUrl(String topic) {
		String url = "https://api.meetup.com/2/groups.json?topic=" + topic + "&key=" + key + "&lat=" + lat + "&lon=" + lon + "&radius=" + radius;
		return url;
	}

	private String buildMessagesUrl(String id) {
		String url = "https://api.meetup.com/2/message?access_token=" + getAccessToken();
		/*url+="&member_id="+id;
		url+="&subject=upcoming talks";
		url+="&message=looking for speakers for my upcoming meetup. are you interested?";*/
		return url;
	}


	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
