package com.silkstream.platform.service;


import com.amazonaws.services.dynamodb.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodb.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.BatchGetItemResult;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.KeysAndAttributes;
import com.silkstream.platform.enums.*;
import com.silkstream.platform.exception.ApplicationException;
import com.silkstream.platform.exception.WrongEmailException;
import com.silkstream.platform.exception.WrongPasswordException;
import com.silkstream.platform.exception.user.UserConfirmationIdNotExist;
import com.silkstream.platform.exception.user.UserNotEnabledException;
import com.silkstream.platform.models.db.Tivity;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.db.UserConfirmation;
import com.silkstream.platform.validator.UserValidator;
import com.silkstream.platform.web.CommentController;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * This class serves to create new users, login and authorize to return users
 */
@Service("userService")
public class UserService extends FollowService {

	@Inject
	protected ShaPasswordEncoder encoder;
	@Inject
	protected CloudSearchService cloudSearchService;
	@Inject
	protected MailService mailService;
	@Inject
	protected CommentController commentController;
	@Inject
	protected TokenBasedRememberMeServices rememberMeServices;
	@Inject
	protected InvitationService invitationService;


	public User get(String id) {
		return get(id, null);
	}

	public User get(String id, List<String> attributesToGet) {
		if (id != null) {
			return mapper.load(User.class, id, attributesToGet);
		}
		return null;
	}

	public User getWithEmail(String email) {
		if (email != null) {
			PaginatedScanList<User> users = mapper.scanWith(User.class, "em", email);
			if (users != null) {
				if (users.size() != 0) {
					return users.get(0);
				}
			}
		}
		return null;
	}

	public User getWithFbId(String facebookId) {
		if (facebookId != null) {
			PaginatedScanList<User> users = mapper.scanWith(User.class, "fi", facebookId);
			if (users != null) {
				if (users.size() != 0) {
					return users.get(0);
				}
			}
		}
		return null;
	}

	public void disable(String id) {
		if (id != null) {
			User user = get(id);
			if (user != null) {
				user.setEnabled(false);
				user.setAccountNonLocked(false);
				this.update(user);
			}
		}
	}

	public User register(String email, String password) {
		return register(email, password, null, null, null, null, null, null);
	}

	public User register(String email, String password, String firstName, String lastName, GenderType gender, String imgUrl, String socialToken, String facebookId) {
		if (email != null && password != null) {
			User user = new User();
			user.setEmail(email);
			user.setPassword(password);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setPictureUrl(imgUrl);
			user.setGender(gender);
			user.setSocialToken(socialToken);
			user.setEnabled(false);
			if (socialToken != null && facebookId != null) {
				user.setEnabled(true);
			}
			user.setFacebookId(facebookId);
			return this.create(user);
		}
		return null;
	}

	public void deleteConfirmationsByUser(User user) {
		if (user != null) {
			List<UserConfirmation> list = mapper.scanWith(UserConfirmation.class, "uid", user.getId());
			if (list != null) {
				for (UserConfirmation confirmation : list) {
					mapper.delete(confirmation);
				}
			}
		}
	}

	public void createConfirmation(User user) {
		if (user != null) {
			createConfirmation(user, true);
		}
	}

	public void createConfirmation(User user, Boolean signUp) {
		if (user != null) {
			deleteConfirmationsByUser(user); // Delete past confirmations
			UserConfirmation userConfirmation = new UserConfirmation();
			String id = createId();
			userConfirmation.setConfimationId(id);
			userConfirmation.setUserId(user.getId());
			userConfirmation.setCreatedDate(System.currentTimeMillis());
			try {
				if (properties.getEnvironment() != EnvironmentType.TEST) {
					if (signUp) {
						mailService.sendSignUp(user, userConfirmation.getConfimationId());
					} else {
						mailService.sendResetPassword(userConfirmation.getConfimationId(), user);
					}
				}
			} catch (Exception e) {

			}
			mapper.save(userConfirmation);
		}
	}

	// TODO: need to create add function
	protected User create(User user) {
		if (user != null) {
			if (user.getEmail() == null) {
				return null;
			}
			try {
				user.setCreatedDate(System.currentTimeMillis());
				user.setPassword(encoder.encodePassword(user.getPassword(), user.getCreatedDate()));
				user.setRole(UserRole.USER);
				user.setReputation(0L);
				user.setLoginCount(0L);
				user.setId(createId());
				if (!user.isEnabled()) {
					createConfirmation(user);
				}

				UserValidator validator = new UserValidator();
				List<Error> errors = validator.validate(user);

				// Check to see if user already exists
				User currentUser = getWithEmail(user.getEmail());
				if (errors.size() == 0 && currentUser == null) {
					indexUser(user);
					invitationService.userRegistered(user);
					mapper.clobber(user);
					// TODO: send email with proper headers
				} else {
					user = currentUser;
				}

			} catch (Exception e) {
				user = null;
			}
			return user;
		}
		return null;
	}


	public User login(String email, String password) throws ApplicationException {
		if (email != null) {
			User user = getWithEmail(email);
			if (user != null) {
				if (!user.isEnabled()) {
					throw new UserNotEnabledException("You need to confirm your account before you login. An email should have been sent with the confirmation link. For the user : " + email);
				}
				if (!user.isAccountNonLocked()) {
					throw new UserNotEnabledException("You account has been disabled. For the user : " + email);
				}
				if (password != null) {
					if (encoder.isPasswordValid(user.getPassword(), password, user.getCreatedDate())) {
						user = login(user);
						return user;
					} else {
						throw new WrongPasswordException("Password does not match saved password. For the user : " + email + " with created date : " + user.getCreatedDate() + " the entered hashed password was : " + encoder.encodePassword(password, user.getCreatedDate()) + " the saved hashed password is : " + user.getPassword());
					}
				}
			} else {
				throw new WrongEmailException("We could not find the email " + email + " in our records");
			}
		}
		return null;
	}


