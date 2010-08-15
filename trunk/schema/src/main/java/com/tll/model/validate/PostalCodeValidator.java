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
 * PostalCodeValidator - Validates artifacts annotated with {@link PostalCode}.
 * @author jpk
 */
public class PostalCodeValidator implements ConstraintValidator<PostalCode, Object>, IPropertyNameProvider {

	private String postalCodePropertyName;
	private String countryPropertyName;

	@Override
	public String getPropertyName() {
		return postalCodePropertyName;
	}

	public void initialize(PostalCode parameters) {
		postalCodePropertyName = parameters.postalCodePropertyName();
		countryPropertyName = parameters.countryPropertyName();
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value == null) return true;
		final BeanWrapper bw = new BeanWrapperImpl(value);
		final Object pvPostalCode = bw.getPropertyValue(postalCodePropertyName);
		Object pvCountry = null;
		try {
			pvCountry = bw.getPropertyValue(countryPropertyName);
		}
		catch(final NotReadablePropertyException e) {
			// ok
		}
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
