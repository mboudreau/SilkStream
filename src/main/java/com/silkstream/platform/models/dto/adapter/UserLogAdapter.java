package com.silkstream.platform.models.dto.adapter;

import com.silkstream.platform.models.db.UserLog;
import com.silkstream.platform.models.dto.UserLogItem;
import org.springframework.stereotype.Service;

@Service("UserLogAdapter")
public class UserLogAdapter {

	public UserLogItem buildCompleteUserLogItem(UserLog userLog, String activity, String description) {


		if (userLog != null) {
			UserLogItem userLogItem = new UserLogItem();
			userLogItem.setId(userLog.getId());
			userLogItem.setReputationChange(userLog.getReputationChange());
			userLogItem.setCreatedDate(userLog.getCreatedDate());
			userLogItem.setTarget(userLog.getTarget());
			userLogItem.setTargetId(userLog.getTargetId());
			userLogItem.setAction(userLog.getAction());
			if (activity != null) {
				userLogItem.setActivity(activity);
			}
			if (description != null) {
				userLogItem.setDescription(description);
			}
			return userLogItem;
		}

		return null;
	}

	public UserLog buildCompleteUserLog(UserLogItem userLogItem) {
		UserLog userLog = new UserLog();
		if (userLogItem != null) {
			userLog.setAction(userLogItem.getAction());
			userLog.setCreatedDate(userLogItem.getCreatedDate());
			userLog.setId(userLogItem.getId());
			userLog.setReputationChange(userLogItem.getReputationChange());
			userLog.setTarget(userLogItem.getTarget());
			userLog.setTargetId(userLogItem.getTargetId());
			userLog.setUserid(userLogItem.getUserId());
		}
		return userLog;
	}

}
