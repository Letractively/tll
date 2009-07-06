/*
 * The Logic Lab
 */
package com.tll.criteria;

/**
 * IComparatorTranslator
 * @author jpk
 * @param <T> the dao impl type to which native criteria is translated
 */
public interface IComparatorTranslator<T> {

	/**
	 * Translates a native criterion instance to a dao impl specific construct.
	 * @param ctn the native criterion
	 * @return the translated dao impl specific type
	 */
	T translate(ICriterion ctn);
}
