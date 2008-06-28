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

import org.hibernate.validator.ValidatorClass;


/**
 * SSN - Social Security Number validation annotation.
 * @author jpk
 */
@ValidatorClass(SSNValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SSN {
	String message() default "{validator.ssn}";
}
