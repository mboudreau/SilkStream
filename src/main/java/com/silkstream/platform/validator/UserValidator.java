package com.silkstream.platform.validator;

import com.silkstream.platform.exception.MissingParametersException;
import com.silkstream.platform.exception.ModelNullException;
import com.silkstream.platform.models.db.User;

import java.util.List;

public class UserValidator extends Validator {

	public Boolean supports(Class clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	public List<Error> validate(Object target) {
		User instance = (User) target;
		List<Error> errors = ValidatorUtils.rejectIfNull(instance);
		if (errors.size() == 0) {
			errors.addAll(ValidatorUtils.rejectIfNullOrEmptyOrWhitespace(instance.getPassword()));
			if(errors.size() !=0){
				throw new MissingParametersException("The user has no password.");
			}
			errors.addAll(ValidatorUtils.rejectIfNullOrEmptyOrWhitespace(instance.getEmail()));
			if(errors.size() !=0){
				throw new MissingParametersException("The user has no email.");
			}
		} else {
			throw new ModelNullException("The user provided is null.");
		}
		return removeNulls(errors);
	}
}
