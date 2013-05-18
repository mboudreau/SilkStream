package com.silkstream.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class PlaceDoesntExistException extends ApplicationException {

	public PlaceDoesntExistException(){
		setType("PlaceDoesntExistException");
		setMessage("Place does not exist.");
	}

	public PlaceDoesntExistException(String message) {
		this();
		setMessage(message);
	}
}
