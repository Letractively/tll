/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.model;

import com.tll.client.bind.IPropertyChangeListener;
import com.tll.client.bind.ISourcesPropertyChangeEvents;
import com.tll.client.bind.PropertyChangeSupport;

/**
 * AbstractModelProperty
 * @author jpk
 */
public abstract class AbstractModelProperty implements IModelProperty {

	/**
	 * The property name.
	 */
	protected String propertyName;

	/**
	 * Needed for {@link ISourcesPropertyChangeEvents} implementation. <br>
	 * <b>NOTE: </b>This member is <em>not</em> intended for RPC marshaling.
	 */
	protected transient PropertyChangeSupport changeSupport;

	/**
	 * Constructor
	 */
	public AbstractModelProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param propertyName
	 */
	protected AbstractModelProperty(String propertyName) {
		super();
		this.propertyName = propertyName;
	}

	public final String getPropertyName() {
		return propertyName;
	}

	public final void setPropertyName(String name) {
		this.propertyName = name;
	}

	public Object getProperty(String propPath) throws PropertyPathException {
		if(!propertyName.equals(propPath)) {
			throw new MalformedPropPathException(propPath);
		}
		return getValue();
	}

	public void setProperty(String propPath, Object value) throws PropertyPathException {
		if(!propertyName.equals(propPath)) {
			throw new MalformedPropPathException(propPath);
		}
		setValue(value);
	}

	public final void setPropertyChangeSupport(PropertyChangeSupport changeSupport) {
		if(this.changeSupport != null && changeSupport != null && this.changeSupport != changeSupport) {
			throw new IllegalStateException("Model property '" + propertyName
					+ "' is already bound to a different property change support reference!");
		}
		this.changeSupport = changeSupport;
	}

	public final void addPropertyChangeListener(IPropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public final void addPropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public final IPropertyChangeListener[] getPropertyChangeListeners() {
		return changeSupport.getPropertyChangeListeners();
	}

	public final void removePropertyChangeListener(IPropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public final void removePropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public int hashCode() {
		return 31 + ((propertyName == null) ? 0 : propertyName.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final AbstractModelProperty other = (AbstractModelProperty) obj;
		if(propertyName == null) {
			if(other.propertyName != null) return false;
		}
		else if(!propertyName.equals(other.propertyName)) return false;
		return true;
	}

}
