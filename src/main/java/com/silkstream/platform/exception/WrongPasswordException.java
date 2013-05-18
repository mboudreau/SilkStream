package com.silkstream.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class WrongPasswordException extends ApplicationException {

	public WrongPasswordException(){
		setType("WrongPasswordException");
		setMessage("The password does not match our records");
	}

	public WrongPasswordException(String message) {
		this();
		setMessage(message);
	}
}
