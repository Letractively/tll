package com.tll.common.bind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * PropertyChangeSupport - This is a utility class that can be used by beans
 * that support bound properties.
 * <p>
 * Note: The emulated version differs from the JDK version in that it is
 * <em>not</em> Serializable.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public class PropertyChangeSupport {

	private transient final Object sourceBean;

	private transient final List<IPropertyChangeListener> allPropertiesChangeListeners =
			new ArrayList<IPropertyChangeListener>();

	private transient final Map<String, List<IPropertyChangeListener>> selectedPropertiesChangeListeners =
			new HashMap<String, List<IPropertyChangeListener>>();

	public PropertyChangeSupport(final Object sourceBean) {
		if(sourceBean == null) {
			throw new NullPointerException();
		}
		this.sourceBean = sourceBean;
	}

	public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		final PropertyChangeEvent event = createPropertyChangeEvent(propertyName, oldValue, newValue);
		doFirePropertyChange(event);
	}

	public void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {

		// nulls and equals check done in doFire...
		doFirePropertyChange(new IndexedPropertyChangeEvent(sourceBean, propertyName, oldValue, newValue, index));
	}

	public synchronized void removePropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		if((propertyName != null) && (listener != null)) {
			final List<IPropertyChangeListener> listeners = selectedPropertiesChangeListeners.get(propertyName);
			if(listeners != null) {
				listeners.remove(listener);
			}
		}
	}

	public synchronized void addPropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		if((listener != null) && (propertyName != null)) {
			List<IPropertyChangeListener> listeners = selectedPropertiesChangeListeners.get(propertyName);

			if(listeners == null) {
				listeners = new ArrayList<IPropertyChangeListener>();
				selectedPropertiesChangeListeners.put(propertyName, listeners);
			}

			listeners.add(listener);
		}
	}

	public synchronized boolean hasListeners(String propertyName) {
		boolean result = allPropertiesChangeListeners.size() > 0;
		if(!result && (propertyName != null)) {
			final List<IPropertyChangeListener> listeners = selectedPropertiesChangeListeners.get(propertyName);
			if(listeners != null) {
				result = listeners.size() > 0;
			}
		}
		return result;
	}

	public synchronized void removePropertyChangeListener(IPropertyChangeListener listener) {
		if(listener != null) {
			allPropertiesChangeListeners.remove(listener);
		}
	}

	public synchronized void addPropertyChangeListener(IPropertyChangeListener listener) {
		if(listener != null) {
			allPropertiesChangeListeners.add(listener);
		}
	}

	public void firePropertyChange(PropertyChangeEvent event) {
		doFirePropertyChange(event);
	}

	private PropertyChangeEvent createPropertyChangeEvent(String propertyName, Object oldValue, Object newValue) {
		return new PropertyChangeEvent(sourceBean, propertyName, oldValue, newValue);
	}

	private void doFirePropertyChange(PropertyChangeEvent event) {
		final String propertyName = event.getPropertyName();
		final Object oldValue = event.getOldValue();
		final Object newValue = event.getNewValue();

		if((newValue != null) && (oldValue != null) && newValue.equals(oldValue)) {
			return;
		}

		/*
		 * Copy the listeners collections so they can be modified while we fire
		 * events.
		 */
		final List<IPropertyChangeListener> allListeners = new ArrayList<IPropertyChangeListener>();

		// Listeners to all property change events
		allListeners.addAll(allPropertiesChangeListeners);

		// Listens to a given property change
		final List<IPropertyChangeListener> listeners = selectedPropertiesChangeListeners.get(propertyName);
		if(listeners != null) {
			allListeners.addAll(listeners);
		}

		// Fire the listeners
		for(final Iterator<IPropertyChangeListener> iter = allListeners.iterator(); iter.hasNext();) {
			final IPropertyChangeListener listener = iter.next();
			listener.propertyChange(event);
		}
	}
}
