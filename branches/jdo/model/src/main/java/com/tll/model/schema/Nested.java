/**
 * The Logic Lab
 * @author jkirton
 * May 13, 2008
 */
package com.tll.model.schema;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Nested - Indicates a nested "value object" that is part of the entity schema
 * where the nested type is not an entity on its accord.
 * <p>
 * This annotation is method bound where the following conventions are in
 * effect:
 * <ol>
 * <li>The method is assumed to be a property accessor bean method conforming to
 * java beans spec
 * <li>The resolved property name of this accessor method is used to
 * subsequently resolve properties in the nested object.
 * <li>The return type of the method is taken to be the nested type.
 * </ol>
 * <p>
 * The
 * @author jpk
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Nested {
}
