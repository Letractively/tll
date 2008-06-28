/*
 * The Logic Lab 
 */
package com.tll.criteria;


/**
 * IComparatorTranslator
 * @author jpk
 */
public interface IComparatorTranslator<T> {

	/**
	 * Translates the native comparator to the desired type.
	 * @param c
	 * @param name
	 * @param value
	 * @return the desired type <T>
	 */
	T translate(Comparator c, String name, Object value);
}
