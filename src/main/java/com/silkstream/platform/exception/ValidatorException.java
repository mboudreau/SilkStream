package com.silkstream.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class ValidatorException extends ApplicationException {

	public ValidatorException(){
		setType("ValidatorException");
		setMessage("The model couldn't be validated.");
	}

	public ValidatorException(String message) {
		this();
		setMessage(message);
	}
}
