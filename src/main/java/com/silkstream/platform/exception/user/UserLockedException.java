package com.silkstream.platform.exception.user;

import com.silkstream.platform.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class UserLockedException extends ApplicationException {

	public UserLockedException(){
		setType("UserLockedException");
		setMessage("You account has been disabled.");
	}

	public UserLockedException(String message) {
		this();
		setMessage(message);
	}
}
