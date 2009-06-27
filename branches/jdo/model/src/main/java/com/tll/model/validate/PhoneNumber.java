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

import javax.validation.Constraint;

/**
 * PhoneNumber - Indicates the annotatee is a phone number.
 * @author jpk
 */
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PhoneNumber {

	/**
	 * @return The name of the bean property that holds the phone number to
	 *         validate.
	 */
	String phonePropertyName() default "phone";

	/**
	 * @return The name of the bean property that holds the country code.
	 */
	String countryPropertyName() default "country";

	String message() default "{validator.phone_number}";

	Class<?>[] groups() default {};
}
