package com.tll.client.bind;

import java.util.Collection;

import com.tll.client.ui.IBindableWidget;
import com.tll.common.bind.IModel;

/**
 * IBindingAction - Defines a very generalized binding action. The operations
 * performed upon {@link #execute()} are implmentation dependent.
 * @param <V> the value type
 * @author jpk
 */
public interface IBindingAction<V> extends IAction {
	
	/**
	 * Sets the given widget to this binding action.
	 * @param widget the widget to set
	 */
	void set(IBindableWidget<V> widget);

	/**
	 * Establishes the primary (non-indexed) bindings. <br>
	 * NOTE: A bound widget must be set.
	 * @param widget The widget to bind
	 */
	void bind(IBindableWidget<V> widget);

	/**
	 * Binds an "indexed" bindable widget to a "parent" binding action.
	 * @param widget
	 * @param indexedPropertyName the name of the indexed property
	 */
	void bindIndexed(IBindableWidget<Collection<IModel>> widget, String indexedPropertyName);

	/**
	 * Unbinds the currently bound widget performing necessary clean up.
	 * @param widget The widget to unbind
	 */
	void unbind(IBindableWidget<V> widget);

	/**
	 * Clears the reference that was set via {@link #set(IBindableWidget)}.
	 * {@link #set(IBindableWidget)} may subsequently be called after this method
	 * is called.
	 * @param widget
	 */
	void unset(IBindableWidget<V> widget);
}
