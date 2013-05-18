package com.silkstream.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class ModelNullException extends ApplicationException {

	public ModelNullException(){
		setType("ModelNullException");
		setMessage("The model is null.");
	}

	public ModelNullException(String message) {
		this();
		setMessage(message);
	}
}
