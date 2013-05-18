package com.silkstream.platform.validator;

import com.silkstream.platform.exception.MissingParametersException;
import com.silkstream.platform.exception.ModelNullException;
import com.silkstream.platform.models.db.Place;
import com.silkstream.platform.models.dto.TivityItem;

import java.util.List;

public class TivityItemValidator extends Validator {

	public Boolean supports(Class clazz) {
		return Place.class.isAssignableFrom(clazz);
	}

	public List<Error> validate(Object target) {
		TivityItem instance = (TivityItem) target;
		List<Error> errors = ValidatorUtils.rejectIfNull(instance);
		if (errors.size() == 0) {
			errors.addAll(ValidatorUtils.rejectIfNull(instance.getLocation()));
			if(errors.size()!=0){
				throw new MissingParametersException("The Tivity has no Place");
			}
		}else {
			throw new ModelNullException("The Tivity provided is null");
		}
		return removeNulls(errors);
	}
}
