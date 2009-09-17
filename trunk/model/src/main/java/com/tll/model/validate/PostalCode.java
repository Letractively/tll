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
import javax.validation.Payload;

/**
 * PostalCode - Indicates the annotatee is a postal code (zip code for US).
 * @author jpk
 */
@Constraint(validatedBy = PostalCodeValidator.class)
@Target(value = {
	ElementType.TYPE, ElementType.ANNOTATION_TYPE })
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

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
