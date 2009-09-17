/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.common.bind;

import java.util.EventObject;

/**
 * PropertyChangeEvent
 * <p><em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
@SuppressWarnings("serial")
public class PropertyChangeEvent extends EventObject {

	private final String propertyName;

	private final Object oldValue;

	private final Object newValue;

	/**
	 * Constructor
	 * @param source
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	public PropertyChangeEvent(Object source, String propertyName, Object oldValue, Object newValue) {
		super(source);

		this.propertyName = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * @return The property name.
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return The old value.
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @return The new value.
	 */
	public Object getNewValue() {
		return newValue;
	}
}
