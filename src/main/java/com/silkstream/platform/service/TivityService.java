package com.silkstream.platform.service;

import com.amazonaws.services.dynamodb.model.*;
import com.silkstream.platform.enums.NotificationType;
import com.silkstream.platform.enums.SearchIndexType;
import com.silkstream.platform.models.db.Notification;
import com.silkstream.platform.models.db.Place;
import com.silkstream.platform.models.db.Tivity;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.web.PlaceController;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("TivityService")
public class TivityService extends BasicService {
	@Inject
	protected UserService userService;
	@Inject
	protected PlaceController placeController;
	@Inject
	protected PlaceService placeService;
	@Inject
	protected GoogleService googleService;
	@Inject
	protected UserLogService userLogService;
	@Inject
	protected CloudSearchService cloudSearchService;
	@Inject
	protected ReputationService reputationService;
	@Inject
	protected CommentService commentService;
	@Inject
	protected MailService mailService;
	@Inject
	protected NotificationService notificationService;

	/*@Scheduled(fixedDelay = 1000 * 60 * 15) // Every 15 minutes
	public void cleanUp() {
		// TODO: create scan that searches for end time lower than current time
		List<Tivity> tivities = mapper.scan(Tivity.class, new DynamoDBScanExpression());
		Long now = System.currentTimeMillis();
		for (Tivity silkstream : tivities) {
			if (silkstream.getEndTime() != null) {
				boolean enable = (silkstream.getEnabled() == null)? true : silkstream.getEnabled();
				silkstream.setEnabled(enable);
				if (silkstream.getEnabled() && silkstream.getEndTime() < now) {
					User user = userService.get(silkstream.getUserId());
					Place place = placeService.get(silkstream.getLocationId());
					silkstream.setEnabled(false);
					save(silkstream, user, place);
				}
			}
		}
	}*/

	public void clobber(Tivity tivity) {
		if (tivity != null) {
			mapper.clobber(tivity);
		}
	}

	public void save(Tivity tivity) {
		if (tivity != null) {
			mapper.save(tivity);
		}
	}

	public void save(Tivity tivity, User user, Place place) {
		if (tivity != null) {
			tivity.setUpdatedDate(System.currentTimeMillis());
			mapper.save(tivity);
			indexTivity(tivity, place, user);
		}
	}

	public void disable(String id) {
		if (id != null) {
			Tivity tivity = get(id);
			if (tivity != null) {
				tivity.setEnabled(false);
				User user = userService.get(tivity.getUserId());
				Place place = placeService.get(tivity.getLocationId());
				this.save(tivity, user, place);
			}
		}
	}

	public List<Tivity> getTivitiesFromPlace(String placeId) {
		if (placeId != null) {
			return mapper.scanWith(Tivity.class, "lo", placeId);
		}
		return null;
	}

	public List<Tivity> getTivitiesFromPlaces(List<String> placeIds) {
		ScanRequest scanRequest = new ScanRequest();
		scanRequest.setTableName(mapper.getTieredTableName(Tivity.class));
		Map<String, Condition> conditionMap = new HashMap<String, Condition>();
		Condition scanFilterCondition = new Condition();
		List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
		for (String id : placeIds) {
			attributeValues.add(new AttributeValue().withS(id));
		}
		scanFilterCondition.setAttributeValueList(attributeValues);
		scanFilterCondition.setComparisonOperator(ComparisonOperator.IN);
		conditionMap.put("lo", scanFilterCondition);
		scanRequest.setScanFilter(conditionMap);

		ScanResult scanResult = mapper.scanWithScanRequest(scanRequest);
		List<Tivity> tivities = new ArrayList<Tivity>();
		if (scanResult.getItems() != null) {
			for (Map<String, AttributeValue> item : scanResult.getItems()) {
				tivities.add(mapper.marshallIntoObject(Tivity.class, item));
			}
		}
		return tivities;
	}

	public void add(Tivity tivity, User user, Place place) {
		if (tivity != null) {
			tivity.setCreatedDate(System.currentTimeMillis());
			tivity.setUpdatedDate(System.currentTimeMillis());
			tivity.setId(createId());
			mapper.clobber(tivity);
			indexTivity(tivity, place, user);
			createNotification(tivity);
			// NEVER SEND EMAIL TO USER THAT JUST CREATED TIVITY
			//mailService.sendTivityCreation(silkstream.getId(),user);
		}
	}

	public Tivity get(String id) {
		return get(id, null);
	}

	public Tivity get(String id, List<String> attributesToGet) {
		if (id != null) {
			return mapper.load(Tivity.class, id, attributesToGet);
		}
		return null;
	}

	private void createNotification(Tivity tivity) {
		List<User> users = userService.getUsersThatFollowThisId(tivity.getLocationId());
		for (User user : users) {
			Notification notification = new Notification();
			notification.setFrom(tivity.getId());
			notification.setTo(user.getId());
			notification.setNotificationType(NotificationType.ADDED_TIVITY_TO_LOCATION);
			notificationService.add(notification);
		}
	}

	private void indexTivity(Tivity tivity, Place place, User user) {
		Integer points = 0;
		if (tivity.getFavoritedUser() != null) {
			points = (tivity.getFavoritedUser() == null) ? 0 : tivity.getFavoritedUser().size();
		}
		if (place != null && place.getGeoLocation() != null && user != null) {
			cloudSearchService.addItem(
					SearchIndexType.ANSWER,
					tivity.getId(),
					user.getId(),
					place.getName(),
					tivity.getActivity(),
					place.getGeoLocation().getLat(),
					place.getGeoLocation().getLon(),
					tivity.getCreatedDate(),
					tivity.getUpdatedDate(),
					tivity.getPromoted(),
					points,
					tivity.getEnabled()
			);
		}
	}
}
