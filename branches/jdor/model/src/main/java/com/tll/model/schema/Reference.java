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
 * Reference - Indicates a related one entity or related many entities are
 * <em>not</em> part of the defult declaring entities' life-cycle.
 * @author jpk
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {

}
