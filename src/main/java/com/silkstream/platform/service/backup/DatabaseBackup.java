package com.silkstream.platform.service.backup;


import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.StringInputStream;
import com.silkstream.platform.enums.EnvironmentType;
import com.silkstream.platform.models.BeanstalkProperties;
import com.silkstream.platform.models.db.*;
import com.silkstream.platform.service.TivityMapperService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseBackup {
	@Inject
	BeanstalkProperties properties;
	@Inject
	TivityMapperService mapper;
	@Inject
	private
	AmazonS3Client amazonS3Client;

	private final String bucketName = "silkstream.dynamodb.backup";

	// Fire at 2am every day
	@Scheduled(cron = "0 0 2 * * ?")
	public void backup() {
		if (properties.getEnvironment().equals(EnvironmentType.PROD)) {
			backupTable(properties.getEnvironment().toString() + "-answer.json", Tivity.class);
			backupTable(properties.getEnvironment().toString() + "-comment.json", Comment.class);
			backupTable(properties.getEnvironment().toString() + "-externalconnection.json", ExternalConnection.class);
			backupTable(properties.getEnvironment().toString() + "-location.json", Place.class);
			backupTable(properties.getEnvironment().toString() + "-user.json", User.class);
			backupTable(properties.getEnvironment().toString() + "-userconfirmation.json", UserConfirmation.class);
			backupTable(properties.getEnvironment().toString() + "-userlog.json", UserLog.class);
		}
	}

	public  <T> PutObjectResult backupTable(String fileName, Class<T> clazz) {
		// get list of all records in table
		List<Map<String,AttributeValue>> items = new ArrayList<Map<String, AttributeValue>>();

		// scan over and over until database is fully read
		ScanResult result = null;
		do {
			ScanRequest request = new ScanRequest().withTableName(mapper.getTieredTableName(clazz));
			if(result != null) {
				request.setExclusiveStartKey(result.getLastEvaluatedKey());
			}
			result = mapper.scanWithScanRequest(request);
			items.addAll(result.getItems());
		}while(result.getLastEvaluatedKey() != null);

		ObjectMetadata data = new ObjectMetadata();
		data.addUserMetadata("Date", new Date().toString());
		data.addUserMetadata("Count", result.getCount().toString());

		try {
			ObjectMapper parser = new ObjectMapper();
			InputStream stream = new StringInputStream(parser.writeValueAsString(items));
			 return getAmazonS3Client().putObject(getBucketName(), fileName, stream, data);
		} catch (IOException e) {

		}
		return null;
	}

	public AmazonS3Client getAmazonS3Client() {
		return amazonS3Client;
	}

	public void setAmazonS3Client(AmazonS3Client amazonS3Client) {
		this.amazonS3Client = amazonS3Client;
	}

	public String getBucketName() {
		return bucketName;
	}
}
