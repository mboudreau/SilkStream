package com.silkstream.platform.validator;

import com.silkstream.platform.exception.MissingParametersException;
import com.silkstream.platform.exception.ModelNullException;
import com.silkstream.platform.models.db.Place;
import com.silkstream.platform.models.dto.PlaceItem;

import java.util.List;

public class PlaceItemValidator extends Validator {

	public Boolean supports(Class clazz) {
		return Place.class.isAssignableFrom(clazz);
	}

	public List<Error> validate(Object target) {
		PlaceItem instance = (PlaceItem) target;
		List<Error> errors = ValidatorUtils.rejectIfNull(instance);
		if (errors.size() == 0) {
			errors.addAll(ValidatorUtils.rejectIfNull(instance.getGoogleId()));
			if (errors.size() != 0) {
				throw new MissingParametersException("The place provided has no google id reference.");
			}
		} else {
			throw new ModelNullException("The place provided is null.");
		}
		return removeNulls(errors);
	}
}
