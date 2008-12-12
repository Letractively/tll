/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

/**
 * PostalCode
 * @author jpk
 */
@ValidatorClass(PostalCodeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PostalCode {

	/**
	 * @return The name of the bean property that holds the postal code to
	 *         validate.
	 */
	String postalCodePropertyName() default "postalCode";

	/**
	 * @return The name of the bean property that holds the country code.
	 */
	String countryPropertyName() default "country";

	String message() default "{validator.postal_code}";

}
