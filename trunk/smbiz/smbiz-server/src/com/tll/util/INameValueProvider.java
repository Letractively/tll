package com.tll.util;

/**
 * INameValueProvider - Definition for providing a name and an associated Object value.
 * @author jpk
 */
public interface INameValueProvider {

	/**
	 * @return The ascribed name.
	 */
	String getName();

	/**
	 * @return The value.
	 */
	Object getValue();

}