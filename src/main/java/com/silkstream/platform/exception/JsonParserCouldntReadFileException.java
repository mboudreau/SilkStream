package com.silkstream.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class JsonParserCouldntReadFileException extends ApplicationException {

	public JsonParserCouldntReadFileException(){
		setType("JsonParserCouldntReadFileException");
		setMessage("The json wasn't parsed properly.");
	}

	public JsonParserCouldntReadFileException(String message) {
		this();
		setMessage(message);
	}
}
