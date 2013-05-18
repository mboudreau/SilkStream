package com.silkstream.platform.service;

import com.silkstream.platform.models.utils.GeoLocation;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

@Service("geoIpService")
public class GeoIpService {

	@Inject
	GoogleService googleService;

	public GeoLocation getGeoLocationFromIp(String ip) throws IOException {
		if (ip == null || ip.length() == 0 || ip.matches("(127(\\.\\d+){1,3}|[0:]+1|localhost)")) {
			return null;
		}

		URL targetUrl = new URL("http://freegeoip.net/json/" + ip);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readValue(targetUrl, JsonNode.class);
		if (node != null) {
			if (node.get("latitude") != null && node.get("longitude") != null) {
				BigDecimal lat  = new BigDecimal(node.get("latitude").getTextValue());
				BigDecimal lon  = new BigDecimal(node.get("longitude").getTextValue());
				GeoLocation l = googleService.getGeoLocation(lat, lon);
				l.setPrecise(false);
				return l;
			}
		}
		return null;
	}
}
