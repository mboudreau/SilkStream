package com.silkstream.platform.exception.user;

import com.silkstream.platform.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class UserNotEnabledException extends ApplicationException {

	public UserNotEnabledException(){
		setType("UserNotEnabledException");
		setMessage("You need to confirm your account before you login. An email should have been sent with the confirmation link.");
	}

	public UserNotEnabledException(String message) {
		this();
		setMessage(message);
	}
}
