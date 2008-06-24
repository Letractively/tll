/*
 * The Logic Lab 
 */
package com.tll.key;

import java.io.Serializable;

import com.tll.util.IDescriptorProvider;

/**
 * IKey - Abstraction serving as an identifier to a particular instance of a
 * particular type.
 * @author jpk
 */
public interface IKey extends IDescriptorProvider, Serializable {

	/**
	 * @return The type of object to which this key refers.
	 */
	Class<?> getType();

	/**
	 * @return <code>true</code> if the defining key properties have been set.
	 */
	boolean isSet();

	/**
	 * Clear the state of this key resetting all defining properties to their
	 * default values.
	 */
	void clear();
}
