package com.silkstream.platform.service;

import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.amazonaws.services.dynamodb.datamodeling.*;
import com.amazonaws.services.dynamodb.model.*;
import com.silkstream.platform.enums.EnvironmentType;
import com.silkstream.platform.models.BeanstalkProperties;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TivityMapperService extends DynamoDBMapper {

	@Inject
	protected BeanstalkProperties properties;
	private final AmazonDynamoDB client;

	public TivityMapperService(AmazonDynamoDB dynamoDB) {
		super(dynamoDB, DynamoDBMapperConfig.DEFAULT);
		this.client = dynamoDB;
	}

	@Override
	public <T> T load(Class<T> clazz, Object hashKey, Object rangeKey, DynamoDBMapperConfig config) {
		return super.load(clazz, hashKey, rangeKey, getTieredConfig(clazz, config));
	}

	@Override
	public <T> void save(T object, DynamoDBMapperConfig config) {
		super.save(object, getTieredConfig(object.getClass(), config));
	}

	@Override
	public void delete(Object object, DynamoDBMapperConfig config) {
		super.delete(object, getTieredConfig(object.getClass(), config));
	}

	@Override
	public <T> PaginatedScanList<T> scan(Class<T> clazz, DynamoDBScanExpression scanExpression, DynamoDBMapperConfig config) {
		return super.scan(clazz, scanExpression, getTieredConfig(clazz, config));
	}

	@Override
	public <T> PaginatedQueryList<T> query(Class<T> clazz, DynamoDBQueryExpression queryExpression, DynamoDBMapperConfig config) {
		return super.query(clazz, queryExpression, getTieredConfig(clazz, config));
	}

	@Override
	public int count(Class<?> clazz, DynamoDBScanExpression scanExpression, DynamoDBMapperConfig config) {
		return super.count(clazz, scanExpression, getTieredConfig(clazz, config));
	}

	@Override
	public int count(Class<?> clazz, DynamoDBQueryExpression queryExpression, DynamoDBMapperConfig config) {
		return super.count(clazz, queryExpression, getTieredConfig(clazz, config));
	}

	// Extra functionality
	public <T> void clobber(T object) {
		super.save(object, getTieredConfig(object.getClass(), new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER)));
	}

	public BatchGetItemResult batchGetItem(Map<String, KeysAndAttributes> items) {
		Map<String, KeysAndAttributes> copyItems = new HashMap<String, KeysAndAttributes>();
		for (String tableName : items.keySet()) {
			copyItems.put(getPrefixedTableName(tableName), items.get(tableName));
		}
		BatchGetItemRequest request = new BatchGetItemRequest();
		request.setRequestItems(copyItems);
		BatchGetItemResult result = client.batchGetItem(request);
		return result;
	}

	public void batchWrite(List<? extends Object> objectsToWrite, List<? extends Object> objectsToDelete, DynamoDBMapperConfig config) {
		// TODO: mimic batch write to get proper table name
		super.batchWrite(objectsToWrite, objectsToDelete, config);
	}

	public <T> T load(Class<T> clazz, String hashKeyValue, List<String> attributesToGet) {
		GetItemRequest getItemRequest = new GetItemRequest(getTieredTableName(clazz), new Key(new AttributeValue(hashKeyValue)));
		if (attributesToGet != null) {
			getItemRequest.setAttributesToGet(attributesToGet);
		}
		GetItemResult getItemResult = client.getItem(getItemRequest);
		if (getItemResult.getItem() != null) {
			return marshallIntoObject(clazz, getItemResult.getItem());
		}
		return null;
	}

	public ScanResult scanWithScanRequest(ScanRequest scanRequest) {
		return client.scan(scanRequest);
	}

	public <T> PaginatedScanList<T> scanWith(Class<T> clazz, String attributeName, String attributeValue) {
		return scanWith(clazz, attributeName, attributeValue, null);
	}

	public <T> PaginatedScanList<T> scanWith(Class<T> clazz, String attributeName, String attributeValue, ComparisonOperator comparisonOperator) {
		if (comparisonOperator == null) {
			comparisonOperator = ComparisonOperator.EQ;
		}
		DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
		Map<String, Condition> map = new HashMap<String, Condition>();
		Condition condition = new Condition();
		List<AttributeValue> list = new ArrayList<AttributeValue>();
		list.add(new AttributeValue(attributeValue));
		condition.setAttributeValueList(list);
		condition.setComparisonOperator(comparisonOperator);
		map.put(attributeName, condition);
		dynamoDBScanExpression.setScanFilter(map);

		return this.scan(clazz, dynamoDBScanExpression);
	}


	protected <T> DynamoDBMapperConfig getTieredConfig(Class<T> clazz, DynamoDBMapperConfig config) {
		// Do not use tiered if table name override already exists
		if (config.getTableNameOverride() == null) {
			DynamoDBMapperConfig.TableNameOverride override = new DynamoDBMapperConfig.TableNameOverride(getTieredTableName(clazz));
			config = new DynamoDBMapperConfig(config.getSaveBehavior(), config.getConsistentReads(), override);
		}
		return config;
	}


	public String getAttributesFromItem(Class clazz, String attributeName, String id) {
		// Do not use tiered if table name override already exists
		GetItemRequest getItemRequest = new GetItemRequest();
		getItemRequest.withTableName(getTieredTableName(clazz));
		getItemRequest.withAttributesToGet(attributeName);
		getItemRequest.withKey(new Key()
				.withHashKeyElement(new AttributeValue(id)));
		return client.getItem(getItemRequest).getItem().get(attributeName).getS();
	}

	public <T> String getTieredTableName(Class<T> clazz) {
		return getPrefixedTableName(getTableName(clazz));
	}

	public String getPrefixedTableName(String tableName) {
		if (properties.getEnvironment() != null) {
			EnvironmentType prefix = properties.getEnvironment();

			tableName = prefix + "-" + tableName;
		}
		return tableName;
	}

	protected <T> String getTableName(Class<T> clazz) {
		DynamoDBTable table = clazz.getAnnotation(DynamoDBTable.class);
		if (table == null) {
			throw new DynamoDBMappingException("Class " + clazz + " must be annotated with " + DynamoDBTable.class);
		}
		return table.tableName();
	}

	public CreateTableResult createTable(CreateTableRequest createTableRequest) {
		return client.createTable(createTableRequest);
	}

}
