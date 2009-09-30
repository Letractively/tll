/**
 * The Logic Lab
 * @author jpk
 * @since Sep 27, 2009
 */
package com.tll.client.ui.field;

import com.tll.client.bind.Binding;
import com.tll.client.bind.BindingException;

/**
 * IFieldBindingBuilder
 * @author jpk
 */
public interface IFieldBindingBuilder {

	/**
	 * Necessary pre-requisite before {@link #createBindings(Binding)} is invoked.
	 * @param widget the required field panel ref
	 */
	void set(IFieldBoundWidget widget);

	/**
	 * Populates the given binding instance with newly created child bindings.
	 * @param binding the parent binding to which the created bindings are added
	 * @throws BindingException Upon a binding creation error
	 */
	void createBindings(Binding binding) throws BindingException;
}
