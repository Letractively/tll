/*
 * The Logic Lab 
 */
package com.tll.key;

import java.io.Serializable;

import com.tll.util.IDescriptorProvider;

/**
 * IKey - Abstraction serving as an identifier to a given type <T> with
 * clonability and comparability.
 * @param <T> the key type.
 * @author jpk
 */
public interface IKey<T> extends IDescriptorProvider, Cloneable, Comparable<IKey<T>>, Serializable {

	/**
	 * @return The type of object to which this key refers.
	 */
	Class<T> getType();

	/**
	 * @return <code>true</code> if the defining key properties have been set.
	 */
	boolean isSet();

	/**
	 * Clear the state of this key resetting all defining properties to their
	 * default values.
	 */
	void clear();

	/**
	 * Publicly accessible method to clone this key.
	 * @return A clone of this key.
	 */
	IKey<T> copy();
}
