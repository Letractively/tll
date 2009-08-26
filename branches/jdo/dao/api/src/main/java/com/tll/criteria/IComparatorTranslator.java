/*
 * The Logic Lab
 */
package com.tll.criteria;

/**
 * IComparatorTranslator
 * @author jpk
 * @param <T> the target type to which the given criteria is translated.
 */
public interface IComparatorTranslator<T> {

	/**
	 * Translates a native criterion instance to a target type.
	 * @param ctn the native criterion
	 * @return A translated type instance.
	 */
	T translate(Criterion ctn);
}
