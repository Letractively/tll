/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.client.bind;

/**
 * IndexedPropertyChangeEvent
 * <p><em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
@SuppressWarnings("serial")
public class IndexedPropertyChangeEvent extends PropertyChangeEvent {

	private final int index;

	/**
	 * Constructor Creates a new property changed event with an indication of the
	 * property index.
	 * @param source the changed bean.
	 * @param propertyName the changed property, or <code>null</code> to indicate
	 *        an unspecified set of the properties have changed.
	 * @param oldValue the previous value of the property, or <code>null</code> if
	 *        the <code>propertyName</code> is <code>null</code> or the previous
	 *        value is unknown.
	 * @param newValue the new value of the property, or <code>null</code> if the
	 *        <code>propertyName</code> is <code>null</code> or the new value is
	 *        unknown..
	 * @param index the index of the property.
	 */
	public IndexedPropertyChangeEvent(Object source, String propertyName, Object oldValue, Object newValue, int index) {
		super(source, propertyName, oldValue, newValue);
		this.index = index;
	}

	/**
	 * Answer the index of the property that was changed in this event.
	 * @return The property element index.
	 */
	public int getIndex() {
		return index;
	}
}
