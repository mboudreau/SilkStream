package com.silkstream.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class WrongEmailException extends ApplicationException {

	public WrongEmailException(){
		setType("WrongEmailException");
		setMessage("The email does not exists in our records");
	}

	public WrongEmailException(String message) {
		this();
		setMessage(message);
	}
}
