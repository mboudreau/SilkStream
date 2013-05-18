package com.silkstream.platform.service;


import com.silkstream.platform.exception.JsonParserCouldntReadFileException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service("activityService")
public class ActivityService extends BasicService {

	List<String> activities;

	public ActivityService() {
	}

	public List<String> all() throws FileNotFoundException {
		if (activities == null) {
			ObjectMapper mapper = new ObjectMapper();
			Resource resource = new ClassPathResource("activities.json");
			try {
				activities = mapper.readValue(resource.getFile(), List.class);
			} catch (IOException e) {
				throw new JsonParserCouldntReadFileException();
			}
			// sort
			java.util.Collections.sort(activities);
		}
		return activities;
	}
}
