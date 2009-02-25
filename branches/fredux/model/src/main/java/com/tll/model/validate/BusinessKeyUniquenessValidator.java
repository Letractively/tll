/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

import java.util.Collection;

import org.hibernate.validator.Validator;

import com.tll.model.IEntity;
import com.tll.model.key.BusinessKeyPropertyException;
import com.tll.model.key.BusinessKeyUtil;
import com.tll.model.key.NonUniqueBusinessKeyException;

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
		try {
			BusinessKeyUtil.isBusinessKeyUnique((Collection<? extends IEntity>) value);
			return true;
		}
		catch(BusinessKeyPropertyException e) {
			return false;
		}
		catch(NonUniqueBusinessKeyException e) {
			return false;
		}
	}
}
