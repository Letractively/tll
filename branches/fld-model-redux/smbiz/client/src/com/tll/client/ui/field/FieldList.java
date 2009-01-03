/**
 * The Logic Lab
 * @author jpk
 * Jan 3, 2009
 */
package com.tll.client.ui.field;

import java.util.Iterator;
import java.util.List;

import com.tll.client.bind.IBindable;
import com.tll.client.bind.IPropertyChangeListener;
import com.tll.client.bind.PropertyChangeSupport;
import com.tll.client.model.PropertyPathException;

/**
 * FieldList - Caters to the handling of indexed field collections.
 * @author jpk
 */
public final class FieldList implements Iterable<FieldGroup>, IBindable {

	/**
	 * The list of indexed fields.
	 */
	private List<FieldGroup> list;

	/**
	 * The aggregated property change support reference.
	 */
	private PropertyChangeSupport changeSupport;

	/**
	 * Constructor
	 */
	public FieldList() {
		super();
	}

	public Object getProperty(String propPath) throws PropertyPathException {
		return null;
	}

	public void setProperty(String propPath, Object value) throws PropertyPathException {
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
	}

	public void addPropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
	}

	public IPropertyChangeListener[] getPropertyChangeListeners() {
		return null;
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
	}

	public void removePropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
	}

	public Iterator<FieldGroup> iterator() {
		return list == null ? null : list.iterator();
	}
}
