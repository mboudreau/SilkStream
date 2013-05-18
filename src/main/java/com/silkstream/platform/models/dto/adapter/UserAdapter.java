package com.silkstream.platform.models.dto.adapter;

import com.silkstream.platform.enums.UserRole;
import com.silkstream.platform.models.dto.UserItem;
import com.silkstream.platform.models.db.User;
import org.springframework.stereotype.Service;

@Service("UserAdapter")
public class UserAdapter {

	public UserItem buildCompleteUserItem(User user) {
		UserItem userItem = new UserItem();
		if (user != null) {
			userItem.setId(user.getId());
			userItem.setFirstName(user.getFirstName());
			userItem.setLastName(user.getLastName());
			userItem.setPictureUrl(user.getPictureUrl());
			userItem.setReputation(user.getReputation());
			userItem.setGender(user.getGender());
			userItem.setActivities(user.getActivities());
			userItem.setLoginCount(user.getLoginCount());
			userItem.setEnable(user.isEnabled());
			userItem.setAvailability(user.getAvailability());
			userItem.setFolloweds(user.getFolloweds());
			if (user.getRole() != null) {
				userItem.setRole(user.getRole().name());
			}
			userItem.setLocations(user.getLocations());
			userItem.setFacebookId(user.getFacebookId());
			userItem.setBio(user.getBio());
		}
		return userItem;
	}

	public User buildCompleteUser(UserItem userItem) {
		User user = new User();
		if (userItem != null) {
			user.setId(userItem.getId());
			user.setFirstName(userItem.getFirstName());
			user.setLastName(userItem.getLastName());
			user.setPictureUrl(userItem.getPictureUrl());
			user.setReputation(userItem.getReputation());
			user.setGender(userItem.getGender());
			user.setActivities(userItem.getActivities());
			user.setLoginCount(userItem.getLoginCount());
			user.setRole(UserRole.valueOf(userItem.getRole()));
			user.setEnabled(userItem.getEnable() == null?false:userItem.getEnable());
			user.setFolloweds(userItem.getFolloweds());
			user.setAvailability(userItem.getAvailability());
			user.setFacebookId(userItem.getFacebookId());
			user.setBio(userItem.getBio());
		}
		return user;
	}
}