	public User incrementLoginCount(User user) {
		if (user != null) {
			if (user.getLoginCount() != null) {
				user.setLoginCount(user.getLoginCount() + 1);
			} else {
				user.setLoginCount(1L);
			}
		}
		return user;
	}

	public User login(User user) {
		// Increment login count
		if (user != null) {
			user = incrementLoginCount(user);
			mapper.save(user);
			addUserToContext(user);
			return user;
		}
		return null;
	}

	public void addUserToContext(User user) {
		if (user != null) {
			UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(user, user.getId(), user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(t);
		}
	}

	/*public void confirmation(String confirmationId, String password, String firstName, String lastName, GenderType gender) {
		if (confirmationId != null) {
			UserConfirmation confirmation = mapper.load(UserConfirmation.class, confirmationId);
			if (confirmation != null) {
				User user = get(confirmation.getUserId());
				if (user != null) {
					if (password != null) {
						if (encoder.encodePassword(password, user.getCreatedDate()).equals(user.getPassword())) {
							if (lastName != null) {
								user.setLastName(lastName);
							}
							if (firstName != null) {
								user.setFirstName(firstName);
							}
							if (gender != null) {
								user.setGender(gender);
							}
							user.setEnabled(true);
							mapper.save(user);
							deleteConfirmationsByUser(user);
							mailService.sendWelcome(user);
							login(user); // Add user to authentication
						} else {
							throw new WrongPasswordException("Password does not match our records per the confirmation id.");
						}
					}
				}
			}
		}
	}*/

	public void rememberUser(HttpServletRequest request, HttpServletResponse response) {
		if (request != null && response != null) {
			rememberMeServices.onLoginSuccess(request, response, SecurityContextHolder.getContext().getAuthentication());
		}
	}

	// TODO: redo this function, very inefficient, need to scan based on id and type, not loop.
	public List<User> getUsersThatFollowThisId(String followedId) {
		List<User> users = mapper.scan(User.class, new DynamoDBScanExpression());
		List<User> res = new ArrayList<User>();
		for (User user : users) {
			if (isFollowing(user, ModelType.LOCATION, followedId)) {
				res.add(user);
			}
		}
		return res;
	}

	public User getUserFromUserConfirmationId(String confirmationId) {
		if (confirmationId != null) {
			UserConfirmation userConfirmation = mapper.load(UserConfirmation.class, confirmationId);
			if (userConfirmation == null) {
				throw new UserConfirmationIdNotExist();
			}
			return mapper.load(User.class, userConfirmation.getUserId());
		}
		return null;
	}

	public User updatePassword(String password, User user) {
		if (password != null && user != null) {
			user.setPassword(password);
			mapper.save(user);
		}
		return user;
	}

	public void update(User user) {
		if (user != null) {
			mapper.save(user);
			indexUser(user);
			// If user is the same as the current signed in user, need to update auth
			if(user.getId().equals(getUser().getId())) {
				addUserToContext(user);
			}
		}
	}

	private void indexUser(User user) {
		if (user.getLocations() != null) {

		}
		cloudSearchService.addItem(
				SearchIndexType.USER,
				user.getId(),
				user.getEmail(),
				user.getFirstName() + " " + user.getLastName(),
				StringUtils.join(user.getActivities(), ","),
				null,
				null,
				user.getCreatedDate(),
				user.getCreatedDate()
		);
	}

	public List<User> getUsersFromTivities(List<Tivity> tivities) {
		if (tivities != null) {
			if (tivities.size() != 0) {
				String tableName = mapper.getTieredTableName(User.class);
				Map<String, KeysAndAttributes> toGet = new HashMap<String, KeysAndAttributes>();
				KeysAndAttributes keysAndAttributes = new KeysAndAttributes();
				List<Key> keys = new ArrayList<Key>();
				Set<String> userIds = new HashSet<String>();

				for (Tivity tivity : tivities) {
					if (userIds.add(tivity.getUserId())) {
						keys.add(new Key(new AttributeValue(tivity.getUserId())));
					}
				}

				keysAndAttributes.setKeys(keys);
				toGet.put("User", keysAndAttributes);

				BatchGetItemResult batchGetItemResult = mapper.batchGetItem(toGet);
				if (batchGetItemResult != null) {
					if (batchGetItemResult.getResponses() != null) {
						if (batchGetItemResult.getResponses().get(tableName) != null) {
							return mapper.marshallIntoObjects(User.class, batchGetItemResult.getResponses().get(tableName).getItems());
						}
					}
				}
			}
		}
		return null;
	}


	public List<String> getAllAttributes() {
		List<String> attributesToGet = new ArrayList<String>();
		attributesToGet.add("id");
		attributesToGet.add("em");
		attributesToGet.add("cr");
		attributesToGet.add("pw");
		attributesToGet.add("en");
		attributesToGet.add("al");
		attributesToGet.add("ce");
		attributesToGet.add("ae");
		attributesToGet.add("ro");
		attributesToGet.add("fn");
		attributesToGet.add("ln");
		attributesToGet.add("ip");
		attributesToGet.add("pu");
		attributesToGet.add("st");
		attributesToGet.add("re");
		attributesToGet.add("ac");
		attributesToGet.add("lc");
		attributesToGet.add("ge");
		attributesToGet.add("gl");
		attributesToGet.add("av");
		attributesToGet.add("fi");
		attributesToGet.add("fu");
		attributesToGet.add("fl");
		return attributesToGet;
	}
}
