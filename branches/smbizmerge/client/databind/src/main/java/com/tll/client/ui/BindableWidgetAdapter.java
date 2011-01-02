/**
 * The Logic Lab
 * @author jpk
 * @since Mar 10, 2009
 */
package com.tll.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.convert.IConverter;
import com.tll.common.model.IPropertyChangeListener;
import com.tll.common.model.MalformedPropPathException;
import com.tll.common.model.PropertyChangeSupport;
import com.tll.common.model.PropertyPathException;

/**
 * BindableWidgetAdapter - Enables widget bindability by composition (rather
 * than by inheritance). A widget wishing to realize bindability using
 * {@link BindableWidgetAdapter} must first inherit {@link IBindableWidget}. The
 * method impls then delegate to the {@link BindableWidgetAdapter} data memeber
 * instance.
 * <p>
 * The methods: {@link #getValue()}, {@link #setValue(Object)} and
 * {@link #setValue(Object, boolean)} must be implemented by the delegator.
 * <p>
 * The {@link Widget} methods: <code>fireEvent()</code> and
 * <code>addValueChangeHandler(...)</code> must be implemented by the delegator
 * since they are protected methods.
 * @author jpk
 * @param <V> the value type
 */
public final class BindableWidgetAdapter<V> implements IBindableWidget<V> {

	/**
	 * The target on which this adapter acts.
	 */
	private final IBindableWidget<V> target;

	/**
	 * Responsible for disseminating <em>property</em> change events.
	 */
	protected final PropertyChangeSupport changeSupport;

	/**
	 * Constructor
	 * @param target
	 */
	public BindableWidgetAdapter(IBindableWidget<V> target) {
		if(target == null) throw new IllegalArgumentException();
		this.target = target;
		changeSupport = new PropertyChangeSupport(target);
	}

	public PropertyChangeSupport getChangeSupport() {
		return changeSupport;
	}

	@Override
	public Object getProperty(String propPath) throws PropertyPathException {
		if(!IBindableWidget.PROPERTY_VALUE.equals(propPath)) {
			throw new MalformedPropPathException(propPath);
		}
		return getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setProperty(String propPath, Object value) throws PropertyPathException, IllegalArgumentException {
		if(!IBindableWidget.PROPERTY_VALUE.equals(propPath)) {
			throw new MalformedPropPathException(propPath);
		}
		IConverter<V, Object> converter = target.getConverter();
		if(converter == null) {
			// attempt to cast
			try {
				setValue((V) value);
			}
			catch(final ClassCastException e) {
				throw new IllegalArgumentException("Unable to coerce the value type - employ a converter");
			}
		}
		else {
			// employ the provided converter
			setValue(converter.convert(value));
		}
	}

	@Override
	public V getValue() {
		return target.getValue();
	}

	@Override
	public void setValue(V value) {
		target.setValue(value);
	}

	@Override
	public void setValue(V value, boolean fireEvents) {
		target.setValue(value, fireEvents);
	}

	@Override
	public IConverter<V, Object> getConverter() {
		return target.getConverter(); 
	}

	@Override
	public void setConverter(IConverter<V, Object> converter) {
		target.setConverter(converter);
	}

	@Override
	public final void addPropertyChangeListener(IPropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(l);
	}

	@Override
	public final void addPropertyChangeListener(String propertyName, IPropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(propertyName, l);
	}

	@Override
	public final void removePropertyChangeListener(IPropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(l);
	}

	@Override
	public final void removePropertyChangeListener(String propertyName, IPropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(propertyName, l);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		target.fireEvent(event);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<V> handler) {
		return target.addValueChangeHandler(handler);
	}
}
