package com.tll.common.bind;

/**
 * PropertyChangeListenerProxy
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public class PropertyChangeListenerProxy extends EventListenerProxy implements IPropertyChangeListener {

	private final String propertyName;

	/**
	 * Constructor
	 * @param propertyName
	 * @param listener
	 */
	// TODO do we need this?
	public PropertyChangeListenerProxy(String propertyName, IPropertyChangeListener listener) {
		super(listener);
		this.propertyName = propertyName;
	}

	/**
	 * @return The property name.
	 */
	public String getPropertyName() {
		return propertyName;
	}

	public void propertyChange(PropertyChangeEvent event) {
		IPropertyChangeListener listener = (IPropertyChangeListener) getListener();
		listener.propertyChange(event);
	}
}
