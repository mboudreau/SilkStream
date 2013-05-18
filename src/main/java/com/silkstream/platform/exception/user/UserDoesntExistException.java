package com.silkstream.platform.exception.user;

import com.silkstream.platform.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class UserDoesntExistException extends ApplicationException {

	public UserDoesntExistException(){
		setType("UserDoesntExistException");
		setMessage("User does not exist.");
	}

	public UserDoesntExistException(String message) {
		this();
		setMessage(message);
	}
}
