/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2007
 */
package com.tll.model.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.perl.Perl5Util;

import com.tll.util.ValidationUtil;

/**
 * SSNValidator - Validates artifacts annotated with {@link SSN}.
 * @author jpk
 */
public class SSNValidator implements ConstraintValidator<SSN, String> {

	public void initialize(SSN parameters) {
		// no-op
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null) return true;
		return (value.length() < 9) ? false : (new Perl5Util()).match(ValidationUtil.SSN_REGEXP, StringUtils.strip(value));
	}

}
