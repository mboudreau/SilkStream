package com.silkstream.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class MissingParametersException extends ApplicationException {

	public MissingParametersException(){
		setType("MissingParametersException");
		setMessage("You haven't inputed all the required parameters.");
	}

	public MissingParametersException(String message) {
		this();
		setMessage(message);
	}
}
