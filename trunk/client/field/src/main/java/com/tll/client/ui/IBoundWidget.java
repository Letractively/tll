package com.tll.client.ui;

import java.util.Comparator;

import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.tll.client.bind.IBindingAction;
import com.tll.client.convert.IConverter;
import com.tll.common.bind.IBindable;

/**
 * IBoundWidget - Common base class for a UI widgets that are boundWidget.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em> @author
 * jpk
 * @param <B> The "bound" type. That is the value type on the other side of the
 *        binding that binds this boundWidget.
 * @param <V> The value type or the type this instance internally holds as the
 *        value.
 * @param <M> The model type which must be boundWidget.
 */
public interface IBoundWidget<B, V, M extends IBindable> extends IBindable, HasChangeHandlers {

	/**
	 * Generic token indicating the name of the value property. Used when firing
	 * property change events.
	 */
	static final String PROPERTY_VALUE = "value";

	/**
	 * Generic token indicating DOM attachment. Used when firing property change
	 * events.
	 */
	// static final String PROPERTY_ATTACHED = "attached";
	/**
	 * Generic token indicating the name of the model property. Used when firing
	 * property change events.
	 */
	static final String PROPERTY_MODEL = "model";

	/**
	 * @return The action
	 */
	IBindingAction getAction();

	/**
	 * Sets the action.
	 * @param action The action to set
	 */
	void setAction(IBindingAction action);

	/**
	 * @return The employed {@link Comparator} for this bound widget.
	 */
	Comparator<Object> getComparator();

	/**
	 * Optional tool used in determining whether a given bound value is the same
	 * as that currently held in this widget.
	 * @param comparator
	 */
	void setComparator(Comparator<Object> comparator);

	/**
	 * A placeholder for access to the object at the other end of the binding
	 * which for bound widgets usually means a data structure of sorts which we
	 * choose to call model.
	 * @return The model containing the bit of data this widget displays.
	 */
	M getModel();

	/**
	 * Sets the model.
	 * @param model The model to set
	 */
	void setModel(M model);

	/**
	 * Responsible for providing a render ready value for display in the UI.
	 * @return The converter.
	 */
	IConverter<V, B> getConverter();

	/**
	 * Sets the converter.
	 * @param converter The converter to set
	 */
	void setConverter(IConverter<V, B> converter);

	/**
	 * Gets the value in the "native" widget type.
	 * @return The [widget] value
	 */
	V getValue();

	/**
	 * Sets the value.
	 * @param value The "bound" value to set
	 */
	void setValue(B value);
}
