package com.silkstream.platform.models.db;

import com.amazonaws.services.dynamodb.datamodeling.*;
import com.silkstream.platform.enums.GenderType;
import com.silkstream.platform.enums.UserRole;
import com.silkstream.platform.enums.WeekDay;
import com.silkstream.platform.models.marshallers.*;
import com.silkstream.platform.models.marshallers.enums.GenderMarshaller;
import com.silkstream.platform.models.marshallers.enums.UserRoleMarshaller;
import com.silkstream.platform.models.marshallers.list.ListFollowedMarshaller;
import com.silkstream.platform.models.marshallers.list.ListGeoLocationMarshaller;
import com.silkstream.platform.models.marshallers.list.ListStringMarshaller;
import com.silkstream.platform.models.utils.AvailabilityDay;
import com.silkstream.platform.models.utils.Followed;
import com.silkstream.platform.models.utils.GeoLocation;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@DynamoDBTable(tableName = "User")
public class User extends DynamoDBModel implements UserDetails {
	private String id;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private GenderType gender;
	private String pictureUrl;
	private List<String> activities;
	private Long reputation;
	private Long loginCount;
	private Long createdDate;
	private String lastIp;
	private boolean enabled;
	private UserRole role;
	private String socialToken;
	private ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	private boolean accountExpired;
	private boolean locked;
	private boolean credentialsExpired;
	private List<Followed> followeds;
	private HashMap<WeekDay, AvailabilityDay> availability;
	private List<GeoLocation> locations;
	private String facebookId;
	private String bio;

	public User() {

	}

	public User(String email, String password) {
		setEmail(email);
		setPassword(password);
	}

	// TODO: Change role to enum
	public User(String email, String password, UserRole role) {
		this(email, password);
		setRole(role);
	}

	@DynamoDBHashKey(attributeName = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "em")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	@DynamoDBIgnore
	public String getUsername() {
		return id;
	}

	@DynamoDBAttribute(attributeName = "cr")
	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	@DynamoDBAttribute(attributeName = "pw")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@DynamoDBAttribute(attributeName = "en")
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@DynamoDBAttribute(attributeName = "al")
	public boolean isAccountNonLocked() {
		return !locked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.locked = !accountNonLocked;
	}

	@DynamoDBAttribute(attributeName = "ce")
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		credentialsExpired = !credentialsNonExpired;
	}

	@DynamoDBAttribute(attributeName = "ae")
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		accountExpired = !accountNonExpired;
	}

	@DynamoDBMarshalling(marshallerClass = UserRoleMarshaller.class)
	@DynamoDBAttribute(attributeName = "ro")
	public UserRole getRole() {
		return this.role;
	}

	public void setRole(UserRole role) {
		this.role = role;
		authorities.clear();
		authorities.add(new SimpleGrantedAuthority(this.role.toString()));
	}

	public boolean hasRole(UserRole role) {
		List<UserRole> roles = Arrays.asList(UserRole.values());
		return roles.indexOf(getRole()) >= roles.indexOf(role);
	}

	// TODO: make this work for realz
	@DynamoDBIgnore
	@JsonIgnore
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@DynamoDBAttribute(attributeName = "fn")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@DynamoDBAttribute(attributeName = "ln")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@DynamoDBAttribute(attributeName = "ip")
	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	@DynamoDBAttribute(attributeName = "pu")
	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String url) {
		this.pictureUrl = url;
	}

	@DynamoDBAttribute(attributeName = "st")
	public String getSocialToken() {
		return socialToken;
	}

	public void setSocialToken(String socialToken) {
		this.socialToken = socialToken;
	}

	@DynamoDBAttribute(attributeName = "re")
	public Long getReputation() {
		return reputation;
	}

	public void setReputation(Long reputation) {
		this.reputation = reputation;
	}

	@DynamoDBMarshalling(marshallerClass = ListStringMarshaller.class)
	@DynamoDBAttribute(attributeName = "ac")
	public List<String> getActivities() {
		return activities;
	}

	public void setActivities(List<String> activities) {
		this.activities = activities;
	}

	@DynamoDBAttribute(attributeName = "lc")
	public Long getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Long loginCount) {
		this.loginCount = loginCount;
	}

	@DynamoDBMarshalling(marshallerClass = GenderMarshaller.class)
	@DynamoDBAttribute(attributeName = "ge")
	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}


	@DynamoDBMarshalling(marshallerClass = ListGeoLocationMarshaller.class)
	@DynamoDBAttribute(attributeName = "gl")
	public List<GeoLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<GeoLocation> locations) {
		this.locations = locations;
	}


	@DynamoDBMarshalling(marshallerClass = AvailabilityMarshaller.class)
	@DynamoDBAttribute(attributeName = "av")
	public HashMap<WeekDay, AvailabilityDay> getAvailability() {
		return availability;
	}

	public void setAvailability(HashMap<WeekDay, AvailabilityDay> availability) {
		this.availability = availability;
	}

	@DynamoDBAttribute(attributeName = "fi")
	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	@DynamoDBAttribute(attributeName = "bi")
	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	@DynamoDBMarshalling(marshallerClass = ListFollowedMarshaller.class)
	@DynamoDBAttribute(attributeName = "fl")
	public List<Followed> getFolloweds() {
		return followeds;
	}

	public void setFolloweds(List<Followed> followeds) {
		this.followeds = followeds;
	}

	@DynamoDBIgnore
	@JsonIgnore
	public String getFullName() {
		return getFirstName()+" "+getLastName();
	}
}
