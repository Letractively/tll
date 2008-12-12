/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

import org.apache.oro.text.perl.Perl5Util;
import org.hibernate.validator.Validator;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.tll.util.ValidationUtil;

/**
 * PhoneNumberValidator
 * @author jpk
 */
public class PhoneNumberValidator implements Validator<PhoneNumber>, IPropertyReference {

	private String phonePropertyName;
	private String countryPropertyName;
	private final BeanWrapper bw = new BeanWrapperImpl();

	public String getPropertyReference() {
		return phonePropertyName;
	}

	public void initialize(PhoneNumber parameters) {
		phonePropertyName = parameters.phonePropertyName();
		countryPropertyName = parameters.countryPropertyName();
	}

	public boolean isValid(Object value) {
		if(value == null)
			return true;
		bw.setWrappedInstance(value);
		Object pvPhone = bw.getPropertyValue(phonePropertyName);
		Object pvCountry = bw.getPropertyValue(countryPropertyName);
		if(pvPhone == null)
			return true;

		final String phoneNumber = ((String) pvPhone).trim().toLowerCase();
		final String country = pvCountry == null ? "us" : ((String) pvCountry).trim().toLowerCase();

		return (new Perl5Util()).match(ValidationUtil.isValidUsaStateAbbr(country) ? ValidationUtil.US_PHONE_REGEXP
				: ValidationUtil.INTNL_PHONE_REGEXP, phoneNumber);
	}

}
