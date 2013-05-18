package com.silkstream.platform.validator;

import com.silkstream.platform.models.dto.Mail;

import java.util.List;

public class MailValidator extends Validator {

	public Boolean supports(Class clazz) {
		return Mail.class.isAssignableFrom(clazz);
	}

	public List<Error> validate(Object target) {
		Mail instance = (Mail) target;
		List<Error> errors = ValidatorUtils.rejectIfNull(instance);
		if (errors.size() == 0) {
			errors.addAll(ValidatorUtils.rejectIfNullOrEmptyOrWhitespace(instance.getSubject()));
			errors.addAll(ValidatorUtils.rejectIfNullOrEmptyOrWhitespace(instance.getMessage()));
		}
		return removeNulls(errors);
	}
}
