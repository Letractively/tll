/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.tll.model.IEntity;
import com.tll.model.bk.BusinessKeyPropertyException;
import com.tll.model.bk.BusinessKeyUtil;
import com.tll.model.bk.NonUniqueBusinessKeyException;

/**
 * BusinessKeyUniquenessValidator - Validates business key uniqueness.
 * @see BusinessKeyUniqueness
 * @author jpk
 */
public class BusinessKeyUniquenessValidator implements ConstraintValidator<BusinessKeyUniqueness, Collection<? extends IEntity>> {

	public void initialize(BusinessKeyUniqueness parameters) {
	}

	public boolean isValid(Collection<? extends IEntity> clc, ConstraintValidatorContext constraintContext) {
		try {
			BusinessKeyUtil.isBusinessKeyUnique(clc);
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
