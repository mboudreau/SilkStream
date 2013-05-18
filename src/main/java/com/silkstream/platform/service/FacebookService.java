package com.silkstream.platform.service;


import com.silkstream.platform.enums.FacebookPermission;
import com.silkstream.platform.exception.FacebookPermissionException;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.dto.FBPermissions;
import com.silkstream.platform.models.dto.UserItem;
import com.silkstream.platform.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import javax.inject.Inject;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.social.facebook.api.Reference;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class serves to create new users, login and authorize to return users
 */
@Service("facebookService")
public class FacebookService extends BasicService {

	@Inject
	private UserService userService;

	private final String facebookAppId = "103127473159068";
	private final String facebookAppSecret = "d6825c6b7c6df34b1cb299ba4625bdad";

	public void publishOnFriendWall(User friend, String title, String message, User user) {
		publishOnFriendWall(friend, title, message, user);
	}

	public void publishOnFriendWall(User friend, String title, String message, User user, String url) {
		if (StringUtil.isNotNullOrEmpty(user.getSocialToken())) {
			user = refreshToken(user);
			url = url == null ? "http://www.silkstream.us" : url;
			if (checkPermissions(user.getSocialToken(), FacebookPermission.PUBLISH_ACTIONS)) {
				Facebook facebook = new FacebookTemplate(user.getSocialToken());
				facebook.feedOperations().postLink(friend.getFacebookId(), message, new FacebookLink(url, title, "Tivity.us", "What and where, let Tivity take it from there. A simple search is all that stands between you and nearby athletic activities."));
			} else {
				throw new FacebookPermissionException();
			}
		}
	}


	public List<UserItem> getFriendsFromUser(User user, String nameQuery) {
		user = refreshToken(user);
		nameQuery = (nameQuery != null) ? nameQuery : null;

		List<UserItem> userItems = new ArrayList<UserItem>();

		if (user != null) {
			if (StringUtil.isNotNullOrEmpty(user.getSocialToken())) {
				user = refreshToken(user);
				Facebook facebook = new FacebookTemplate(user.getSocialToken());
				if (checkPermissions(user.getSocialToken(), FacebookPermission.INSTALLED)) {
					if (facebook != null) {
						List<Reference> friends = facebook.friendOperations().getFriends();
						for (Reference reference : friends) {
							UserItem userItem = new UserItem();
							if (reference.getName() != null) {
								ArrayList<String> names = new ArrayList<String>(Arrays.asList(reference.getName().split(" ")));
								if (names.size() != 0) {
									userItem.setFirstName(names.remove(0));
									userItem.setLastName(StringUtils.join(names, " "));
								}
							}
							userItem.setFacebookId(reference.getId());
							if (nameQuery != null) {
								if (reference.getName() != null) {
									if (reference.getName().contains(nameQuery)) {
										userItems.add(userItem);
									}
								}
							} else {
								userItems.add(userItem);
							}
						}
					}
				}
			} else {
				throw new FacebookPermissionException();
			}
		}
		return userItems;
	}

	public List<UserItem> getFriendsFromUser(User user) {
		return getFriendsFromUser(user, null);
	}

	public User refreshToken(User user) {
		if (user != null) {
			if (user.getSocialToken() != null) {
				String newToken = refreshToken(user.getSocialToken());
				if (newToken != null && newToken != "") {
					user.setSocialToken(newToken);
					userService.addUserToContext(user);
				}
			}
		}
		return user;
	}

	public String refreshToken(String oldToken) {
		String url = "https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&client_id=" + facebookAppId + "&client_secret=" + facebookAppSecret + "&fb_exchange_token=" + oldToken;
		String response = getRawDataFromUrl(url);
		if (response != null) {
			String[] data = response.split("&");
			if (data.length == 2) {
				data = data[0].split("=");
				if (data.length == 2) {
					return data[1];
				}
			}
		}
		return null;
	}

	public boolean checkPermissions(String token, FacebookPermission permission) {
		String url = "https://graph.facebook.com/me/permissions?access_token=" + token;
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJacksonHttpMessageConverter());
		restTemplate.setMessageConverters(list);
		FBPermissions permissions = restTemplate.getForEntity(url, FBPermissions.class).getBody();
		if (permissions != null) {
			if (permissions.getData() != null) {
				if(permissions.getData().get(0) !=null){
					Integer perm = permissions.getData().get(0).get(permission.toString().toLowerCase());
					if (perm != null) {
						if (perm == 1) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private String getRawDataFromUrl(String url) {
		try {
			String result = "";
			URL oracle = new URL(url);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				result += inputLine;
			in.close();
			return result;
		} catch (Exception e) {
			return null;
		}
	}

}
