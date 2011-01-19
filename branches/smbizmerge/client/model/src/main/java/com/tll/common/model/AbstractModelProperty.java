/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.common.model;


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
		// Logger.debug("AbstractModelProperty() - default constructor!");
	}

	/**
	 * Constructor
	 * @param propertyName
	 */
	protected AbstractModelProperty(final String propertyName) {
		super();
		/* we can't check for this due to RPC constraints.
		if(KStringUtil.isEmpty(propertyName)) {
			throw new IllegalArgumentException("A property name must be specified");
		}
		 */
		// Logger.debug("Creating model property: " + propertyName);
		// assert propertyName != null;
		this.propertyName = propertyName;
	}

	@Override
	public final String getPropertyName() {
		return propertyName;
	}

	// NOTE: we ignore the propPath
	@Override
	public Object getProperty(final String propPath) {
		return getValue();
	}

	// NOTE: we ignore the propPath
	@Override
	public void setProperty(final String propPath, final Object value) throws PropertyPathException,
			IllegalArgumentException {
		setValue(value);
	}

	protected final PropertyChangeSupport getChangeSupport() {
		if(changeSupport == null) {
			changeSupport = new PropertyChangeSupport(this);
		}
		return changeSupport;
	}

	@Override
	public final void addPropertyChangeListener(final IPropertyChangeListener listener) {
		getChangeSupport().addPropertyChangeListener(listener);
	}

	@Override
	public final void addPropertyChangeListener(final String propName, final IPropertyChangeListener listener) {
		getChangeSupport().addPropertyChangeListener(propName, listener);
	}

	@Override
	public final void removePropertyChangeListener(final IPropertyChangeListener listener) {
		getChangeSupport().removePropertyChangeListener(listener);
	}

	@Override
	public final void removePropertyChangeListener(final String propName, final IPropertyChangeListener listener) {
		getChangeSupport().removePropertyChangeListener(propName, listener);
	}

	/*
	 * We want physical (by memory address) comparison ONLY
	 */
	@Override
	public final boolean equals(final Object obj) {
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
