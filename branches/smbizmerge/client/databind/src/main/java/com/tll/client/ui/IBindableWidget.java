package com.tll.client.ui;

import com.google.gwt.user.client.ui.HasValue;
import com.tll.client.convert.IHasConverter;
import com.tll.common.model.IBindable;

/**
 * Extension of {@link IBindable} relevant to ui widgets capable of
 * participating in a binding.
 * @param <V> the value type
 * @author jpk
 */
public interface IBindableWidget<V> extends IBindable, HasValue<V>, IHasConverter<Object, V> {
	
	/**
	 * Generic token indicating the name of the value property.
	 */
	static final String PROPERTY_VALUE = "value";
}
