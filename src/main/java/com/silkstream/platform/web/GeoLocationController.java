package com.silkstream.platform.web;

import com.silkstream.platform.enums.GoogleType;
import com.silkstream.platform.models.db.Place;
import com.silkstream.platform.models.utils.GeoLocation;
import com.silkstream.platform.service.GeoIpService;
import com.silkstream.platform.service.GoogleService;
import com.silkstream.platform.utils.InetAddressUtil;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/api/geolocation", produces = "application/json")
public class GeoLocationController extends BasicController {
	@Inject
	protected GoogleService googleService;
	@Inject
	protected GeoIpService geoIpService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, params = {"lat","lon"})
	@ResponseBody
	public GeoLocation getGeoLocationFromLatLon(@RequestParam("lat") BigDecimal lat, @RequestParam("lon") BigDecimal lon) throws MalformedURLException, IOException {
		return googleService.getGeoLocation(lat, lon);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public GeoLocation getGeoLocationFromIp(HttpServletRequest request) throws IOException {
		return geoIpService.getGeoLocationFromIp(InetAddressUtil.getAddressFromRequest(request));
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, params = "query")
	@ResponseBody
	public List<GeoLocation> getGeoLocationsFromQuery(@RequestParam("query") String query) throws Exception {
		List<GeoLocation> geoLocations = new ArrayList<GeoLocation>();
		for (Place location :  googleService.getAutoCompleteFromQuery(query, GoogleType.CITIES)){
			geoLocations.add(location.getGeoLocation());
		}
        if (geoLocations.size() == 0) {
	        geoLocations.addAll(googleService.getGeoLocation(query));
        }
		return geoLocations;
	}
}
