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

/**
 * PhoneNumbers - Provides the ability to group one or more {@link PhoneNumber}
 * s.
 * @author jpk
 */
@Target(value = {
	ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PhoneNumbers {

	PhoneNumber[] value();
}