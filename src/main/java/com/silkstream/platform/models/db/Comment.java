package com.silkstream.platform.models.db;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;
import com.silkstream.platform.enums.UserLogType;
import com.silkstream.platform.models.marshallers.enums.UserLogTypeMarshaller;

@DynamoDBTable(tableName = "Comment")
public class Comment {
	private String id;
	private String userid;
	private String message;
	private String targetId;
    private Long updatedDate;
	private Long creadtedDate;
	private Long rating;
	private UserLogType target;
    private boolean flag;

    public  Comment() {

    }


	@DynamoDBHashKey(attributeName = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "ui")
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@DynamoDBAttribute(attributeName = "ms")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@DynamoDBAttribute(attributeName = "ti")
	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	@DynamoDBAttribute(attributeName = "cd")
	public Long getCreadtedDate() {
		return creadtedDate;
	}

	public void setCreadtedDate(Long creadtedDate) {
		this.creadtedDate = creadtedDate;
	}

	@DynamoDBAttribute(attributeName = "ra")
	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}
    @DynamoDBMarshalling(marshallerClass = UserLogTypeMarshaller.class)
	@DynamoDBAttribute(attributeName = "ta")
	public UserLogType getTarget() {
		return target;
	}

	public void setTarget(UserLogType target) {
		this.target = target;
	}

    @DynamoDBAttribute(attributeName = "ud")
    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    @DynamoDBAttribute(attributeName = "fl")
    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
