package com.tll.criteria;

import java.io.Serializable;

/**
 * Criterion definition.
 * @author jpk
 */
public interface ICriterion extends Serializable {

	/**
	 * Returns the property name used by the UI for this criterion. In most cases,
	 * the property name will be equivalent to the field name. However, in the
	 * case of foreign key fields, the field value will include the ".id" whereas
	 * the property name will be only the reference entity name.
	 * @return the property name
	 */
	String getPropertyName();

	/**
	 * Returns the name of the field used by the persistence framework.
	 * @return the name of the field
	 * @see #getPropertyName()
	 */
	String getField();

	/**
	 * Returns the value for the field that should be applied to the query.
	 * @return the value to used in the query
	 */
	Object getValue();

	/**
	 * Returns the comparator that should be used in the query as the relationship
	 * between the field and the value.
	 * @return the comparator enum
	 */
	Comparator getComparator();

	/**
	 * Returns true if this criterion is set. By default, this method checks to
	 * see if a value is set. Subclasses may override this to provide more complex
	 * logic if necessary
	 * @return true if criterion is defined, false otherwise
	 */
	boolean isSet();

	/**
	 * @return true if this {@link ICriterion} is a {@link CriterionGroup}, false
	 *         if not.
	 */
	boolean isGroup();

	/**
	 * Clears the value of this criterion.
	 */
	void clear();

	/**
	 * Returns true if this criterion is case sensitive. False otherwise. Certain
	 * subclasses will never be case sensitive, such as a boolean criterion and
	 * can override this method to always return false.
	 * @return true if this criterion is case sensitive, false otherwise.
	 */
	boolean isCaseSensitive();
}
