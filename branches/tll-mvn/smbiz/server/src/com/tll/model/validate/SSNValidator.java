/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2007
 */
package com.tll.model.validate;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.perl.Perl5Util;
import org.hibernate.mapping.Property;
import org.hibernate.validator.PropertyConstraint;
import org.hibernate.validator.Validator;

import com.tll.util.ValidationUtil;

/**
 * SSNValidator - Validates artifacts annotated with {@link SSN}.
 * @author jpk
 */
public class SSNValidator implements Validator<SSN>, PropertyConstraint {

	public void initialize(SSN parameters) {
		// no-op
	}

	public boolean isValid(Object value) {
		if(value == null) return true;
		if(value instanceof String == false) return false;
		String s = (String) value;
		return (s.length() < 9) ? false : (new Perl5Util()).match(ValidationUtil.SSN_REGEXP, StringUtils.strip(s));
	}

	public void apply(Property property) {
		// no-op
	}

}
