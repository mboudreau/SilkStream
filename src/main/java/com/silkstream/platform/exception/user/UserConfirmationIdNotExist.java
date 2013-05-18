package com.silkstream.platform.exception.user;

import com.silkstream.platform.exception.ApplicationException;

public class UserConfirmationIdNotExist extends ApplicationException {
    public UserConfirmationIdNotExist(){
        setType("UserConfirmationIdNotExist");
        setMessage("User Confirmation ID does not exist");
    }

    public UserConfirmationIdNotExist(String message) {
        this();
        setMessage(message);
    }
}
