package com.silkstream.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class GoogleServiceException extends ApplicationException {

	public GoogleServiceException(){
		setType("GoogleServiceException");
		setMessage("The google service encountered an issue.");
	}

	public GoogleServiceException(String message) {
		this();
		setMessage(message);
	}
}
