package com.tll.criteria;

import java.io.Serializable;

import com.tll.client.model.IPropertyValue;

/**
 * Criterion definition.
 * @author jpk
 */
public interface ICriterion extends Serializable {

	/**
	 * @return The property value containing the property name and search value.
	 */
	IPropertyValue getPropertyValue();

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
