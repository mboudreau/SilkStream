package com.silkstream.platform.validator;

import com.silkstream.platform.exception.MissingParametersException;
import com.silkstream.platform.exception.ModelNullException;
import com.silkstream.platform.models.db.Notification;

import java.util.List;

public class NotificationValidator extends Validator {

	public Boolean supports(Class clazz) {
		return Notification.class.isAssignableFrom(clazz);
	}

	public List<Error> validate(Object target) {
		Notification instance = (Notification) target;
		List<Error> errors = ValidatorUtils.rejectIfNull(instance);
		if (errors.size() == 0) {
			errors.addAll(ValidatorUtils.rejectIfNullOrEmptyOrWhitespace(instance.getFrom()));
			if(errors.size() !=0){
				throw new MissingParametersException("The notification doesn't have a from.");
			}
			errors.addAll(ValidatorUtils.rejectIfNullOrEmptyOrWhitespace(instance.getTo()));
			if(errors.size() !=0){
				throw new MissingParametersException("The notification doesn't have a to.");
			}
			errors.addAll(ValidatorUtils.rejectIfNullOrEmptyOrWhitespace(instance.getId()));
			if(errors.size() !=0){
				throw new MissingParametersException("The notification doesn't have an id.");
			}
			errors.addAll(ValidatorUtils.rejectIfNull(instance.getCreatedDate()));
			if(errors.size() !=0){
				throw new MissingParametersException("The notification doesn't have a created date.");
			}
		} else {
			throw new ModelNullException("The notification provided is null.");
		}
		return removeNulls(errors);
	}
}
