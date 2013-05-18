package com.silkstream.platform.exception.user;

import com.silkstream.platform.exception.ApplicationException;

public class PasswordDoesnotMatchException extends ApplicationException {
    public PasswordDoesnotMatchException(){
        setType("PasswordDoesnotMatchException");
        setMessage("The password and confirmation password does not match");
    }

    public PasswordDoesnotMatchException(String message) {
        this();
        setMessage(message);
    }
}
