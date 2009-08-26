/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2007
 */
package com.tll.model.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

/**
 * SSN - Indicates the annotatee is a social security number.
 * @author jpk
 */
@Constraint(validatedBy = SSNValidator.class)
@Target(value={ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SSN {

	String message() default "{validator.ssn}";

	Class<?>[] groups() default {};
}
