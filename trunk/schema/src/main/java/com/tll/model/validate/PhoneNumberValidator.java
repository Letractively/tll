/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.oro.text.perl.Perl5Util;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;

import com.tll.IPropertyNameProvider;
import com.tll.util.ValidationUtil;

/**
 * PhoneNumberValidator
 * @author jpk
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, Object>, IPropertyNameProvider {

	private String phonePropertyName;
	private String countryPropertyName;

	@Override
	public String getPropertyName() {
		return phonePropertyName;
	}

	public void initialize(PhoneNumber parameters) {
		phonePropertyName = parameters.phonePropertyName();
		countryPropertyName = parameters.countryPropertyName();
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value == null) return true;
		final BeanWrapper bw = new BeanWrapperImpl(value);
		final Object pvPhone = bw.getPropertyValue(phonePropertyName);
		Object pvCountry = null;
		try {
			pvCountry = bw.getPropertyValue(countryPropertyName);
		}
		catch(final NotReadablePropertyException e) {
			// ok
		}
		if(pvPhone == null) return true;

		final String phoneNumber = ((String) pvPhone).trim().toLowerCase();
		final String country = pvCountry == null ? "us" : ((String) pvCountry).trim().toLowerCase();

		return (new Perl5Util()).match(ValidationUtil.isValidUsaStateAbbr(country) ? ValidationUtil.US_PHONE_REGEXP
				: ValidationUtil.INTNL_PHONE_REGEXP, phoneNumber);
	}

}
