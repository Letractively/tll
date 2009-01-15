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
 * PostalCodeValidator - Validates artifacts annotated with {@link PhoneNumber}.
 * @author jpk
 */
public class PostalCodeValidator implements Validator<PostalCode>, IPropertyReference {

	private String postalCodePropertyName;
	private String countryPropertyName;
	private final BeanWrapper bw = new BeanWrapperImpl();

	public String getPropertyReference() {
		return postalCodePropertyName;
	}

	public void initialize(PostalCode parameters) {
		postalCodePropertyName = parameters.postalCodePropertyName();
		countryPropertyName = parameters.countryPropertyName();
	}

	public boolean isValid(Object value) {
		if(value == null) return true;
		bw.setWrappedInstance(value);
		Object pvPostalCode = bw.getPropertyValue(postalCodePropertyName);
		Object pvCountry = bw.getPropertyValue(countryPropertyName);
		if(pvPostalCode == null) return true;

		final String postalCode = ((String) pvPostalCode).trim().toLowerCase();
		final String country = pvCountry == null ? "us" : ((String) pvCountry).trim().toLowerCase();

		if(ValidationUtil.isValidUsaStateAbbr(country)) {
			return (new Perl5Util()).match(ValidationUtil.US_ZIPCODE_REGEXP, postalCode);
		}

		// currently no validation for internation postal codes
		return true;
	}
}
