/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.common.model;

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
	}

	/**
	 * Constructor
	 * @param propertyName
	 */
	protected AbstractModelProperty(String propertyName) {
		super();
		/* we can't check for this due to RPC constraints.
		if(StringUtil.isEmpty(propertyName)) {
			throw new IllegalArgumentException("A property name must be specified");
		}
		*/
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
	public void setProperty(String propPath, Object value) throws PropertyPathException, Exception {
		try {
			setValue(value);
		}
		catch(IllegalArgumentException e) {
			// bad value
			throw new Exception(e);
		}
	}

	protected PropertyChangeSupport getChangeSupport() {
		if(changeSupport == null) {
			changeSupport = new PropertyChangeSupport(this);
		}
		return changeSupport;
	}

	public final void addPropertyChangeListener(IPropertyChangeListener listener) {
		getChangeSupport().addPropertyChangeListener(listener);
	}

	public final void addPropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		getChangeSupport().addPropertyChangeListener(propertyName, listener);
	}

	public final IPropertyChangeListener[] getPropertyChangeListeners() {
		return getChangeSupport().getPropertyChangeListeners();
	}

	public final void removePropertyChangeListener(IPropertyChangeListener listener) {
		getChangeSupport().removePropertyChangeListener(listener);
	}

	public final void removePropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		getChangeSupport().removePropertyChangeListener(propertyName, listener);
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
