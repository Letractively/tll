/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.common.model;

/**
 * ISourcesPropertyChangeEvents - Indicates the ability to fire
 * {@link PropertyChangeEvent}s.
 * @author jpk
 */
public interface ISourcesPropertyChangeEvents {

	/**
	 * Adds a property change listener.
	 * @param listener
	 */
	void addPropertyChangeListener(IPropertyChangeListener listener);

	/**
	 * Adds a property change listener.
	 * @param propertyName
	 * @param listener
	 */
	void addPropertyChangeListener(String propertyName, IPropertyChangeListener listener);

	/**
	 * Removes a property change listener.
	 * @param listener
	 */
	void removePropertyChangeListener(IPropertyChangeListener listener);

	/**
	 * Removes a property change listener.
	 * @param propertyName
	 * @param listener
	 */
	void removePropertyChangeListener(String propertyName, IPropertyChangeListener listener);

}
