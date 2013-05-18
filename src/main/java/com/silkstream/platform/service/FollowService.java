package com.silkstream.platform.service;

import com.silkstream.platform.enums.ModelType;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.utils.Followed;

import java.util.ArrayList;
import java.util.List;

public class FollowService extends BasicService {

	public Boolean isFollowing(User user, ModelType type, String id) {
		if (user != null && user.getFolloweds() != null) {
			for (Followed followed : user.getFolloweds()) {
				if (followed.getModelType() == type && followed.getId().equals(id)) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Followed> removeSpecificId(List<Followed> list, String id) {
		List<Followed> copy = new ArrayList<Followed>(list);
		for (Followed followed : list) {
			if (followed.getId().equals(id)) {
				copy.remove(followed);
			}
		}
		return copy;
	}

}
