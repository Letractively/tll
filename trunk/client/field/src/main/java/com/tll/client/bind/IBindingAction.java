package com.tll.client.bind;

import com.tll.client.ui.field.IFieldBoundWidget;

/**
 * IBindingAction - Defines a very generalized binding action. The operations
 * performed upon {@link #execute()} are implmentation dependent.
 * @author jpk
 */
public interface IBindingAction extends IAction {
	
	/**
	 * Sets the given widget to this binding action.
	 * @param widget the widget to set
	 */
	void set(IFieldBoundWidget widget);

	/**
	 * Establishes the primary (non-indexed) bindings. <br>
	 * NOTE: A bound widget must be set.
	 * @param widget The widget to bind
	 */
	void bind(IFieldBoundWidget widget);

	/**
	 * Unbinds the currently bound widget performing necessary clean up.
	 * @param widget The widget to unbind
	 */
	void unbind(IFieldBoundWidget widget);

	/**
	 * Clears the reference that was set via {@link #set(IFieldBoundWidget)}.
	 * {@link #set(IFieldBoundWidget)} may subsequently be called after this
	 * method is called.
	 * @param widget
	 */
	void unset(IFieldBoundWidget widget);
}
