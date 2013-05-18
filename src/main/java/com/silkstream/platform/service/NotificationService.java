package com.silkstream.platform.service;


import com.amazonaws.services.dynamodb.datamodeling.DynamoDBScanExpression;
import com.silkstream.platform.enums.SearchIndexType;
import com.silkstream.platform.models.db.Notification;
import com.silkstream.platform.validator.NotificationValidator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service("notificationService")
public class NotificationService extends BasicService {
	@Inject
	private UserService userService;
	@Inject
	private CloudSearchService cloudSearchService;
	@Inject
	private MailService mailService;
	@Inject
	private TivityService tivityService;
	@Inject
	private PlaceService placeService;

	/*@Scheduled(fixedDelay = 1000 * 60 * 5) // Every 5 minutes
	public void sendNotifications() {
		if (properties.getEnvironment().equals(EnvironmentType.PROD)) {
			List<Notification> notifications = getNotificationToSend();
			for (Notification notification : notifications) {
				User user = userService.get(notification.getTo());
				if (user != null) {
					if (notification.getNotificationType().equals(NotificationType.ADDED_TIVITY_TO_LOCATION)) {

						Tivity silkstream = tivityService.get(notification.getFrom());
						if (silkstream != null) {
							Place place = placeService.get(silkstream.getLocationId());
							if (place != null) {
								Date date = new Date(silkstream.getStartTime());
								DateFormat format = new SimpleDateFormat("EEEE, MMM d 'at' h a");
								format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
								String formatted = format.format(date);
								System.out.println(formatted);
								format.setTimeZone(TimeZone.getTimeZone("New York City/United States"));
								formatted = format.format(date);

								String content = "You follow " + place.getName() + " on Tivity and for good reason...how else would you know that " + silkstream.getActivity() + " has just been scheduled for " + formatted + " ?";
								String link = "silkstream/" + silkstream.getId();
								String subject = "Tivity is taking place at " + place.getName();
								mailService.sendNotification(user, subject, content, link);
								notificationSent(notification);
							}
						}
					} else if (notification.getNotificationType().equals(NotificationType.LOCATION_FOLLOWED)) {
						Place place = placeService.get(notification.getFrom());
						if (place != null) {
							String content = "Great news - You're now following " + place.getName() + " and will be notified each time something active is happening there.";
							String link = "place/" + place.getId();
							String subject = "You're now following " + place.getName() + " on Tivity";
							mailService.sendNotification(user, subject, content, link);
							notificationSent(notification);
						}
					}
				} else {
					delete(notification);
				}
			}
		}
	}*/

	private void notificationSent(Notification notification) {
		notification.setSentFlag(true);
		update(notification);
	}

	public Notification get(String id) {
		return get(id, null);
	}

	public Notification get(String id, List<String> attributesToGet) {
		if (id != null) {
			return mapper.load(Notification.class, id, attributesToGet);
		}
		return null;
	}

	public void delete(Notification notification) {
		mapper.delete(notification);
		cloudSearchService.deleteItem(notification.getId());
	}

	public void update(Notification notification) {
		if (notification != null) {
			mapper.save(notification);
			indexNotification(notification);
		}
	}

	public Notification add(Notification notification) {
		if (notification != null) {
			notification.setCreatedDate(now());
			notification.setId(createId());
			NotificationValidator validator = new NotificationValidator();
			List<Error> errors = validator.validate(notification);
			if (errors.size() == 0) {
				indexNotification(notification);
				mapper.clobber(notification);
			}
		}
		return null;
	}

	private List<Notification> getNotificationToSend() {
		List<Notification> notifications = mapper.scan(Notification.class, new DynamoDBScanExpression());
		List<Notification> res = new ArrayList<Notification>();
		Long now = System.currentTimeMillis();
		for (Notification notification : notifications) {
			if (!notification.isSentFlag()) {
				if (notification.getSendDate() == null || notification.getSendDate() < now) {
					res.add(notification);
				}
			}
		}
		return res;
	}

	private void indexNotification(Notification notification) {
		cloudSearchService.addItem(
				SearchIndexType.NOTIFICATION,
				notification.getId(),
				notification.getTo(),
				null,
				null,
				null,
				null,
				notification.getCreatedDate(),
				null,
				null,
				null);
	}

}
