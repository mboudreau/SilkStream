package com.silkstream.platform.exception;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class FacebookPermissionException extends RuntimeException {
	protected String type = "FacebookPermissionException";
	protected String message = "You don't have the facebook permissions for this action.";

	public FacebookPermissionException() {
	}

	public FacebookPermissionException(String message) {
		setMessage(message);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
