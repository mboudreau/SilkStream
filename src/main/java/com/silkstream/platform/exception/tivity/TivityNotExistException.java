package com.silkstream.platform.exception.tivity;

import com.silkstream.platform.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class TivityNotExistException extends ApplicationException {

	public TivityNotExistException(){
		setType("TivityNotExistException");
		setMessage("Tivity does not exist.");
	}

	public TivityNotExistException(String message) {
		this();
		setMessage(message);
	}
}
