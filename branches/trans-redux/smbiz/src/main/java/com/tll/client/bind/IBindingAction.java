package com.tll.client.bind;

import com.tll.common.bind.IBindable;

/**
 * IBindingAction - "Controls" a given component participating in a binding.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public interface IBindingAction extends IAction {

	/**
	 * Sets the bindable.
	 * @param <B> The {@link IBindable} type.
	 * @param bindable The boundWidget to set
	 */
	<B extends IBindable> void setBindable(B bindable);

	/**
	 * Establishes the bindings. A prior call to {@link #setBindable(IBindable)}
	 * is required.
	 */
	void bind();

	/**
	 * Unbinds what is currently bound performing necessary clean up.
	 */
	void unbind();
}
