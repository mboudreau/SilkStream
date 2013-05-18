package com.silkstream.platform.service;

import com.amazonaws.services.dynamodb.model.*;
import com.silkstream.platform.enums.ModelType;
import com.silkstream.platform.models.db.Invitation;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.utils.Followed;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("invitationService")
public class InvitationService extends BasicService {
	@Inject
	private UserService userService;
	@Inject
	private ReputationService reputationService;

	public void save(Invitation userInvitation) {
		mapper.save(userInvitation);
	}

	public Invitation get(String id) {
		return get(id,null);
	}

	public Invitation get(String id,List<String> attributeToGet) {
		return mapper.load(Invitation.class, id,attributeToGet);
	}

	public List<Invitation> loadUsingFrom(String fromId) {
		if (fromId != null) {
			return mapper.scanWith(Invitation.class,"fr",fromId);
		}
		return null;
	}

	public List<Invitation> loadUsingTo(String facebookId, String email) {
		if (facebookId != null || email != null) {
			ScanRequest scanRequest = new ScanRequest(mapper.getTieredTableName(Invitation.class));
			Map<String, Condition> scanFilter = new HashMap<String, Condition>();
			List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
			if (facebookId != null) {
				attributeValues.add(new AttributeValue(facebookId));
			}
			if (email != null) {
				attributeValues.add(new AttributeValue(email));
			}
			Condition condition = new Condition();
			condition.setAttributeValueList(attributeValues);
			condition.setComparisonOperator(ComparisonOperator.IN);
			scanFilter.put("to", condition);
			scanRequest.setScanFilter(scanFilter);

			ScanResult scanResult = mapper.scanWithScanRequest(scanRequest);

			List<Invitation> userInvitations = new ArrayList<Invitation>();
			for (Map<String, AttributeValue> object : scanResult.getItems()) {
				Invitation userInvitation = mapper.marshallIntoObject(Invitation.class, object);
				userInvitations.add(userInvitation);
			}

			return userInvitations;
		}
		return null;
	}

	public void userRegistered(User invitedUser) {
		if (invitedUser != null) {
			List<Invitation> userInvitations = loadUsingTo(invitedUser.getFacebookId(), invitedUser.getEmail());
			if (userInvitations != null) {
				for(Invitation userInvitation : userInvitations){
					User referee = userService.get(userInvitation.getFrom());
					referee.setReputation(referee.getReputation()+reputationService.USER_INVITE_WENT_THROUGH);
					if(referee.getFolloweds()==null){
						referee.setFolloweds(new ArrayList<Followed>());
					}
					Followed followed = new Followed();
					followed.setModelType(ModelType.USER);
					followed.setId(invitedUser.getId());
					referee.getFolloweds().add(followed);
					mapper.save(referee);
				}
			}
		}
	}
}
