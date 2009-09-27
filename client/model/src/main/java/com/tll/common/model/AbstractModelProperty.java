/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.common.model;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.bind.ISourcesPropertyChangeEvents;
import com.tll.common.bind.PropertyChangeSupport;

/**
 * AbstractModelProperty - Base class for all implemented {@link IModelProperty}
 * s.
 * @author jpk
 */
public abstract class AbstractModelProperty implements IModelProperty {

	/**
	 * The property name.
	 */
	protected/*final*/String propertyName;

	/**
	 * Needed for {@link ISourcesPropertyChangeEvents} implementation. <br>
	 * <b>NOTE: </b>This member is <em>not</em> intended for RPC marshaling.
	 */
	private transient PropertyChangeSupport changeSupport;

	/**
	 * Constructor
	 */
	public AbstractModelProperty() {
		super();
		Log.debug("AbstractModelProperty() - default constructor!");
	}

	/**
	 * Constructor
	 * @param propertyName
	 */
	protected AbstractModelProperty(String propertyName) {
		super();
		/* we can't check for this due to RPC constraints.
		if(KStringUtil.isEmpty(propertyName)) {
			throw new IllegalArgumentException("A property name must be specified");
		}
		 */
		//Log.debug("Creating model property: " + propertyName);
		//assert propertyName != null;
		this.propertyName = propertyName;
	}

	public final String getPropertyName() {
		return propertyName;
	}

	// NOTE: we ignore the propPath
	public Object getProperty(String propPath) {
		return getValue();
	}

	// NOTE: we ignore the propPath
	public void setProperty(String propPath, Object value) throws PropertyPathException, IllegalArgumentException {
		setValue(value);
	}

	protected final PropertyChangeSupport getChangeSupport() {
		if(changeSupport == null) {
			changeSupport = new PropertyChangeSupport(this);
		}
		return changeSupport;
	}

	public final void addPropertyChangeListener(IPropertyChangeListener listener) {
		getChangeSupport().addPropertyChangeListener(listener);
	}

	public final void addPropertyChangeListener(String propName, IPropertyChangeListener listener) {
		getChangeSupport().addPropertyChangeListener(propName, listener);
	}

	public final void removePropertyChangeListener(IPropertyChangeListener listener) {
		getChangeSupport().removePropertyChangeListener(listener);
	}

	public final void removePropertyChangeListener(String propName, IPropertyChangeListener listener) {
		getChangeSupport().removePropertyChangeListener(propName, listener);
	}

	/*
	 * We want physical (by memory address) comparison ONLY
	 */
	@Override
	public final boolean equals(Object obj) {
		return super.equals(obj);
	}

	/*
	 * We want physical (by memory address) comparison ONLY
	 */
	@Override
	public final int hashCode() {
		return super.hashCode();
	}
}