package com.silkstream.platform.security.social;

import org.springframework.social.connect.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DynamoDBUsersConnectionRepository implements UsersConnectionRepository {

	private DynamoDBConnectionService dynamoService;
	private ConnectionFactoryLocator connectionFactoryLocator;
	private TivityConnectionSignUp connectionSignUp;

	public DynamoDBUsersConnectionRepository(DynamoDBConnectionService dynamoService, ConnectionFactoryLocator connectionFactoryLocator) {
		this.dynamoService = dynamoService;
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

	public void setConnectionSignUp(TivityConnectionSignUp connectionSignUp) {
		this.connectionSignUp = connectionSignUp;
	}

	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		ConnectionKey key = connection.getKey();
		List<String> localUserIds = dynamoService.getUserIds(key.getProviderId(), key.getProviderUserId());
		if (connectionSignUp != null) {
			String newUserId = null;
			if (key.getProviderId().equals("facebook")) {
				newUserId = connectionSignUp.executeFacebook(connection);
			}/*else if (key.getProviderId().equals("twitter")){
                newUserId = connectionSignUp.executeTwitter(connection);
            }*/
			if (newUserId != null) {
				createConnectionRepository(newUserId).addConnection(connection);
				return Arrays.asList(newUserId);
			}
		}
		return localUserIds;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		return dynamoService.getUserIds(providerId, providerUserIds);
	}

	@Override
	public ConnectionRepository createConnectionRepository(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}
		return new DynamoDBConnectionRepository(userId, dynamoService, connectionFactoryLocator);
	}
}