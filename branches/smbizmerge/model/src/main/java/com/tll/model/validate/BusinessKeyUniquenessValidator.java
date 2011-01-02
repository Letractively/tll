/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.tll.model.EntityMetadata;
import com.tll.model.bk.BusinessKeyFactory;
import com.tll.model.bk.BusinessKeyPropertyException;
import com.tll.model.bk.NonUniqueBusinessKeyException;

/**
 * Validates business key uniqueness based in simple enity metadata.
 * @see BusinessKeyUniqueness
 * @author jpk
 */
public class BusinessKeyUniquenessValidator implements ConstraintValidator<BusinessKeyUniqueness, Collection<?>> {

	private final BusinessKeyFactory bkf = new BusinessKeyFactory(new EntityMetadata());

	@Override
	public void initialize(BusinessKeyUniqueness parameters) {
		// no-op
	}

	@Override
	public boolean isValid(Collection<?> clc, ConstraintValidatorContext constraintContext) {
		try {
			bkf.isBusinessKeyUnique(clc);
			return true;
		}
		catch(final BusinessKeyPropertyException e) {
			return false;
		}
		catch(final NonUniqueBusinessKeyException e) {
			return false;
		}
	}
}
