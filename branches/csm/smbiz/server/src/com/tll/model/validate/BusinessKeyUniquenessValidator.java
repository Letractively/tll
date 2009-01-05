/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

import java.util.Collection;

import org.hibernate.validator.Validator;

import com.tll.model.EntityUtil;
import com.tll.model.IEntity;

/**
 * BusinessKeyUniquenessValidator - Validates business key uniqueness.
 * @see BusinessKeyUniqueness
 * @author jpk
 */
public class BusinessKeyUniquenessValidator implements Validator<BusinessKeyUniqueness> {

	public void initialize(BusinessKeyUniqueness parameters) {
	}

	@SuppressWarnings("unchecked")
	public boolean isValid(Object value) {
		return value == null ? true : EntityUtil.isBusinessKeyUnique((Collection<? extends IEntity>) value);
	}
}
