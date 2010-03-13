package com.tll.client.ui;

import com.google.gwt.user.client.ui.HasValue;
import com.tll.client.convert.IConverter;
import com.tll.common.bind.IBindable;

/**
 * IBindableWidget - Extension of {@link IBindable} relevant to ui widgets
 * capable of participating in a binding.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @param <V> the value type
 * @author jpk
 */
public interface IBindableWidget<V> extends IBindable, HasValue<V> {
	
	/**
	 * Generic token indicating the name of the value property.
	 */
	static final String PROPERTY_VALUE = "value";

	/**
	 * @return Optional converter used to type coerce un-typed inbound values when
	 *         {@link #setProperty(String, Object)} is called.
	 */
	IConverter<V, Object> getConverter();
	
	/**
	 * Sets the converter.
	 * @param converter
	 */
	void setConverter(IConverter<V, Object> converter);
}
