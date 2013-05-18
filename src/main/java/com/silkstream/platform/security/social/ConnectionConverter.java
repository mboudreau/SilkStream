package com.silkstream.platform.security.social;

import com.silkstream.platform.models.db.ExternalConnection;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;

import java.util.ArrayList;
import java.util.List;

public class ConnectionConverter {
	private final ConnectionFactoryLocator connectionFactoryLocator;
	private final TextEncryptor textEncryptor;

	public ConnectionConverter(ConnectionFactoryLocator connectionFactoryLocator,
							   TextEncryptor textEncryptor) {

		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	public Connection<?> convert(ExternalConnection cnn) {
		if (cnn == null) return null;

		ConnectionData connectionData = fillConnectionData(cnn);
		ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
		return connectionFactory.createConnection(connectionData);
	}

	public List<Connection<?>> convert(List<ExternalConnection> cnns) {
		List<Connection<?>> conns = new ArrayList<Connection<?>>();
		for (ExternalConnection cnn : cnns) {
			conns.add(convert(cnn));
		}
		return conns;
	}

	private ConnectionData fillConnectionData(ExternalConnection uc) {
		return new ConnectionData(uc.getProviderId(),
				uc.getProviderUserId(),
				uc.getDisplayName(),
				uc.getProfileUrl(),
				uc.getImageUrl(),
				decrypt(uc.getAccessToken()),
				decrypt(uc.getSecret()),
				decrypt(uc.getRefreshToken()),
				expireTime(uc.getExpireTime()));
	}

	public ExternalConnection convert(Connection<?> cnn) {
		ConnectionData data = cnn.createData();

		ExternalConnection userConn = new ExternalConnection();
		userConn.setProviderId(data.getProviderId());
		userConn.setProviderUserId(data.getProviderUserId());
		userConn.setDisplayName(data.getDisplayName());
		userConn.setProfileUrl(data.getProfileUrl());
		userConn.setImageUrl(data.getImageUrl());
		userConn.setAccessToken(encrypt(data.getAccessToken()));
		userConn.setSecret(encrypt(data.getSecret()));
		userConn.setRefreshToken(encrypt(data.getRefreshToken()));
		userConn.setExpireTime(data.getExpireTime());
		return userConn;
	}
	// helper methods

	private String decrypt(String encryptedText) {
		return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
	}

	private String encrypt(String text) {
		return text != null ? textEncryptor.encrypt(text) : text;
	}

	private Long expireTime(long expireTime) {
		return expireTime == 0 ? null : expireTime;
	}
}
