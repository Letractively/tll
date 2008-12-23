/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.client.bind;

/**
 * ISourcesPropertyChangeEvents
 * <p><em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
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
	 * @return The property listeners.
	 */
	IPropertyChangeListener[] getPropertyChangeListeners();

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
