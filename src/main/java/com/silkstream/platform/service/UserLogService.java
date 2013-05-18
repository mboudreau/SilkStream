package com.silkstream.platform.service;


import com.silkstream.platform.enums.ActionType;
import com.silkstream.platform.enums.SearchIndexType;
import com.silkstream.platform.enums.UserLogType;
import com.silkstream.platform.exception.user.UserDoesntExistException;
import com.silkstream.platform.models.db.*;
import com.silkstream.platform.models.dto.SearchResults;
import com.silkstream.platform.models.dto.UserLogItem;
import com.silkstream.platform.models.dto.adapter.UserLogAdapter;
import com.silkstream.platform.models.dto.google.ResultInfo;
import javax.inject.Inject;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("userLogService")
public class UserLogService extends BasicService {
	@Inject
	protected ShaPasswordEncoder encoder;
	@Inject
	protected CloudSearchService cloudSearchService;
	@Inject
	protected MailService mailService;
	@Inject
	protected TivityService tivityService;
	@Inject
	protected UserService userService;
	@Inject
	protected PlaceService placeService;
	@Inject
	UserLogAdapter userLogAdapter;

	public void save(UserLog userLog) {
		if (userLog != null) {
			userLog.setCreatedDate(System.currentTimeMillis());
			userLog.setId(createId());
			mapper.clobber(userLog);
			if (userLog.getAction() != null && userLog.getTarget() != null) {
				cloudSearchService.addItem(
						SearchIndexType.LOG,
						userLog.getId(),
						userLog.getUserid(),
						userLog.getAction().toString(),
						userLog.getTarget().toString(),
						new BigDecimal(0),
						new BigDecimal(0),
						userLog.getCreatedDate(),
						userLog.getCreatedDate()
				);
			}
		}
	}

	public UserLog load(String id) {
		return load(id,null);
	}

	public UserLog load(String id,List<String> attributesToGet) {
		if (id != null) {
			return mapper.load(UserLog.class, id,attributesToGet);
		}
		return null;
	}

	public UserLog delete(String id) {
		if (id != null) {
			UserLog log = load(id);
			if (log != null) {
				return delete(log);
			}
		}
		return null;
	}

	public UserLog delete(UserLog log) {
		if (log != null) {
			if (log.getReputationChange() != 0L) {
				User user = userService.get(log.getUserid());
				if (user != null) {
					user.setReputation(user.getReputation() - log.getReputationChange());
					userService.update(user);
				}
			}
			mapper.delete(log);
		}
		return null;
	}

	public SearchResults<UserLogItem> loadLogsFromUser(String userId) {
		return loadLogsFromUser(userId, null);
	}

	public List<UserLog> getAllLogsOnTarget(String targetId) {
		return mapper.scanWith(UserLog.class, "ti", targetId);
	}

	public SearchResults<UserLogItem> loadLogsFromUser(String userId, Integer num) {
		num = num == null ? 10 : num;
		SearchResults<UserLogItem> searchLogs = new SearchResults<UserLogItem>();
		SearchResults<ResultInfo> logs = cloudSearchService.search(SearchIndexType.LOG, userId, num);

		List<UserLogItem> userLogs = new ArrayList<UserLogItem>();
		if (logs != null) {
			for (ResultInfo log : logs.getList()) {
				UserLog userLog = load(log.getId());
				if (userLog != null) {
					if (userLog.getTarget() != null) {
						if (userLog.getTarget().equals(UserLogType.ANSWER)) {
							Tivity tivity = tivityService.get(userLog.getTargetId());
							if (tivity != null) {
								Place place = placeService.get(tivity.getLocationId());
								if (place != null) {
									UserLogItem userLogItem = userLogAdapter.buildCompleteUserLogItem(userLog, tivity.getActivity(), place.getDescription());
									userLogs.add(userLogItem);
								}
							}
						}
					}
				}
			}
		}
		// TODO: need the count to make it a real search service
		searchLogs.setList(userLogs);
		searchLogs.setCount(logs.getCount());

		return searchLogs;
	}

	public UserLog create(UserLogType logType, String targetId, String userId, ActionType actionType, Long repChange) {
		UserLog userLog = new UserLog();
		if (actionType != null) {
			userLog.setAction(actionType);
		}
		userLog.setCreatedDate(System.currentTimeMillis());
		userLog.setId(createId());
		if (repChange != null) {
			userLog.setReputationChange(repChange);
		}
		if (logType != null) {
			userLog.setTarget(logType);
		}
		if (targetId != null) {
			userLog.setTargetId(targetId);
		}
		if (userId != null) {
			userLog.setUserid(userId);
			User user = userService.get(userId);
			if (user != null) {
				if (user.getReputation() == null) {
					user.setReputation(0L);
				}
				if (repChange != null) {
					user.setReputation(user.getReputation() + repChange);
				}

				// TODO: change this to update any currently logged in user
				// Check if current user is the same as the one logged in, increment reputation
				if (user.getId() == getUser().getId()) {
					getUser().setReputation(user.getReputation());
				}

				mapper.clobber(user);
				userService.addUserToContext(user);
				mapper.clobber(userLog);
				if (actionType != null && logType != null && userId != null) {
					cloudSearchService.addItem(
							SearchIndexType.LOG,
							userLog.getId(),
							userId,
							actionType.toString(),
							logType.toString(),
							new BigDecimal(0),
							new BigDecimal(0),
							userLog.getCreatedDate(),
							userLog.getCreatedDate()
					);
				}
				return userLog;
			}
		}
		throw new UserDoesntExistException();
	}
}
