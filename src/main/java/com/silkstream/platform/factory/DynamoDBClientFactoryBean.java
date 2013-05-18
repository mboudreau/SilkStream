package com.silkstream.platform.factory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.michelboudreau.alternator.AlternatorDB;
import com.michelboudreau.alternator.AlternatorDBClient;
import com.silkstream.platform.enums.EnvironmentType;
import com.silkstream.platform.models.BeanstalkProperties;
import org.springframework.beans.factory.FactoryBean;
import javax.inject.Inject;

import java.io.File;

public class DynamoDBClientFactoryBean implements FactoryBean<AmazonDynamoDB> {
	@Inject
	private BeanstalkProperties properties;
	private AWSCredentials credentials;
	private final File persitence = new File("db/alternatordb.json");
	private AlternatorDB db = new AlternatorDB(9090, persitence,false);

	public DynamoDBClientFactoryBean(AWSCredentials awsCredentials) {
		this.credentials = awsCredentials;
	}

	public AmazonDynamoDB getObject() {
		if (properties.getEnvironment().equals(EnvironmentType.TEST) || properties.getEnvironment().equals(EnvironmentType.LOCAL)) {
			try{
			db.start();
			}catch (Exception e){
			}
			return new AlternatorDBClient();
		}
		return new AmazonDynamoDBClient(credentials);
	}

	public Class<AmazonDynamoDB> getObjectType() {
		return AmazonDynamoDB.class;
	}

	public boolean isSingleton() {
		return false;
	}

	public AmazonDynamoDBClient getAmazonDynamoDBClient(){
		return new AmazonDynamoDBClient(credentials);
	}

	public AlternatorDB getAlternatorDB(){
		return this.db;
	}
}
