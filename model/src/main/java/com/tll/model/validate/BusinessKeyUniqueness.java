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
 * BusinessKeyUniqueness - Applied on a collection of entities. This edit
 * ensures all entities w/in the collection are unique against oneanother based
 * on the defined business keys for the entity type of the collection.
 * @author jpk
 */
@Constraint(validatedBy = BusinessKeyUniquenessValidator.class)
@Target(value = {
	ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessKeyUniqueness {

	String type();

	String message() default "{validator.business_key_uniqueness}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
