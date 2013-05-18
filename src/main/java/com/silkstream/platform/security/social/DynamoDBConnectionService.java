package com.silkstream.platform.security.social;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodb.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.silkstream.platform.models.db.ExternalConnection;
import com.silkstream.platform.service.TivityMapperService;
import javax.inject.Inject;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.Map.Entry;


public class DynamoDBConnectionService {
	@Inject
	protected TivityMapperService mapper;
	@Inject
	private ConnectionConverter converter;

	public DynamoDBConnectionService() {
	}

	public int getMaxRank(String userId, String providerId) {
		ExternalConnection conn = getConnFromParams(userId, providerId);
		if (conn == null)
			return 1;

		return conn.getRank() + 1;
	}

	public void create(String userId, Connection<?> userConn, int rank) {
		ExternalConnection dynamoCnn = converter.convert(userConn);
		dynamoCnn.setUserId(userId);
		dynamoCnn.setRank(rank);
		mapper.clobber(dynamoCnn);
	}

	public void update(String userId, Connection<?> userConn) {
		ExternalConnection dynamoCnn = converter.convert(userConn);
		dynamoCnn.setUserId(userId);
		mapper.clobber(dynamoCnn);
	}

	public void remove(String userId, ConnectionKey connectionKey) {
		//delete where userId = ? and providerId = ? and providerUserId = ?
		mapper.delete(getConnFromParams(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId()));
	}

	public void remove(String userId, String providerId) {
		// delete where userId = ? and providerId = ?
		mapper.delete(getConnFromParams(userId, providerId));
	}

	public Connection<?> getPrimaryConnection(String userId, String providerId) {
		ExternalConnection dc = getConnFromParams(userId, providerId);
		return converter.convert(dc);
	}

	public Connection<?> getConnection(String userId, String providerId, String providerUserId) {
		// where userId = ? and providerId = ? and providerUserId = ?
		return converter.convert(getConnFromParams(userId, providerId, providerUserId));
	}

	public List<Connection<?>> getConnections(String userId) {
		// select where userId = ? order by providerId, rank
		return converter.convert(getConnsFromUserId(userId));
	}

	public List<Connection<?>> getConnections(String userId, String providerId) {
		// where userId = ? and providerId = ? order by rank
		return getConnections(userId);
	}

	public List<Connection<?>> getConnections(String userId, MultiValueMap<String, String> providerUsers) {
		// userId? and providerId = ? and providerUserId in (?, ?, ...) order by providerId, rank

		if (providerUsers == null || providerUsers.isEmpty()) {
			throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
		}

		List<Connection<?>> conns = new ArrayList<Connection<?>>();
		List<ExternalConnection> dConns = getConnsFromUserId(userId);
		for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext(); ) {
			Entry<String, List<String>> entry = it.next();
			String providerId = entry.getKey();
			for (ExternalConnection dc : dConns) {
				if (dc.getProviderId().equals(providerId)) {
					if (entry.getValue().contains(dc.getProviderUserId())) {
						conns.add(converter.convert(dc));
					}
				}
			}
		}
		return conns;
	}

	public Set<String> getUserIds(String providerId, Set<String> providerUserIds) {
		//select userId from " + tablePrefix + "UserConnection where providerId = :providerId and providerUserId in (:providerUserIds)
		/*DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
				Map<String, Condition> filter = new HashMap<String, Condition>();
				Condition rangeKeyCondition = new Condition();
				List<AttributeValue> list = new ArrayList<AttributeValue>();
				AttributeValue rangeKey = new AttributeValue();
				rangeKey.setS(providerId);
				list.create(rangeKey);
				rangeKeyCondition.setAttributeValueList(list);
				rangeKeyCondition.setComparisonOperator(ComparisonOperator.EQ);
				filter.put("pi", rangeKeyCondition);
				scanExpression.setScanFilter(filter);*/
		String pid = "";
		for (String p : providerUserIds) {
			pid = p;
		}
		// List<ExternalConnection> results = mapper.scan(ExternalConnection.class, scanExpression);
		ExternalConnection connection = mapper.load(ExternalConnection.class, pid, providerId);
		Set<String> userIds = new HashSet<String>();
		if (connection != null) {
			userIds.add(connection.getUserId());
		}

		return userIds;
	}

	public List<String> getUserIds(String providerId, String providerUserId) {
		//select userId where providerId = ? and providerUserId = ?",
		Set<String> set = new HashSet<String>();
		set.add(providerUserId);
		return new ArrayList<String>(getUserIds(providerId, set));
	}


	public List<ExternalConnection> getConnsFromUserId(String userId) {
		AttributeValue hashKey = new AttributeValue();
		hashKey.setS(userId);
		AttributeValue rangeKey = new AttributeValue();
		DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression(hashKey);
		PaginatedQueryList<ExternalConnection> externalConnections = mapper.query(ExternalConnection.class, queryExpression);
		return externalConnections;
	}

	public ExternalConnection getConnFromParams(List<ExternalConnection> list, String providerId, String providerUserId) {
		ExternalConnection conn = new ExternalConnection();
		Set<String> params = new HashSet<String>();
		params.add(providerId);
		params.add(providerUserId);
		params.remove(null);
		for (ExternalConnection cnn : list) {
			if (params.contains(cnn.getProviderId()) && params.contains(cnn.getProviderUserId())) {
				return cnn;
			} else if (params.contains(cnn.getProviderId())) {
				return cnn;
			} else if (params.contains(cnn.getProviderUserId())) {
				return cnn;
			}
		}
		return null;
	}

	public ExternalConnection getConnFromParams(String userId, String providerId, String providerUserId) {
		return getConnFromParams(getConnsFromUserId(userId), providerId, providerUserId);
	}

	public ExternalConnection getConnFromParams(String userId, String providerId) {
		return getConnFromParams(userId, providerId, null);
	}
}