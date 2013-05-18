package com.silkstream.platform.exception.user;

import com.silkstream.platform.exception.ApplicationException;

public class UserAlreadyExistsException extends ApplicationException {

	public UserAlreadyExistsException(){
		setType("UserAlreadyExistsException");
		setMessage("User already exists.");
	}

	public UserAlreadyExistsException(String message) {
		this();
		setMessage(message);
	}
}
