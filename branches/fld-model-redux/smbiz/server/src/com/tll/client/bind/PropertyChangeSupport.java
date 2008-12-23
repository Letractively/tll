package com.tll.client.bind;

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
 * <p><em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 *         </p>
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
			List<IPropertyChangeListener> listeners = selectedPropertiesChangeListeners.get(propertyName);
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

			// RI compatibility
			if(listener instanceof PropertyChangeListenerProxy) {
				PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy) listener;

				listeners.add(new PropertyChangeListenerProxy(proxy.getPropertyName(), (IPropertyChangeListener) proxy
						.getListener()));
			}
			else {
				listeners.add(listener);
			}
		}
	}

	public synchronized IPropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		List<IPropertyChangeListener> listeners = null;

		if(propertyName != null) {
			listeners = selectedPropertiesChangeListeners.get(propertyName);
		}

		if(listeners == null) {
			return new IPropertyChangeListener[] {};
		}

		IPropertyChangeListener[] changeListeners = new IPropertyChangeListener[listeners.size()];
		Iterator<IPropertyChangeListener> iter = listeners.iterator();
		for(int i = 0; i < changeListeners.length; i++) {
			changeListeners[i] = iter.next();
		}
		return changeListeners;
	}

	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		PropertyChangeEvent event = createPropertyChangeEvent(propertyName, oldValue, newValue);
		doFirePropertyChange(event);
	}

	public void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {

		if(oldValue != newValue) {
			fireIndexedPropertyChange(propertyName, index, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
		}
	}

	public void firePropertyChange(String propertyName, int oldValue, int newValue) {
		PropertyChangeEvent event = createPropertyChangeEvent(propertyName, oldValue, newValue);
		doFirePropertyChange(event);
	}

	public void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {

		if(oldValue != newValue) {
			fireIndexedPropertyChange(propertyName, index, new Integer(oldValue), new Integer(newValue));
		}
	}

	public synchronized boolean hasListeners(String propertyName) {
		boolean result = allPropertiesChangeListeners.size() > 0;
		if(!result && (propertyName != null)) {
			List<IPropertyChangeListener> listeners = selectedPropertiesChangeListeners.get(propertyName);
			if(listeners != null) {
				result = listeners.size() > 0;
			}
		}
		return result;
	}

	public synchronized void removePropertyChangeListener(IPropertyChangeListener listener) {
		if(listener != null) {
			if(listener instanceof PropertyChangeListenerProxy) {
				String name = ((PropertyChangeListenerProxy) listener).getPropertyName();
				IPropertyChangeListener lst = (IPropertyChangeListener) ((PropertyChangeListenerProxy) listener).getListener();

				removePropertyChangeListener(name, lst);
			}
			else {
				allPropertiesChangeListeners.remove(listener);
			}
		}
	}

	public synchronized void addPropertyChangeListener(IPropertyChangeListener listener) {
		if(listener != null) {
			if(listener instanceof PropertyChangeListenerProxy) {
				String name = ((PropertyChangeListenerProxy) listener).getPropertyName();
				IPropertyChangeListener lst = (IPropertyChangeListener) ((PropertyChangeListenerProxy) listener).getListener();
				addPropertyChangeListener(name, lst);
			}
			else {
				allPropertiesChangeListeners.add(listener);
			}
		}
	}

	public synchronized IPropertyChangeListener[] getPropertyChangeListeners() {
		ArrayList<IPropertyChangeListener> result = new ArrayList<IPropertyChangeListener>(allPropertiesChangeListeners);

		for(Iterator<String> it = selectedPropertiesChangeListeners.keySet().iterator(); it.hasNext();) {
			String propertyName = it.next();
			List<IPropertyChangeListener> selectedListeners = selectedPropertiesChangeListeners.get(propertyName);

			if(selectedListeners != null) {
				for(Iterator<IPropertyChangeListener> it1 = selectedListeners.iterator(); it1.hasNext();) {
					IPropertyChangeListener listener = it1.next();
					result.add(new PropertyChangeListenerProxy(propertyName, listener));
				}
			}
		}

		IPropertyChangeListener[] changeListeners = new IPropertyChangeListener[result.size()];
		Iterator<IPropertyChangeListener> iter = result.iterator();
		for(int i = 0; i < changeListeners.length; i++) {
			changeListeners[i] = iter.next();
		}
		return changeListeners;
	}

	public void firePropertyChange(PropertyChangeEvent event) {
		doFirePropertyChange(event);
	}

	private PropertyChangeEvent createPropertyChangeEvent(String propertyName, Object oldValue, Object newValue) {
		return new PropertyChangeEvent(sourceBean, propertyName, oldValue, newValue);
	}

	private PropertyChangeEvent createPropertyChangeEvent(String propertyName, boolean oldValue, boolean newValue) {
		return new PropertyChangeEvent(sourceBean, propertyName, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
	}

	private PropertyChangeEvent createPropertyChangeEvent(String propertyName, int oldValue, int newValue) {
		return new PropertyChangeEvent(sourceBean, propertyName, new Integer(oldValue), new Integer(newValue));
	}

	private void doFirePropertyChange(PropertyChangeEvent event) {
		String propertyName = event.getPropertyName();
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if((newValue != null) && (oldValue != null) && newValue.equals(oldValue)) {
			return;
		}

		/*
		 * Copy the listeners collections so they can be modified while we fire
		 * events.
		 */
		List<IPropertyChangeListener> allListeners = new ArrayList<IPropertyChangeListener>();

		// Listeners to all property change events
		allListeners.addAll(allPropertiesChangeListeners);

		// Listens to a given property change
		List<IPropertyChangeListener> listeners = selectedPropertiesChangeListeners.get(propertyName);
		if(listeners != null) {
			allListeners.addAll(listeners);
		}

		// Fire the listeners
		for(Iterator<IPropertyChangeListener> iter = allListeners.iterator(); iter.hasNext();) {
			IPropertyChangeListener listener = iter.next();
			listener.propertyChange(event);
		}
	}
}
