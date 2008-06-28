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
 * AtLeastOne - Applied on a collection of entities. This edit
 * ensures all entities w/in the collection are unique against oneanother based
 * on the defined business keys for the entity type of the collection.
 * @author jpk
 */
@ValidatorClass(AtLeastOneValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AtLeastOne {
	String type();
	String message() default "{validator.at_least_one}";
}
