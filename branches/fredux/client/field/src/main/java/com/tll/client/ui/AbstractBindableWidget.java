package com.tll.client.ui;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.tll.client.bind.IBindingAction;
import com.tll.client.convert.IConverter;
import com.tll.common.bind.IBindable;
import com.tll.common.bind.IModel;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.bind.PropertyChangeSupport;
import com.tll.common.model.MalformedPropPathException;
import com.tll.common.model.PropertyPathException;

/**
 * AbstractBindableWidget
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @param <V> the value type
 * @author jpk
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBindableWidget<V> extends Composite implements
		IBindableWidget<V> {
	
	/**
	 * The converter for handling in-bound un-typed values.
	 */
	IConverter<V, Object> converter;

	/**
	 * The optional model.
	 */
	IModel model;

	/**
	 * The optional action.
	 */
	private IBindingAction<V> action;

	/**
	 * Responsible for handling validation exceptions.
	 */
	//private IErrorHandler errorHandler;

	/**
	 * Responsible for disseminating <em>property</em> change events.
	 */
	protected final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	@Override
	public final IBindingAction<V> getAction() {
		return action;
	}

	@Override
	public final void setAction(IBindingAction<V> action) {
		this.action = action;
	}

	@Override
	public final IModel getModel() {
		return model;
	}

	@Override
	public void setModel(IModel model) {
		// don't spuriously re-apply the same model instance!
		if(this.model != null && model == this.model) {
			return;
		}
		Log.debug("AbstractBindableWidget.setModel() - START");
		
		final IBindable old = this.model;
		
		if(old != null) {
			Log.debug("AbstractBindableWidget - unbinding existing action..");
			action.unbind(this);
		}

		this.model = model;

		if(action != null) {
			action.set(this);
			if(isAttached() && (model != null)) {
				Log.debug("AbstractBindableWidget - re-binding existing action..");
				action.bind(this);
			}
		}
		//Log.debug("AbstractBindableWidget - firing 'model' prop change event..");
		//changeSupport.firePropertyChange(PropertyChangeType.MODEL.prop(), old, model);
	}

	/*
	public final IErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(IErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
	*/
	
	@Override
	public final IPropertyChangeListener[] getPropertyChangeListeners() {
		return changeSupport.getPropertyChangeListeners();
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
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<V> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public final Object getProperty(String propPath) throws PropertyPathException {
		if(!IBindableWidget.PROPERTY_VALUE.equals(propPath)) {
			throw new MalformedPropPathException(propPath);
		}
		return getValue();
	}
	
	@Override
	public final void setProperty(String propPath, Object value) throws PropertyPathException, IllegalArgumentException {
		if(!IBindableWidget.PROPERTY_VALUE.equals(propPath)) {
			throw new MalformedPropPathException(propPath);
		}
		final IConverter<V, Object> converter = getConverter();
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
	public void setValue(V value, boolean fireEvents) {
		// default is to not support this method
		throw new UnsupportedOperationException();
	}

	@Override
	public final IConverter<V, Object> getConverter() {
		return converter;
	}

	@Override
	public final void setConverter(IConverter<V, Object> converter) {
		this.converter = converter;
	}

	@Override
	protected void onAttach() {
		Log.debug("Attaching " + toString() + "..");
	  if(action != null) {
			Log.debug("Setting action [" + action + "] on [" + this + "]..");
			action.set(this);
		}
		super.onAttach();
		//Log.debug("Firing prop change 'attach' event for " + toString() + "..");
		//changeSupport.firePropertyChange(PropertyChangeType.ATTACHED.prop(), false, true);
	}

	@Override
	protected void onLoad() {
		Log.debug("Loading " + toString() + "..");
		super.onLoad();
		if(action != null) {
			Log.debug("Binding action [" + action + "] on [" + this + "]..");
			action.bind(this);
		}
	}

	@Override
	protected void onDetach() {
		Log.debug("Detatching " + toString() + "..");
		super.onDetach();
		if(action != null) {
			Log.debug("Unbinding action [" + action + "] from [" + this + "]..");
			action.unbind(this);
		}
		//Log.debug("Firing prop change 'detach' event for " + toString() + "..");
		//changeSupport.firePropertyChange(PropertyChangeType.ATTACHED.prop(), true, false);
	}

	@Override
	public String toString() {
		return "Bound Widget";
	}
}
